package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.GramKartRepository
import com.example.data.PlaceOrderResult
import com.example.data.local.GramKartDatabase
import com.example.data.local.UserPreferencesRepository
import com.example.data.model.OrderStatus
import com.example.data.model.ProductCategory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class GramKartRepositoryTest {

    private lateinit var db: GramKartDatabase
    private lateinit var repository: GramKartRepository

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, GramKartDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        val prefs = UserPreferencesRepository(context)
        repository = GramKartRepository(db, prefs)
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun `customer cart order admin status and reset demo flow`() = runTest {
        repository.resetDemoData()

        val initialProducts = repository.productsFlow.first()
        assertEquals(20, initialProducts.size)
        assertEquals(6, initialProducts.map { ProductCategory.fromId(it.category) }.toSet().size)
        assertEquals(3, repository.ordersFlow.first().size)

        // Add tomato (price 38) -> delivery fee 25, total 63
        val tomato = initialProducts.first { it.imageKey == "tomato" }
        val initialStock = tomato.stock
        repository.addToCart(tomato.id)
        var summary = repository.cartSummaryFlow.first()
        assertEquals(1, summary.totalCount)
        assertEquals(tomato.priceInr, summary.subtotalInr)
        assertEquals(25, summary.deliveryFeeInr)
        assertEquals(tomato.priceInr + 25, summary.grandTotalInr)

        // Admin edits tomato price to 50 -> cart immediately reflects new price!
        repository.saveProduct(tomato.copy(priceInr = 50))
        summary = repository.cartSummaryFlow.first()
        assertEquals(50, summary.subtotalInr)
        assertEquals(75, summary.grandTotalInr)

        // Place COD demo order
        val result = repository.placeOrder(
            customerName = "Test Buyer",
            phone = "9988776655",
            address = "Plot 12, Gramin Road"
        )
        assertTrue(result is PlaceOrderResult.Success)
        val success = result as PlaceOrderResult.Success
        assertEquals("GK-1004", success.orderCode)
        val orderId = success.orderId

        // Cart cleared & stock decremented
        assertEquals(0, repository.cartSummaryFlow.first().totalCount)
        val updatedTomato = repository.productsFlow.first().first { it.id == tomato.id }
        assertEquals(initialStock - 1, updatedTomato.stock)

        // Admin views order & progresses status through Confirmed -> Preparing -> Cancelled
        val orders = repository.ordersFlow.first()
        assertEquals(4, orders.size)
        assertEquals(4, orders.map { it.order.orderCode }.toSet().size)

        repository.updateOrderStatus(orderId, OrderStatus.CONFIRMED)
        assertEquals(OrderStatus.CONFIRMED.id, repository.ordersFlow.first().first { it.order.id == orderId }.order.status)

        repository.updateOrderStatus(orderId, OrderStatus.PREPARING)
        assertEquals(OrderStatus.PREPARING.id, repository.ordersFlow.first().first { it.order.id == orderId }.order.status)

        repository.updateOrderStatus(orderId, OrderStatus.CANCELLED)
        val cancelledOrder = repository.ordersFlow.first().first { it.order.id == orderId }
        assertEquals(OrderStatus.CANCELLED.id, cancelledOrder.order.status)
        val restoredTomato = repository.productsFlow.first().first { it.id == tomato.id }
        assertEquals(initialStock, restoredTomato.stock)

        // Reset Demo Data restores original prices and order count
        repository.resetDemoData()
        assertEquals(3, repository.ordersFlow.first().size)
        val resetTomato = repository.productsFlow.first().first { it.imageKey == "tomato" }
        assertEquals(38, resetTomato.priceInr)
    }

    private suspend fun GramKartRepository.placeOrder(
        customerName: String,
        phone: String,
        address: String
    ): PlaceOrderResult = placeOrder(customerName, phone, address, "")
}
