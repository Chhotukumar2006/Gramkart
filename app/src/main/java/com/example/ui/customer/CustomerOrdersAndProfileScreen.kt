package com.example.ui.customer

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AdminPanelSettings
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Payments
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.material.icons.rounded.ReceiptLong
import androidx.compose.material.icons.rounded.Translate
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.data.local.OrderWithItems
import com.example.data.model.CustomerProfile
import com.example.data.model.OrderStatus
import com.example.ui.components.OrderStatusChip
import com.example.ui.components.SubtleDivider
import com.example.ui.components.formatTimestamp
import com.example.ui.i18n.AppLanguage
import com.example.ui.i18n.AppStrings
import com.example.ui.theme.GramBorder
import com.example.ui.theme.GramError
import com.example.ui.theme.GramErrorContainer
import com.example.ui.theme.GramGreenContainer
import com.example.ui.theme.GramGreenDark
import com.example.ui.theme.GramGreenPrimary
import com.example.ui.theme.GramOrangeAccent
import com.example.ui.theme.GramOrangeContainer
import com.example.ui.theme.GramOrangeDark
import com.example.ui.theme.GramTextMuted
import com.example.ui.theme.GramTextSecondary
import com.example.ui.theme.GramWhite

@Composable
fun CustomerOrdersScreen(
    orders: List<OrderWithItems>,
    language: AppLanguage,
    strings: AppStrings,
    onBrowseGroceries: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Surface(color = GramWhite, shadowElevation = 1.dp) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = strings.myOrdersTitle,
                    style = MaterialTheme.typography.headlineSmall,
                    color = GramGreenDark
                )
                Surface(
                    shape = RoundedCornerShape(50),
                    color = GramGreenContainer
                ) {
                    Text(
                        text = orders.size.toString(),
                        style = MaterialTheme.typography.labelMedium,
                        color = GramGreenDark,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }
        }

        if (orders.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.ReceiptLong,
                        contentDescription = null,
                        tint = GramGreenPrimary,
                        modifier = Modifier.size(64.dp)
                    )
                    Text(strings.noOrdersTitle, style = MaterialTheme.typography.titleLarge)
                    Text(
                        text = strings.noOrdersSubtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = GramTextSecondary,
                        textAlign = TextAlign.Center
                    )
                    Button(
                        onClick = onBrowseGroceries,
                        colors = ButtonDefaults.buttonColors(containerColor = GramGreenPrimary)
                    ) {
                        Text(strings.browseGroceries)
                    }
                }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(orders, key = { it.order.id }) { orderWithItems ->
                    CustomerOrderCard(
                        orderWithItems = orderWithItems,
                        language = language,
                        strings = strings
                    )
                }
            }
        }
    }
}

@Composable
private fun CustomerOrderCard(
    orderWithItems: OrderWithItems,
    language: AppLanguage,
    strings: AppStrings
) {
    val order = orderWithItems.order
    val status = OrderStatus.fromId(order.status)

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = GramWhite),
        border = BorderStroke(1.dp, GramBorder),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("customer_order_${order.orderCode}")
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = order.orderCode,
                        style = MaterialTheme.typography.titleMedium,
                        color = GramGreenDark,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = formatTimestamp(order.createdAtMillis),
                        style = MaterialTheme.typography.bodySmall,
                        color = GramTextMuted
                    )
                }
                OrderStatusChip(status = status, language = language)
            }

            // Live Status Stepper
            OrderProgressStepper(status = status, language = language)

            SubtleDivider(modifier = Modifier.fillMaxWidth().height(1.dp))

            // Items
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                orderWithItems.items.forEach { item ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "${item.quantity}x ${item.displayName(language)} (${item.unitEn})",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "₹${item.unitPriceInr * item.quantity}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            SubtleDivider(modifier = Modifier.fillMaxWidth().height(1.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "${strings.deliverTo}: ${order.customerName} (${order.customerPhone})",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = order.deliveryAddress,
                        style = MaterialTheme.typography.bodySmall,
                        color = GramTextSecondary
                    )
                    if (order.notes.isNotBlank()) {
                        Text(
                            text = "${strings.orderNotesLabel}: ${order.notes}",
                            style = MaterialTheme.typography.bodySmall,
                            color = GramOrangeDark
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Payments,
                            contentDescription = null,
                            tint = GramGreenPrimary,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = strings.paymentCodLabel,
                            style = MaterialTheme.typography.labelSmall,
                            color = GramGreenPrimary
                        )
                    }
                }
                Text(
                    text = "₹${order.totalInr}",
                    style = MaterialTheme.typography.titleLarge,
                    color = GramGreenDark,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }
    }
}

