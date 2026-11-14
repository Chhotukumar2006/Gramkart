package com.example.ui.customer

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.DeleteOutline
import androidx.compose.material.icons.rounded.LocalShipping
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Payments
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.data.model.CartSummary
import com.example.data.model.CustomerProfile
import com.example.ui.components.DemoNoticeStrip
import com.example.ui.components.ProductArtwork
import com.example.ui.components.QuantityStepper
import com.example.ui.components.SubtleDivider
import com.example.ui.i18n.AppLanguage
import com.example.ui.i18n.AppStrings
import com.example.ui.theme.GramBorder
import com.example.ui.theme.GramError
import com.example.ui.theme.GramGreenContainer
import com.example.ui.theme.GramGreenDark
import com.example.ui.theme.GramGreenPrimary
import com.example.ui.theme.GramGreenSoft
import com.example.ui.theme.GramOrangeAccent
import com.example.ui.theme.GramOrangeContainer
import com.example.ui.theme.GramOrangeDark
import com.example.ui.theme.GramTextSecondary
import com.example.ui.theme.GramWhite

@Composable
fun CustomerCartScreen(
    cartSummary: CartSummary,
    language: AppLanguage,
    strings: AppStrings,
    onAddToCart: (Long) -> Unit,
    onDecrementCart: (Long) -> Unit,
    onRemoveFromCart: (Long) -> Unit,
    onBrowseGroceries: () -> Unit,
    onProceedToCheckout: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        // Screen Header
        Surface(color = GramWhite, shadowElevation = 1.dp) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = strings.myCartTitle,
                    style = MaterialTheme.typography.headlineSmall,
                    color = GramGreenDark
                )
                if (cartSummary.totalCount > 0) {
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = GramGreenContainer
                    ) {
                        Text(
                            text = strings.itemsCount(cartSummary.totalCount),
                            style = MaterialTheme.typography.labelMedium,
                            color = GramGreenDark,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }

        if (cartSummary.items.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    modifier = Modifier.widthIn(max = 420.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_empty_cart),
                        contentDescription = strings.emptyCartTitle,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .size(190.dp)
                            .clip(RoundedCornerShape(24.dp))
                    )
                    Text(
                        text = strings.emptyCartTitle,
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = strings.emptyCartSubtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = GramTextSecondary,
                        textAlign = TextAlign.Center
                    )
                    Button(
                        onClick = onBrowseGroceries,
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = GramGreenPrimary),
                        modifier = Modifier
                            .padding(top = 6.dp)
                            .height(48.dp)
                            .testTag("browse_groceries_button")
                    ) {
                        Text(strings.browseGroceries, fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                // Free delivery meter
                item {
                    val progress = (cartSummary.subtotalInr.toFloat() / cartSummary.freeDeliveryThresholdInr).coerceIn(0f, 1f)
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = if (cartSummary.amountForFreeDelivery == 0) GramGreenContainer else GramOrangeContainer.copy(alpha = 0.65f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Rounded.LocalShipping,
                                    contentDescription = null,
                                    tint = if (cartSummary.amountForFreeDelivery == 0) GramGreenDark else GramOrangeDark,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (cartSummary.amountForFreeDelivery == 0) {
                                        strings.freeDeliveryUnlocked
                                    } else {
                                        strings.addMoreForFreeDelivery(cartSummary.amountForFreeDelivery)
                                    },
                                    style = MaterialTheme.typography.labelLarge,
                                    color = if (cartSummary.amountForFreeDelivery == 0) GramGreenDark else GramOrangeDark
                                )
                            }
                            LinearProgressIndicator(
                                progress = { progress },
                                color = if (cartSummary.amountForFreeDelivery == 0) GramGreenPrimary else GramOrangeAccent,
                                trackColor = GramWhite,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(50))
                            )
                        }
                    }
                }

                // Cart Items
                items(cartSummary.items, key = { it.productId }) { item ->
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = GramWhite),
                        border = BorderStroke(1.dp, GramBorder)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            ProductArtwork(
                                imageKey = item.imageKey,
                                categoryId = item.categoryId,
                                contentDescription = item.displayName(language),
                                modifier = Modifier.size(64.dp)
                            )
                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(3.dp)
                            ) {
                                Text(
                                    text = item.displayName(language),
                                    style = MaterialTheme.typography.titleSmall,
                                    maxLines = 2
                                )
                                Text(
                                    text = "${item.displayUnit(language)} • ₹${item.priceInr}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = GramTextSecondary
                                )
                                Text(
                                    text = "₹${item.lineTotalInr}",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = GramGreenDark,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                            Column(
                                horizontalAlignment = Alignment.End,
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                IconButton(
                                    onClick = { onRemoveFromCart(item.productId) },
                                    modifier = Modifier
                                        .size(30.dp)
                                        .testTag("remove_cart_${item.productId}")
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.DeleteOutline,
                                        contentDescription = "Remove item",
                                        tint = GramError,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                QuantityStepper(
                                    quantity = item.quantity,
                                    canIncrement = item.quantity < item.stock,
                                    onDecrement = { onDecrementCart(item.productId) },
                                    onIncrement = { onAddToCart(item.productId) }
                                )
                            }
                        }
                    }
                }

                // Bill Summary Card
                item {
                    Card(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = GramWhite),
                        border = BorderStroke(1.dp, GramBorder)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                text = strings.billSummary,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(strings.itemSubtotal, color = GramTextSecondary)
                                Text("₹${cartSummary.subtotalInr}", fontWeight = FontWeight.SemiBold)
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(strings.deliveryFee, color = GramTextSecondary)
                                Text(
                                    text = if (cartSummary.deliveryFeeInr == 0) strings.freeLabel else "₹${cartSummary.deliveryFeeInr}",
                                    color = if (cartSummary.deliveryFeeInr == 0) GramGreenPrimary else MaterialTheme.colorScheme.onSurface,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            SubtleDivider(modifier = Modifier.fillMaxWidth().height(1.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = strings.grandTotal,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = "₹${cartSummary.grandTotalInr}",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = GramGreenDark,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                        }
                    }
                }
            }

            // Bottom Checkout CTA
            Surface(
                color = GramWhite,
                shadowElevation = 8.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = onProceedToCheckout,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GramGreenPrimary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .height(52.dp)
                        .testTag("proceed_to_checkout_button")
                ) {
                    Text(
                        text = "${strings.proceedToCheckout} • ₹${cartSummary.grandTotalInr}",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun CustomerCheckoutScreen(
    cartSummary: CartSummary,
    profile: CustomerProfile,
    language: AppLanguage,
    strings: AppStrings,
    onBack: () -> Unit,
    onPlaceOrder: (name: String, phone: String, address: String, notes: String) -> Unit
) {
    BackHandler(onBack = onBack)

    var name by remember(profile.name) { mutableStateOf(profile.name) }
    var phone by remember(profile.phone) { mutableStateOf(profile.phone) }
    var address by remember(profile.address) { mutableStateOf(profile.address) }
    var notes by remember { mutableStateOf("") }

    var nameError by remember { mutableStateOf<String?>(null) }
    var phoneError by remember { mutableStateOf<String?>(null) }
    var addressError by remember { mutableStateOf<String?>(null) }

    Column(modifier = Modifier.fillMaxSize()) {
        Surface(color = GramWhite, shadowElevation = 2.dp) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack, modifier = Modifier.testTag("checkout_back_button")) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = "Back"
                    )
                }
                Text(
                    text = strings.checkoutTitle,
                    style = MaterialTheme.typography.titleLarge,
                    color = GramGreenDark
                )
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            DemoNoticeStrip(text = strings.demoBannerNotice, modifier = Modifier.fillMaxWidth())

            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = GramWhite),
                border = BorderStroke(1.dp, GramBorder)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = strings.deliveryDetails,
                        style = MaterialTheme.typography.titleMedium
                    )

                    OutlinedTextField(
                        value = name,
                        onValueChange = {
                            name = it
                            nameError = null
                        },
                        label = { Text(strings.fullName) },
                        leadingIcon = { Icon(Icons.Rounded.Person, contentDescription = null, tint = GramGreenPrimary) },
                        isError = nameError != null,
                        supportingText = nameError?.let { { Text(it) } },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("checkout_name_input")
                    )

                    OutlinedTextField(
                        value = phone,
                        onValueChange = { input ->
                            phone = input.filter { it.isDigit() }.take(10)
                            phoneError = null
                        },
                        label = { Text(strings.mobileNumber) },
                        leadingIcon = { Icon(Icons.Rounded.Phone, contentDescription = null, tint = GramGreenPrimary) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        isError = phoneError != null,
                        supportingText = phoneError?.let { { Text(it) } },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("checkout_phone_input")
                    )

                    OutlinedTextField(
                        value = address,
                        onValueChange = {
                            address = it
                            addressError = null
                        },
                        label = { Text(strings.deliveryAddress) },
                        leadingIcon = { Icon(Icons.Rounded.LocationOn, contentDescription = null, tint = GramGreenPrimary) },
                        isError = addressError != null,
                        supportingText = addressError?.let { { Text(it) } },
                        minLines = 2,
                        maxLines = 4,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("checkout_address_input")
                    )

                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        label = { Text(strings.orderNotesOptional) },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // Cash on Delivery Demo Card
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = GramGreenSoft),
                border = BorderStroke(1.5.dp, GramGreenPrimary)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    RadioButton(
                        selected = true,
                        onClick = {},
                        colors = RadioButtonDefaults.colors(selectedColor = GramGreenPrimary)
                    )
                    Icon(
                        imageVector = Icons.Rounded.Payments,
                        contentDescription = null,
                        tint = GramGreenPrimary,
                        modifier = Modifier.size(26.dp)
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = strings.codDemoTitle,
                            style = MaterialTheme.typography.titleSmall,
                            color = GramGreenDark
                        )
                        Text(
                            text = strings.codDemoSubtitle,
                            style = MaterialTheme.typography.bodySmall,
                            color = GramTextSecondary
                        )
                    }
                }
            }

            // Compact Items Summary
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = GramWhite),
                border = BorderStroke(1.dp, GramBorder)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "${strings.billSummary} (${strings.itemsCount(cartSummary.totalCount)})",
                        style = MaterialTheme.typography.titleSmall
                    )
                    cartSummary.items.forEach { item ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "${item.quantity}x ${item.displayName(language)}",
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = "₹${item.lineTotalInr}",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                    SubtleDivider(modifier = Modifier.fillMaxWidth().height(1.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(strings.deliveryFee, style = MaterialTheme.typography.bodySmall, color = GramTextSecondary)
                        Text(
                            text = if (cartSummary.deliveryFeeInr == 0) strings.freeLabel else "₹${cartSummary.deliveryFeeInr}",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            color = GramGreenPrimary
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(strings.grandTotal, style = MaterialTheme.typography.titleMedium)
                        Text(
                            text = "₹${cartSummary.grandTotalInr}",
                            style = MaterialTheme.typography.titleLarge,
                            color = GramGreenDark,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }
        }

        Surface(color = GramWhite, shadowElevation = 8.dp) {
            Button(
                onClick = {
                    val validName = name.trim().isNotEmpty()
                    val validPhone = phone.trim().length == 10
                    val validAddr = address.trim().length >= 5
                    nameError = if (validName) null else strings.errNameRequired
                    phoneError = if (validPhone) null else strings.errPhoneInvalid
                    addressError = if (validAddr) null else strings.errAddressRequired
                    if (validName && validPhone && validAddr) {
                        onPlaceOrder(name, phone, address, notes)
                    }
                },
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = GramOrangeAccent),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .height(54.dp)
                    .testTag("place_order_button")
            ) {
                Icon(Icons.Rounded.CheckCircle, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = strings.placeOrderBtn(cartSummary.grandTotalInr),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }
    }
}

@Composable
fun OrderSuccessScreen(
    orderCode: String,
    strings: AppStrings,
    onTrackOrder: () -> Unit,
    onContinueShopping: () -> Unit
) {
    BackHandler(onBack = onContinueShopping)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GramWhite)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.widthIn(max = 420.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_order_success),
                contentDescription = strings.orderPlacedTitle,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(200.dp)
                    .clip(RoundedCornerShape(24.dp))
            )
            Surface(
                shape = RoundedCornerShape(50),
                color = GramGreenContainer
            ) {
                Text(
                    text = orderCode,
                    style = MaterialTheme.typography.labelLarge,
                    color = GramGreenDark,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                )
            }
            Text(
                text = strings.orderPlacedTitle,
                style = MaterialTheme.typography.headlineMedium,
                color = GramGreenDark,
                textAlign = TextAlign.Center
            )
            Text(
                text = strings.orderPlacedSubtitle(orderCode),
                style = MaterialTheme.typography.bodyMedium,
                color = GramTextSecondary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(6.dp))
            Button(
                onClick = onTrackOrder,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = GramGreenPrimary),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("track_order_button")
            ) {
                Text(strings.trackMyOrder, fontWeight = FontWeight.Bold)
            }
            OutlinedButton(
                onClick = onContinueShopping,
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, GramGreenPrimary),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("continue_shopping_button")
            ) {
                Text(strings.continueShopping, color = GramGreenPrimary, fontWeight = FontWeight.Bold)
            }
        }
    }
}
