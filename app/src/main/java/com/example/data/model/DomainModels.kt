package com.example.data.model

import androidx.compose.ui.graphics.Color
import com.example.ui.i18n.AppLanguage

enum class ProductCategory(
    val id: String,
    val labelEn: String,
    val labelHi: String,
    val tintColor: Color,
    val bgTint: Color
) {
    GROCERY("GROCERY", "Grocery", "किराना", Color(0xFFB45309), Color(0xFFFEF3C7)),
    VEGETABLES("VEGETABLES", "Vegetables", "सब्ज़ियाँ", Color(0xFF15803D), Color(0xFFDCFCE7)),
    FRUITS("FRUITS", "Fruits", "फल", Color(0xFFEA580C), Color(0xFFFFEDD5)),
    DAIRY("DAIRY", "Dairy", "डेयरी", Color(0xFF0284C7), Color(0xFFE0F2FE)),
    MEDICAL("MEDICAL", "Medical", "मेडिकल", Color(0xFFDC2626), Color(0xFFFEE2E2)),
    HOUSEHOLD("HOUSEHOLD", "Household", "घरेलू", Color(0xFF7C3AED), Color(0xFFEDE9FE));

    fun label(language: AppLanguage): String =
        if (language == AppLanguage.HI) labelHi else labelEn

    companion object {
        fun fromId(id: String): ProductCategory =
            entries.find { it.id == id } ?: GROCERY
    }
}

enum class OrderStatus(
    val id: String,
    val labelEn: String,
    val labelHi: String
) {
    PENDING("PENDING", "Pending", "लंबित"),
    CONFIRMED("CONFIRMED", "Confirmed", "कन्फर्म"),
    PREPARING("PREPARING", "Preparing", "तैयारी हो रही"),
    OUT_FOR_DELIVERY("OUT_FOR_DELIVERY", "Out for Delivery", "डिलीवरी के लिए निकला"),
    DELIVERED("DELIVERED", "Delivered", "डिलीवर हो गया"),
    CANCELLED("CANCELLED", "Cancelled", "रद्द");

    fun label(language: AppLanguage): String =
        if (language == AppLanguage.HI) labelHi else labelEn

    companion object {
        fun fromId(id: String): OrderStatus = when (id) {
            "ACCEPTED" -> CONFIRMED
            "REJECTED" -> CANCELLED
            else -> entries.find { it.id == id } ?: PENDING
        }
    }
}

data class CustomerProfile(
    val name: String = "Aarav Sharma",
    val phone: String = "9876543210",
    val address: String = "House 14, Shanti Nagar, Near Gram Panchayat, Jaipur 302006"
)

data class CartItemDetail(
    val productId: Long,
    val nameEn: String,
    val nameHi: String,
    val unitEn: String,
    val unitHi: String,
    val priceInr: Int,
    val mrpInr: Int,
    val stock: Int,
    val imageKey: String,
    val categoryId: String,
    val quantity: Int
) {
    val lineTotalInr: Int get() = priceInr * quantity
    fun displayName(language: AppLanguage): String =
        if (language == AppLanguage.HI && nameHi.isNotBlank()) nameHi else nameEn
    fun displayUnit(language: AppLanguage): String =
        if (language == AppLanguage.HI && unitHi.isNotBlank()) unitHi else unitEn
}

data class CartSummary(
    val items: List<CartItemDetail> = emptyList(),
    val totalCount: Int = 0,
    val subtotalInr: Int = 0,
    val deliveryFeeInr: Int = 0,
    val grandTotalInr: Int = 0,
    val freeDeliveryThresholdInr: Int = 299
) {
    val amountForFreeDelivery: Int
        get() = (freeDeliveryThresholdInr - subtotalInr).coerceAtLeast(0)
}

data class CategorySales(
    val category: ProductCategory,
    val revenueInr: Int,
    val unitsSold: Int
)

data class SalesAnalytics(
    val totalSalesInr: Int = 0,
    val totalOrders: Int = 0,
    val pendingOrders: Int = 0,
    val acceptedOrders: Int = 0,
    val deliveredOrders: Int = 0,
    val rejectedOrders: Int = 0,
    val lowStockCount: Int = 0,
    val totalProducts: Int = 0,
    val categorySales: List<CategorySales> = emptyList()
)
