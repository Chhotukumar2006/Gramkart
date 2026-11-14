package com.example.data.local

import com.example.data.model.OrderStatus
import com.example.data.model.ProductCategory

object DemoSeedData {

    fun sampleProducts(): List<ProductEntity> = listOf(
        // GROCERY
        ProductEntity(
            nameEn = "Royal Basmati Rice",
            nameHi = "रॉयल बासमती चावल",
            category = ProductCategory.GROCERY.id,
            priceInr = 128,
            mrpInr = 150,
            unitEn = "1 kg",
            unitHi = "1 किग्रा",
            stock = 24,
            imageKey = "rice",
            descriptionEn = "Aged extra-long grain aromatic basmati rice.",
            descriptionHi = "सुगंधित लंबे दाने वाला बासमती चावल।"
        ),
        ProductEntity(
            nameEn = "Whole Wheat Chakki Atta",
            nameHi = "चक्की फ्रेश आटा",
            category = ProductCategory.GROCERY.id,
            priceInr = 245,
            mrpInr = 275,
            unitEn = "5 kg",
            unitHi = "5 किग्रा",
            stock = 16,
            imageKey = "atta",
            descriptionEn = "100% MP Sharbati whole wheat flour for soft rotis.",
            descriptionHi = "मुलायम रोटियों के लिए शुद्ध गेहूँ का आटा।"
        ),
        ProductEntity(
            nameEn = "Unpolished Toor Dal",
            nameHi = "अरहर / तूर दाल",
            category = ProductCategory.GROCERY.id,
            priceInr = 165,
            mrpInr = 185,
            unitEn = "1 kg",
            unitHi = "1 किग्रा",
            stock = 18,
            imageKey = "dal",
            descriptionEn = "Protein-rich farm fresh yellow pigeon peas.",
            descriptionHi = "प्रोटीन से भरपूर शुद्ध अरहर दाल।"
        ),
        ProductEntity(
            nameEn = "Kachi Ghani Mustard Oil",
            nameHi = "कच्ची घानी सरसों तेल",
            category = ProductCategory.GROCERY.id,
            priceInr = 158,
            mrpInr = 175,
            unitEn = "1 L",
            unitHi = "1 लीटर",
            stock = 14,
            imageKey = "oil",
            descriptionEn = "Cold-pressed pure mustard oil for authentic flavour.",
            descriptionHi = "शुद्ध कोल्ड-प्रेस्ड सरसों का तेल।"
        ),

        // VEGETABLES
        ProductEntity(
            nameEn = "Fresh Desi Tomatoes",
            nameHi = "ताज़ा देसी टमाटर",
            category = ProductCategory.VEGETABLES.id,
            priceInr = 38,
            mrpInr = 48,
            unitEn = "1 kg",
            unitHi = "1 किग्रा",
            stock = 30,
            imageKey = "tomato",
            descriptionEn = "Juicy red farm-picked tomatoes.",
            descriptionHi = "खेत से ताज़े लाल रसीले टमाटर।"
        ),
        ProductEntity(
            nameEn = "Organic Potatoes (Aloo)",
            nameHi = "ताज़ा आलू",
            category = ProductCategory.VEGETABLES.id,
            priceInr = 32,
            mrpInr = 40,
            unitEn = "1 kg",
            unitHi = "1 किग्रा",
            stock = 40,
            imageKey = "potato",
            descriptionEn = "Clean, fresh harvest everyday potatoes.",
            descriptionHi = "रोज़मर्रा के उपयोग के लिए ताज़ा आलू।"
        ),
        ProductEntity(
            nameEn = "Red Nashik Onions",
            nameHi = "लाल प्याज़",
            category = ProductCategory.VEGETABLES.id,
            priceInr = 44,
            mrpInr = 54,
            unitEn = "1 kg",
            unitHi = "1 किग्रा",
            stock = 28,
            imageKey = "onion",
            descriptionEn = "Crisp, pungent red onions.",
            descriptionHi = "ताज़ा और कड़क लाल प्याज़।"
        ),
        ProductEntity(
            nameEn = "Fresh Green Spinach",
            nameHi = "ताज़ा हरा पालक",
            category = ProductCategory.VEGETABLES.id,
            priceInr = 22,
            mrpInr = 28,
            unitEn = "250 g",
            unitHi = "250 ग्राम",
            stock = 12,
            imageKey = "spinach",
            descriptionEn = "Tender iron-rich green spinach leaves.",
            descriptionHi = "आयरन से भरपूर ताज़ा हरा पालक।"
        ),

        // FRUITS
        ProductEntity(
            nameEn = "Sweet Kesar Mangoes",
            nameHi = "मीठे केसर आम",
            category = ProductCategory.FRUITS.id,
            priceInr = 145,
            mrpInr = 175,
            unitEn = "1 kg",
            unitHi = "1 किग्रा",
            stock = 15,
            imageKey = "mango",
            descriptionEn = "Naturally ripened sweet golden mangoes.",
            descriptionHi = "प्राकृतिक रूप से पके मीठे केसर आम।"
        ),
        ProductEntity(
            nameEn = "Kashmir Red Apples",
            nameHi = "कश्मीरी लाल सेब",
            category = ProductCategory.FRUITS.id,
            priceInr = 160,
            mrpInr = 190,
            unitEn = "1 kg",
            unitHi = "1 किग्रा",
            stock = 14,
            imageKey = "apple",
            descriptionEn = "Crunchy and sweet Himalayan red apples.",
            descriptionHi = "कुरकुरे और मीठे कश्मीरी सेब।"
        ),
        ProductEntity(
            nameEn = "Fresh Robusta Bananas",
            nameHi = "ताज़े केले",
            category = ProductCategory.FRUITS.id,
            priceInr = 36,
            mrpInr = 45,
            unitEn = "6 pcs",
            unitHi = "6 नग",
            stock = 22,
            imageKey = "banana",
            descriptionEn = "Naturally sweet energy-packed bananas.",
            descriptionHi = "ऊर्जा से भरपूर ताज़े मीठे केले।"
        ),

        // DAIRY
        ProductEntity(
            nameEn = "Farm Fresh Toned Milk",
            nameHi = "ताज़ा टोंड दूध",
            category = ProductCategory.DAIRY.id,
            priceInr = 54,
            mrpInr = 58,
            unitEn = "1 L",
            unitHi = "1 लीटर",
            stock = 25,
            imageKey = "milk",
            descriptionEn = "Pasteurized pure cow & buffalo milk pouch.",
            descriptionHi = "शुद्ध पाश्चुरीकृत ताज़ा दूध।"
        ),
        ProductEntity(
            nameEn = "Soft Malai Paneer",
            nameHi = "ताज़ा मलाई पनीर",
            category = ProductCategory.DAIRY.id,
            priceInr = 88,
            mrpInr = 98,
            unitEn = "200 g",
            unitHi = "200 ग्राम",
            stock = 4, // Low stock sample for Admin dashboard
            imageKey = "paneer",
            descriptionEn = "Rich, creamy and soft cottage cheese.",
            descriptionHi = "मुलायम और मलाईदार ताज़ा पनीर।"
        ),
        ProductEntity(
            nameEn = "Thick Homestead Curd",
            nameHi = "गाढ़ा ताज़ा दही",
            category = ProductCategory.DAIRY.id,
            priceInr = 38,
            mrpInr = 42,
            unitEn = "400 g",
            unitHi = "400 ग्राम",
            stock = 18,
            imageKey = "curd",
            descriptionEn = "Creamy probiotic dahi cup.",
            descriptionHi = "ताज़ा और मलाईदार घर जैसा दही।"
        ),

        // MEDICAL
        ProductEntity(
            nameEn = "Paracetamol 500mg",
            nameHi = "पैरासिटामोल 500mg",
            category = ProductCategory.MEDICAL.id,
            priceInr = 28,
            mrpInr = 32,
            unitEn = "15 tabs",
            unitHi = "15 गोलियाँ",
            stock = 35,
            imageKey = "medicine",
            descriptionEn = "Fever & mild pain relief tablet strip (Demo).",
            descriptionHi = "बुखार और दर्द से राहत की गोलियाँ (डेमो)।"
        ),
        ProductEntity(
            nameEn = "First-Aid Bandage Pack",
            nameHi = "फर्स्ट-एड बैंडेज पैक",
            category = ProductCategory.MEDICAL.id,
            priceInr = 45,
            mrpInr = 55,
            unitEn = "20 strips",
            unitHi = "20 पट्टियाँ",
            stock = 20,
            imageKey = "bandage",
            descriptionEn = "Waterproof antiseptic adhesive strips.",
            descriptionHi = "वाटरप्रूफ एंटीसेप्टिक पट्टियाँ।"
        ),
        ProductEntity(
            nameEn = "Herbal Hand Sanitizer",
            nameHi = "हर्बल हैंड सैनिटाइज़र",
            category = ProductCategory.MEDICAL.id,
            priceInr = 79,
            mrpInr = 95,
            unitEn = "200 ml",
            unitHi = "200 मिली",
            stock = 3, // Low stock sample
            imageKey = "sanitizer",
            descriptionEn = "70% alcohol neem & aloe hygiene gel.",
            descriptionHi = "नीम और एलोवेरा युक्त हैंड सैनिटाइज़र।"
        ),

        // HOUSEHOLD
        ProductEntity(
            nameEn = "Active Wash Detergent",
            nameHi = "एक्टिव वॉश डिटर्जेंट",
            category = ProductCategory.HOUSEHOLD.id,
            priceInr = 132,
            mrpInr = 150,
            unitEn = "1 kg",
            unitHi = "1 किग्रा",
            stock = 15,
            imageKey = "detergent",
            descriptionEn = "Tough stain removal washing powder.",
            descriptionHi = "कपड़ों के जिद्दी दाग हटाने वाला पाउडर।"
        ),
        ProductEntity(
            nameEn = "Lemon Dishwash Gel",
            nameHi = "नींबू डिशवॉश जेल",
            category = ProductCategory.HOUSEHOLD.id,
            priceInr = 104,
            mrpInr = 118,
            unitEn = "500 ml",
            unitHi = "500 मिली",
            stock = 19,
            imageKey = "dishwash",
            descriptionEn = "Concentrated grease-cutting utensil cleaner.",
            descriptionHi = "बर्तनों की चिकनाई साफ करने वाला जेल।"
        ),
        ProductEntity(
            nameEn = "Neem & Tulsi Bath Soap",
            nameHi = "नीम-तुलसी नहाने का साबुन",
            category = ProductCategory.HOUSEHOLD.id,
            priceInr = 90,
            mrpInr = 105,
            unitEn = "3 x 100g",
            unitHi = "3 x 100ग्राम",
            stock = 21,
            imageKey = "soap",
            descriptionEn = "Natural antibacterial bathing bar multipack.",
            descriptionHi = "नीम और तुलसी के गुणों वाला साबुन पैक।"
        )
    )

