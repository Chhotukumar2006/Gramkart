package com.example.data.local

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.example.ui.i18n.AppLanguage

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val nameEn: String,
    val nameHi: String,
    val category: String,
    val priceInr: Int,
    val mrpInr: Int,
    val unitEn: String,
    val unitHi: String,
    val stock: Int,
    val imageKey: String,
    val descriptionEn: String = "",
    val descriptionHi: String = ""
) {
    fun displayName(language: AppLanguage): String =
        if (language == AppLanguage.HI && nameHi.isNotBlank()) nameHi else nameEn

    fun displayUnit(language: AppLanguage): String =
        if (language == AppLanguage.HI && unitHi.isNotBlank()) unitHi else unitEn

    val discountPercent: Int
        get() = if (mrpInr > priceInr && mrpInr > 0) {
            ((mrpInr - priceInr) * 100) / mrpInr
        } else 0
}

@Entity(tableName = "cart_items")
data class CartItemEntity(
    @PrimaryKey val productId: Long,
    val quantity: Int
)

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val orderCode: String,
    val customerName: String,
    val customerPhone: String,
    val deliveryAddress: String,
    val paymentMethod: String = "Cash on Delivery (Demo)",
    val subtotalInr: Int,
    val deliveryFeeInr: Int,
    val totalInr: Int,
    val status: String,
    val createdAtMillis: Long,
    val notes: String = ""
)

@Entity(
    tableName = "order_items",
    foreignKeys = [
        ForeignKey(
            entity = OrderEntity::class,
            parentColumns = ["id"],
            childColumns = ["orderId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("orderId")]
)
data class OrderItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val orderId: Long,
    val productId: Long,
    val productNameEn: String,
    val productNameHi: String,
    val categoryId: String,
    val unitPriceInr: Int,
    val quantity: Int,
    val unitEn: String,
    val unitHi: String,
    val imageKey: String
) {
    fun displayName(language: AppLanguage): String =
        if (language == AppLanguage.HI && productNameHi.isNotBlank()) productNameHi else productNameEn
}

data class OrderWithItems(
    @Embedded val order: OrderEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "orderId"
    )
    val items: List<OrderItemEntity>
)
