package com.example.data

import androidx.room.withTransaction
import com.example.data.local.CartItemEntity
import com.example.data.local.DemoSeedData
import com.example.data.local.GramKartDatabase
import com.example.data.local.OrderEntity
import com.example.data.local.OrderItemEntity
import com.example.data.local.OrderWithItems
import com.example.data.local.ProductEntity
import com.example.data.local.UserPreferencesRepository
import com.example.data.model.CartItemDetail
import com.example.data.model.CartSummary
import com.example.data.model.CategorySales
import com.example.data.model.CustomerProfile
import com.example.data.model.OrderStatus
import com.example.data.model.ProductCategory
import com.example.data.model.SalesAnalytics
import com.example.ui.i18n.AppLanguage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

sealed interface PlaceOrderResult {
    data class Success(val orderCode: String, val orderId: Long) : PlaceOrderResult
    data object EmptyCart : PlaceOrderResult
    data object StockAdjusted : PlaceOrderResult
}

class GramKartRepository(
    private val db: GramKartDatabase,
    private val prefs: UserPreferencesRepository
) {
    private val productDao = db.productDao()
    private val cartDao = db.cartDao()
    private val orderDao = db.orderDao()

    val productsFlow: Flow<List<ProductEntity>> = productDao.observeAllProducts()
    val ordersFlow: Flow<List<OrderWithItems>> = orderDao.observeAllOrdersWithItems()
    val languageFlow: Flow<AppLanguage> = prefs.languageFlow
    val customerProfileFlow: Flow<CustomerProfile> = prefs.customerProfileFlow
    val isAdminLoggedInFlow: Flow<Boolean> = prefs.isAdminLoggedInFlow

    val cartSummaryFlow: Flow<CartSummary> = combine(
        cartDao.observeCartItems(),
        productDao.observeAllProducts()
    ) { cartItems, products ->
        val productMap = products.associateBy { it.id }
        val details = cartItems.mapNotNull { cartItem ->
            val product = productMap[cartItem.productId] ?: return@mapNotNull null
            if (product.stock <= 0 || cartItem.quantity <= 0) return@mapNotNull null
            val clampedQty = cartItem.quantity.coerceAtMost(product.stock)
            CartItemDetail(
                productId = product.id,
                nameEn = product.nameEn,
                nameHi = product.nameHi,
                unitEn = product.unitEn,
                unitHi = product.unitHi,
                priceInr = product.priceInr,
                mrpInr = product.mrpInr,
                stock = product.stock,
                imageKey = product.imageKey,
                categoryId = product.category,
                quantity = clampedQty
            )
        }
        val totalCount = details.sumOf { it.quantity }
        val subtotal = details.sumOf { it.lineTotalInr }
        val threshold = 299
        val deliveryFee = if (subtotal == 0 || subtotal >= threshold) 0 else 25
        CartSummary(
            items = details,
            totalCount = totalCount,
            subtotalInr = subtotal,
            deliveryFeeInr = deliveryFee,
            grandTotalInr = subtotal + deliveryFee,
            freeDeliveryThresholdInr = threshold
        )
    }

    val salesAnalyticsFlow: Flow<SalesAnalytics> = combine(
        ordersFlow,
        productsFlow
    ) { orders, products ->
        val nonCancelled = orders.filter {
            OrderStatus.fromId(it.order.status) != OrderStatus.CANCELLED
        }
        val totalSales = nonCancelled.sumOf { it.order.totalInr }
        val pending = orders.count { OrderStatus.fromId(it.order.status) == OrderStatus.PENDING }
        val accepted = orders.count {
            val st = OrderStatus.fromId(it.order.status)
            st == OrderStatus.CONFIRMED ||
                st == OrderStatus.PREPARING ||
                st == OrderStatus.OUT_FOR_DELIVERY
        }
        val delivered = orders.count { OrderStatus.fromId(it.order.status) == OrderStatus.DELIVERED }
        val rejected = orders.count { OrderStatus.fromId(it.order.status) == OrderStatus.CANCELLED }
        val lowStock = products.count { it.stock <= 5 }

        val categoryStats = ProductCategory.entries.map { cat ->
            val matchingItems = nonCancelled.flatMap { it.items }.filter { it.categoryId == cat.id }
            CategorySales(
                category = cat,
                revenueInr = matchingItems.sumOf { it.unitPriceInr * it.quantity },
                unitsSold = matchingItems.sumOf { it.quantity }
            )
        }.sortedByDescending { it.revenueInr }

        SalesAnalytics(
            totalSalesInr = totalSales,
            totalOrders = orders.size,
            pendingOrders = pending,
            acceptedOrders = accepted,
            deliveredOrders = delivered,
            rejectedOrders = rejected,
            lowStockCount = lowStock,
            totalProducts = products.size,
            categorySales = categoryStats
        )
    }

    val cartQuantitiesMapFlow: Flow<Map<Long, Int>> = cartDao.observeCartItems().map { list ->
        list.associate { it.productId to it.quantity }
    }

    suspend fun ensureSeeded() {
        val alreadySeeded = prefs.isSeeded()
        val currentProducts = productDao.getAllProductsOnce()
        if (!alreadySeeded || currentProducts.isEmpty()) {
            seedInitialData()
            prefs.setSeeded(true)
        }
    }

    suspend fun resetDemoData() {
        db.withTransaction {
            cartDao.clearCart()
            orderDao.clearAllOrders()
            productDao.clearAll()
            seedInitialDataInternal()
        }
        prefs.updateCustomerProfile(CustomerProfile())
        prefs.setSeeded(true)
    }

    private suspend fun seedInitialData() {
        db.withTransaction {
            cartDao.clearCart()
            orderDao.clearAllOrders()
            productDao.clearAll()
            seedInitialDataInternal()
        }
    }

    private suspend fun seedInitialDataInternal() {
        val sampleProducts = DemoSeedData.sampleProducts()
        productDao.insertAll(sampleProducts)
        val insertedProducts = productDao.getAllProductsOnce()

        DemoSeedData.sampleOrders().forEach { spec ->
            val orderId = orderDao.insertOrder(spec.order)
            val orderItems = spec.productIndices.mapNotNull { (idx, qty) ->
                val prod = insertedProducts.getOrNull(idx) ?: return@mapNotNull null
                OrderItemEntity(
                    orderId = orderId,
                    productId = prod.id,
                    productNameEn = prod.nameEn,
                    productNameHi = prod.nameHi,
                    categoryId = prod.category,
                    unitPriceInr = prod.priceInr,
                    quantity = qty,
                    unitEn = prod.unitEn,
                    unitHi = prod.unitHi,
                    imageKey = prod.imageKey
                )
            }
            orderDao.insertOrderItems(orderItems)
        }
    }

    suspend fun addToCart(productId: Long) {
        val product = productDao.getProductById(productId) ?: return
        if (product.stock <= 0) return
        val existing = cartDao.getCartItem(productId)
        val newQty = ((existing?.quantity ?: 0) + 1).coerceAtMost(product.stock)
        cartDao.upsertCartItem(CartItemEntity(productId = productId, quantity = newQty))
    }

    suspend fun decrementCart(productId: Long) {
        val existing = cartDao.getCartItem(productId) ?: return
        if (existing.quantity <= 1) {
            cartDao.deleteCartItem(productId)
        } else {
            cartDao.upsertCartItem(existing.copy(quantity = existing.quantity - 1))
        }
    }

    suspend fun removeFromCart(productId: Long) {
        cartDao.deleteCartItem(productId)
    }

    suspend fun placeOrder(
        customerName: String,
        customerPhone: String,
        deliveryAddress: String,
        notes: String
    ): PlaceOrderResult {
        return db.withTransaction {
            val cartItems = cartDao.observeCartItems().first()
            if (cartItems.isEmpty()) return@withTransaction PlaceOrderResult.EmptyCart

            var stockAdjusted = false
            val validLines = mutableListOf<Pair<ProductEntity, Int>>()

            for (cartItem in cartItems) {
                val product = productDao.getProductById(cartItem.productId)
                if (product == null || product.stock <= 0) {
                    cartDao.deleteCartItem(cartItem.productId)
                    stockAdjusted = true
                } else if (cartItem.quantity > product.stock) {
                    cartDao.upsertCartItem(cartItem.copy(quantity = product.stock))
                    stockAdjusted = true
                } else {
                    validLines.add(product to cartItem.quantity)
                }
            }

            if (stockAdjusted) return@withTransaction PlaceOrderResult.StockAdjusted
            if (validLines.isEmpty()) return@withTransaction PlaceOrderResult.EmptyCart

            val subtotal = validLines.sumOf { (prod, qty) -> prod.priceInr * qty }
            val deliveryFee = if (subtotal >= 299) 0 else 25
            val total = subtotal + deliveryFee
            val existingMax = orderDao.getAllOrderCodes()
                .mapNotNull { code -> code.removePrefix("GK-").toIntOrNull() }
                .maxOrNull() ?: 1000
            val nextNumber = (existingMax + 1).coerceAtLeast(1001)
            val orderCode = "GK-$nextNumber"

            val orderId = orderDao.insertOrder(
                OrderEntity(
                    orderCode = orderCode,
                    customerName = customerName.trim(),
                    customerPhone = customerPhone.trim(),
                    deliveryAddress = deliveryAddress.trim(),
                    paymentMethod = "Cash on Delivery (Demo)",
                    subtotalInr = subtotal,
                    deliveryFeeInr = deliveryFee,
                    totalInr = total,
                    status = OrderStatus.PENDING.id,
                    createdAtMillis = System.currentTimeMillis(),
                    notes = notes.trim()
                )
            )

            val orderItems = validLines.map { (prod, qty) ->
                OrderItemEntity(
                    orderId = orderId,
                    productId = prod.id,
                    productNameEn = prod.nameEn,
                    productNameHi = prod.nameHi,
                    categoryId = prod.category,
                    unitPriceInr = prod.priceInr,
                    quantity = qty,
                    unitEn = prod.unitEn,
                    unitHi = prod.unitHi,
                    imageKey = prod.imageKey
                )
            }
            orderDao.insertOrderItems(orderItems)

            // Decrement stock
            validLines.forEach { (prod, qty) ->
                productDao.adjustStock(prod.id, -qty)
            }

            cartDao.clearCart()
            prefs.updateCustomerProfile(
                CustomerProfile(
                    name = customerName.trim(),
                    phone = customerPhone.trim(),
                    address = deliveryAddress.trim()
                )
            )
            PlaceOrderResult.Success(orderCode = orderCode, orderId = orderId)
        }
    }

    suspend fun saveProduct(product: ProductEntity) {
        if (product.id == 0L) {
            productDao.insertProduct(product)
        } else {
            productDao.updateProduct(product)
        }
    }

    suspend fun deleteProduct(productId: Long) {
        db.withTransaction {
            cartDao.deleteCartItem(productId)
            productDao.deleteProductById(productId)
        }
    }

    suspend fun adjustProductStock(productId: Long, delta: Int) {
        productDao.adjustStock(productId, delta)
    }

    suspend fun updateOrderStatus(orderId: Long, newStatus: OrderStatus) {
        db.withTransaction {
            val existing = orderDao.getOrderWithItems(orderId) ?: return@withTransaction
            val oldStatus = OrderStatus.fromId(existing.order.status)
            if (oldStatus == newStatus) return@withTransaction

            // Restore stock if order moves to CANCELLED from a non-cancelled state
            if (oldStatus != OrderStatus.CANCELLED && newStatus == OrderStatus.CANCELLED) {
                existing.items.forEach { item ->
                    productDao.adjustStock(item.productId, item.quantity)
                }
            }
            // Deduct stock if order is re-activated from CANCELLED
            if (oldStatus == OrderStatus.CANCELLED && newStatus != OrderStatus.CANCELLED) {
                existing.items.forEach { item ->
                    productDao.adjustStock(item.productId, -item.quantity)
                }
            }

            orderDao.updateOrderStatus(orderId, newStatus.id)
        }
    }

    suspend fun setLanguage(language: AppLanguage) {
        prefs.setLanguage(language)
    }

    suspend fun updateCustomerProfile(profile: CustomerProfile) {
        prefs.updateCustomerProfile(profile)
    }

    suspend fun setAdminLoggedIn(loggedIn: Boolean) {
        prefs.setAdminLoggedIn(loggedIn)
    }
}
