package com.example.ui.admin

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Logout
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.CurrencyRupee
import androidx.compose.material.icons.rounded.Inventory2
import androidx.compose.material.icons.rounded.PendingActions
import androidx.compose.material.icons.rounded.ReceiptLong
import androidx.compose.material.icons.rounded.RestartAlt
import androidx.compose.material.icons.rounded.Storefront
import androidx.compose.material.icons.rounded.Translate
import androidx.compose.material.icons.rounded.VerifiedUser
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.local.OrderWithItems
import com.example.data.model.OrderStatus
import com.example.data.model.SalesAnalytics
import com.example.ui.GramKartViewModel
import com.example.ui.components.formatTimestamp
import com.example.ui.i18n.AppLanguage
import com.example.ui.i18n.AppStrings
import com.example.ui.theme.GramBorder
import com.example.ui.theme.GramError
import com.example.ui.theme.GramErrorContainer
import com.example.ui.theme.GramGreenContainer
import com.example.ui.theme.GramGreenDark
import com.example.ui.theme.GramGreenPrimary
import com.example.ui.theme.GramInfo
import com.example.ui.theme.GramInfoContainer
import com.example.ui.theme.GramOrangeAccent
import com.example.ui.theme.GramOrangeContainer
import com.example.ui.theme.GramOrangeDark
import com.example.ui.theme.GramTextSecondary
import com.example.ui.theme.GramWhite