@Composable
private fun OrderProgressStepper(
    status: OrderStatus,
    language: AppLanguage
) {
    if (status == OrderStatus.CANCELLED) {
        Surface(
            shape = RoundedCornerShape(10.dp),
            color = GramErrorContainer,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = status.label(language),
                style = MaterialTheme.typography.labelMedium,
                color = GramError,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
            )
        }
        return
    }

    val steps = listOf(
        OrderStatus.PENDING,
        OrderStatus.CONFIRMED,
        OrderStatus.PREPARING,
        OrderStatus.OUT_FOR_DELIVERY,
        OrderStatus.DELIVERED
    )
    val currentIdx = steps.indexOf(status).coerceAtLeast(0)

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        steps.forEachIndexed { idx, step ->
            val reached = idx <= currentIdx
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(if (reached) GramGreenPrimary else GramBorder)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = step.label(language),
                    style = MaterialTheme.typography.labelSmall,
                    color = if (reached) GramGreenDark else GramTextMuted,
                    fontWeight = if (idx == currentIdx) FontWeight.Bold else FontWeight.Normal,
                    textAlign = TextAlign.Center,
                    maxLines = 2
                )
            }
            if (idx < steps.lastIndex) {
                Box(
                    modifier = Modifier
                        .padding(top = 5.dp)
                        .width(8.dp)
                        .height(2.dp)
                        .background(if (idx < currentIdx) GramGreenPrimary else GramBorder)
                )
            }
        }
    }
}

@Composable
fun CustomerProfileScreen(
    profile: CustomerProfile,
    language: AppLanguage,
    strings: AppStrings,
    onSaveProfile: (CustomerProfile) -> Unit,
    onSelectLanguage: (AppLanguage) -> Unit,
    onOpenAdminPanel: () -> Unit
) {
    var name by remember(profile.name) { mutableStateOf(profile.name) }
    var phone by remember(profile.phone) { mutableStateOf(profile.phone) }
    var address by remember(profile.address) { mutableStateOf(profile.address) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Profile Header Card
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = GramGreenPrimary)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(GramWhite.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Person,
                        contentDescription = null,
                        tint = GramWhite,
                        modifier = Modifier.size(30.dp)
                    )
                }
                Column {
                    Text(
                        text = profile.name,
                        style = MaterialTheme.typography.titleLarge,
                        color = GramWhite,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${strings.demoCustomerLabel} • +91 ${profile.phone}",
                        style = MaterialTheme.typography.bodySmall,
                        color = GramWhite.copy(alpha = 0.88f)
                    )
                }
            }
        }

        // Language Card
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = GramWhite),
            border = BorderStroke(1.dp, GramBorder)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Rounded.Translate, contentDescription = null, tint = GramGreenPrimary)
                    Text(strings.languageSetting, style = MaterialTheme.typography.titleMedium)
                }
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    AppLanguage.entries.forEach { lang ->
                        FilterChip(
                            selected = language == lang,
                            onClick = { onSelectLanguage(lang) },
                            label = { Text(lang.fullLabel, fontWeight = FontWeight.SemiBold) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = GramGreenContainer,
                                selectedLabelColor = GramGreenDark
                            ),
                            modifier = Modifier.testTag("profile_lang_${lang.code.lowercase()}")
                        )
                    }
                }
            }
        }

        // Saved Delivery Details Form
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = GramWhite),
            border = BorderStroke(1.dp, GramBorder)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(strings.editDeliveryInfo, style = MaterialTheme.typography.titleMedium)

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(strings.fullName) },
                    leadingIcon = { Icon(Icons.Rounded.Person, contentDescription = null, tint = GramGreenPrimary) },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it.filter { c -> c.isDigit() }.take(10) },
                    label = { Text(strings.mobileNumber) },
                    leadingIcon = { Icon(Icons.Rounded.Phone, contentDescription = null, tint = GramGreenPrimary) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text(strings.deliveryAddress) },
                    leadingIcon = { Icon(Icons.Rounded.LocationOn, contentDescription = null, tint = GramGreenPrimary) },
                    minLines = 2,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                Button(
                    onClick = {
                        onSaveProfile(
                            CustomerProfile(
                                name = name.trim().ifEmpty { profile.name },
                                phone = phone.trim().ifEmpty { profile.phone },
                                address = address.trim().ifEmpty { profile.address }
                            )
                        )
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GramGreenPrimary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("save_profile_button")
                ) {
                    Text(strings.saveDetails, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Admin Portal Entry Card
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = GramOrangeContainer.copy(alpha = 0.65f)),
            border = BorderStroke(1.dp, GramOrangeAccent.copy(alpha = 0.35f))
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.AdminPanelSettings,
                        contentDescription = null,
                        tint = GramOrangeDark,
                        modifier = Modifier.size(28.dp)
                    )
                    Column {
                        Text(
                            text = strings.adminPortalTitle,
                            style = MaterialTheme.typography.titleMedium,
                            color = GramOrangeDark
                        )
                        Text(
                            text = strings.adminPortalSubtitle,
                            style = MaterialTheme.typography.bodySmall,
                            color = GramTextSecondary
                        )
                    }
                }
                Button(
                    onClick = onOpenAdminPanel,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GramOrangeAccent),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("profile_open_admin_button")
                ) {
                    Text(strings.openAdminPanel, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Demo Disclaimer Card
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = GramWhite,
            border = BorderStroke(1.dp, GramBorder)
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(Icons.Rounded.Info, contentDescription = null, tint = GramGreenPrimary)
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(strings.demoModeDisclaimerTitle, style = MaterialTheme.typography.titleSmall)
                    Text(
                        text = strings.demoModeDisclaimerBody,
                        style = MaterialTheme.typography.bodySmall,
                        color = GramTextSecondary
                    )
                }
            }
        }
    }
}