    data class SeedOrderSpec(
        val order: OrderEntity,
        val productIndices: List<Pair<Int, Int>> // Pair(index into sampleProducts, qty)
    )

    fun sampleOrders(nowMillis: Long = System.currentTimeMillis()): List<SeedOrderSpec> {
        val products = sampleProducts()
        return listOf(
            SeedOrderSpec(
                order = OrderEntity(
                    orderCode = "GK-1001",
                    customerName = "Priya Verma",
                    customerPhone = "9811223344",
                    deliveryAddress = "House 8, Mandir Marg, Rampur Block, Jaipur",
                    paymentMethod = "Cash on Delivery (Demo)",
                    subtotalInr = 342,
                    deliveryFeeInr = 0,
                    totalInr = 342,
                    status = OrderStatus.DELIVERED.id,
                    createdAtMillis = nowMillis - 1000L * 60 * 60 * 18,
                    notes = "Ring bell twice"
                ),
                productIndices = listOf(0 to 1, 4 to 2, 11 to 2, 14 to 1)
            ),
            SeedOrderSpec(
                order = OrderEntity(
                    orderCode = "GK-1002",
                    customerName = "Rohan Patidar",
                    customerPhone = "9765432109",
                    deliveryAddress = "Plot 22, Kisan Colony, Near Primary School",
                    paymentMethod = "Cash on Delivery (Demo)",
                    subtotalInr = 398,
                    deliveryFeeInr = 0,
                    totalInr = 398,
                    status = OrderStatus.CONFIRMED.id,
                    createdAtMillis = nowMillis - 1000L * 60 * 45,
                    notes = ""
                ),
                productIndices = listOf(1 to 1, 8 to 1, 17 to 1) // Wait: 245 + 145 + 132 = 522 -> let's compute exact!
            ),
            SeedOrderSpec(
                order = OrderEntity(
                    orderCode = "GK-1003",
                    customerName = "Sunita Devi",
                    customerPhone = "9922334455",
                    deliveryAddress = "Ward 5, Green Park Lane, Gramin Bazar",
                    paymentMethod = "Cash on Delivery (Demo)",
                    subtotalInr = 250,
                    deliveryFeeInr = 25,
                    totalInr = 275,
                    status = OrderStatus.PENDING.id,
                    createdAtMillis = nowMillis - 1000L * 60 * 10,
                    notes = "Call before arriving"
                ),
                productIndices = listOf(4 to 2, 5 to 1, 11 to 1, 12 to 1)
            )
        ).map { spec ->
            // Guarantee exact math from the referenced products
            val subtotal = spec.productIndices.sumOf { (idx, qty) ->
                products[idx].priceInr * qty
            }
            val delivery = if (subtotal >= 299) 0 else 25
            spec.copy(
                order = spec.order.copy(
                    subtotalInr = subtotal,
                    deliveryFeeInr = delivery,
                    totalInr = subtotal + delivery
                )
            )
        }
    }
}