@Composable
fun AdminDashboardScreen(
    analytics: SalesAnalytics,
    orders: List<OrderWithItems>,
    language: AppLanguage,
    strings: AppStrings,
    onAcceptOrder: (Long) -> Unit,
    onRejectOrder: (Long) -> Unit
) {
    val pendingList = orders.filter { it.order.status == OrderStatus.PENDING.id }
    val maxCatRev = (analytics.categorySales.maxOfOrNull { it.revenueInr } ?: 1).coerceAtLeast(1)

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        // KPI 2x2 Grid
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    AdminKpiCard(
                        title = strings.totalSales,
                        value = "₹${analytics.totalSalesInr}",
                        icon = Icons.Rounded.CurrencyRupee,
                        bgColor = GramGreenContainer,
                        fgColor = GramGreenDark,
                        modifier = Modifier.weight(1f)
                    )
                    AdminKpiCard(
                        title = strings.totalOrders,
                        value = analytics.totalOrders.toString(),
                        icon = Icons.Rounded.ReceiptLong,
                        bgColor = GramInfoContainer,
                        fgColor = GramInfo,
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    AdminKpiCard(
                        title = strings.pendingOrders,
                        value = analytics.pendingOrders.toString(),
                        icon = Icons.Rounded.PendingActions,
                        bgColor = GramOrangeContainer,
                        fgColor = GramOrangeDark,
                        modifier = Modifier.weight(1f)
                    )
                    AdminKpiCard(
                        title = strings.lowStockItems,
                        value = "${analytics.lowStockCount} / ${analytics.totalProducts}",
                        icon = Icons.Rounded.Inventory2,
                        bgColor = if (analytics.lowStockCount > 0) GramErrorContainer else GramGreenContainer,
                        fgColor = if (analytics.lowStockCount > 0) GramError else GramGreenDark,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Sales by Category Chart Card
        item {
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
                        text = strings.salesByCategory,
                        style = MaterialTheme.typography.titleMedium
                    )
                    analytics.categorySales.forEach { stat ->
                        val ratio = (stat.revenueInr.toFloat() / maxCatRev).coerceIn(0f, 1f)
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = stat.category.label(language),
                                    style = MaterialTheme.typography.labelLarge
                                )
                                Text(
                                    text = "₹${stat.revenueInr} (${stat.unitsSold})",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = GramTextSecondary
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(10.dp)
                                    .clip(RoundedCornerShape(50))
                                    .background(stat.category.bgTint)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth(fraction = if (stat.revenueInr > 0) ratio.coerceAtLeast(0.06f) else 0f)
                                        .height(10.dp)
                                        .clip(RoundedCornerShape(50))
                                        .background(stat.category.tintColor)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Pending Action Queue
        item {
            Text(
                text = strings.pendingQueueTitle,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        if (pendingList.isEmpty()) {
            item {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = GramGreenContainer.copy(alpha = 0.5f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = strings.noPendingOrders,
                        style = MaterialTheme.typography.bodyMedium,
                        color = GramGreenDark,
                        modifier = Modifier.padding(18.dp)
                    )
                }
            }
        } else {
            items(pendingList, key = { it.order.id }) { orderWithItems ->
                val order = orderWithItems.order
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = GramWhite),
                    border = BorderStroke(1.dp, GramOrangeAccent.copy(alpha = 0.4f))
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "${order.orderCode} • ${order.customerName}",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "${order.customerPhone} • ${formatTimestamp(order.createdAtMillis)}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = GramTextSecondary
                                )
                            }
                            Text(
                                text = "₹${order.totalInr}",
                                style = MaterialTheme.typography.titleMedium,
                                color = GramGreenDark,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                        Text(
                            text = orderWithItems.items.joinToString(", ") {
                                "${it.quantity}x ${it.displayName(language)}"
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = GramTextSecondary
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Button(
                                onClick = { onAcceptOrder(order.id) },
                                colors = ButtonDefaults.buttonColors(containerColor = GramGreenPrimary),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("dashboard_accept_${order.orderCode}")
                            ) {
                                Icon(Icons.Rounded.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(strings.acceptOrder)
                            }
                            OutlinedButton(
                                onClick = { onRejectOrder(order.id) },
                                border = BorderStroke(1.dp, GramError),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = GramError),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("dashboard_reject_${order.orderCode}")
                            ) {
                                Icon(Icons.Rounded.Close, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(strings.rejectOrder)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AdminKpiCard(
    title: String,
    value: String,
    icon: ImageVector,
    bgColor: Color,
    fgColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = GramWhite),
        border = BorderStroke(1.dp, GramBorder),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = bgColor,
                modifier = Modifier.size(36.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, contentDescription = null, tint = fgColor, modifier = Modifier.size(20.dp))
                }
            }
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = GramTextSecondary
            )
        }
    }
}

@Composable
fun AdminSettingsScreen(
    language: AppLanguage,
    strings: AppStrings,
    onSelectLanguage: (AppLanguage) -> Unit,
    onResetDemoData: () -> Unit,
    onSwitchToCustomer: () -> Unit,
    onLogoutAdmin: () -> Unit
) {
    var showResetDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Admin Identity Card
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = GramGreenDark)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.VerifiedUser,
                    contentDescription = null,
                    tint = GramWhite,
                    modifier = Modifier.size(34.dp)
                )
                Column {
                    Text(
                        text = strings.loggedInAs,
                        style = MaterialTheme.typography.titleMedium,
                        color = GramWhite
                    )
                    Text(
                        text = GramKartViewModel.DEMO_ADMIN_EMAIL,
                        style = MaterialTheme.typography.bodySmall,
                        color = GramWhite.copy(alpha = 0.85f)
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
                            )
                        )
                    }
                }
            }
        }

        // Reset Demo Data Card
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = GramOrangeContainer.copy(alpha = 0.55f)),
            border = BorderStroke(1.dp, GramOrangeAccent.copy(alpha = 0.35f))
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Rounded.RestartAlt, contentDescription = null, tint = GramOrangeDark)
                    Text(strings.resetDemoTitle, style = MaterialTheme.typography.titleMedium, color = GramOrangeDark)
                }
                Text(
                    text = strings.resetDemoSubtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = GramTextSecondary
                )
                Button(
                    onClick = { showResetDialog = true },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GramOrangeAccent),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("reset_demo_data_button")
                ) {
                    Icon(Icons.Rounded.RestartAlt, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(strings.resetDemoBtn, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Navigation Actions
        Button(
            onClick = onSwitchToCustomer,
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = GramGreenPrimary),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("switch_to_customer_button")
        ) {
            Icon(Icons.Rounded.Storefront, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(strings.switchToCustomer, fontWeight = FontWeight.Bold)
        }

        OutlinedButton(
            onClick = onLogoutAdmin,
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, GramError),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = GramError),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("logout_admin_button")
        ) {
            Icon(Icons.AutoMirrored.Rounded.Logout, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(strings.logoutAdmin, fontWeight = FontWeight.Bold)
        }
    }

    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text(strings.resetConfirmTitle) },
            text = { Text(strings.resetConfirmBody) },
            confirmButton = {
                Button(
                    onClick = {
                        showResetDialog = false
                        onResetDemoData()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GramOrangeAccent),
                    modifier = Modifier.testTag("confirm_reset_demo_button")
                ) {
                    Text(strings.resetDemoBtn)
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
                    Text(strings.cancelBtn)
                }
            }
        )
    }
}
