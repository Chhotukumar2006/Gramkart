package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.rounded.Storefront
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.model.OrderStatus
import com.example.ui.admin.AdminDashboardScreen
import com.example.ui.admin.AdminLoginScreen
import com.example.ui.admin.AdminOrdersScreen
import com.example.ui.admin.AdminProductsScreen
import com.example.ui.admin.AdminSettingsScreen
import com.example.ui.components.GramKartLogoMark
import com.example.ui.components.LanguageTogglePill
import com.example.ui.customer.CustomerCartScreen
import com.example.ui.customer.CustomerCheckoutScreen
import com.example.ui.customer.CustomerHomeScreen
import com.example.ui.customer.CustomerOrdersScreen
import com.example.ui.customer.CustomerProfileScreen
import com.example.ui.customer.OrderSuccessScreen
import com.example.ui.i18n.stringsFor
import com.example.ui.theme.GramBackground
import com.example.ui.theme.GramBorder
import com.example.ui.theme.GramGreenContainer
import com.example.ui.theme.GramGreenDark
import com.example.ui.theme.GramGreenPrimary
import com.example.ui.theme.GramOrangeAccent
import com.example.ui.theme.GramOrangeContainer
import com.example.ui.theme.GramOrangeDark
import com.example.ui.theme.GramTextSecondary
import com.example.ui.theme.GramWhite

@Composable
fun GramKartApp(
    viewModel: GramKartViewModel = viewModel()
) {
    val appMode by viewModel.appMode.collectAsStateWithLifecycle()
    val customerTab by viewModel.customerTab.collectAsStateWithLifecycle()
    val customerSubScreen by viewModel.customerSubScreen.collectAsStateWithLifecycle()
    val adminTab by viewModel.adminTab.collectAsStateWithLifecycle()
    val language by viewModel.language.collectAsStateWithLifecycle()

    val customerProducts by viewModel.filteredCustomerProducts.collectAsStateWithLifecycle()
    val customerSearch by viewModel.customerSearch.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val cartQuantities by viewModel.cartQuantities.collectAsStateWithLifecycle()
    val cartSummary by viewModel.cartSummary.collectAsStateWithLifecycle()
    val allOrders by viewModel.allOrders.collectAsStateWithLifecycle()
    val customerProfile by viewModel.customerProfile.collectAsStateWithLifecycle()

    val adminProducts by viewModel.filteredAdminProducts.collectAsStateWithLifecycle()
    val adminProductSearch by viewModel.adminProductSearch.collectAsStateWithLifecycle()
    val adminProductCategory by viewModel.adminProductCategory.collectAsStateWithLifecycle()
    val adminOrders by viewModel.filteredAdminOrders.collectAsStateWithLifecycle()
    val adminOrderFilter by viewModel.adminOrderStatusFilter.collectAsStateWithLifecycle()
    val adminOrderSearch by viewModel.adminOrderSearch.collectAsStateWithLifecycle()
    val adminOrderStatusCounts by viewModel.adminOrderStatusCounts.collectAsStateWithLifecycle()
    val salesAnalytics by viewModel.salesAnalytics.collectAsStateWithLifecycle()

    val strings = stringsFor(language)
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.snackbarEvents.collect { msg ->
            snackbarHostState.showSnackbar(msg)
        }
    }

    Scaffold(
        containerColor = GramBackground,
        contentWindowInsets = WindowInsets.safeDrawing,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (appMode) {
                AppMode.CUSTOMER -> {
                    when (val sub = customerSubScreen) {
                        is CustomerSubScreen.Checkout -> {
                            CustomerCheckoutScreen(
                                cartSummary = cartSummary,
                                profile = customerProfile,
                                language = language,
                                strings = strings,
                                onBack = { viewModel.closeSubScreen() },
                                onPlaceOrder = { name, phone, addr, notes ->
                                    viewModel.placeOrder(name, phone, addr, notes)
                                }
                            )
                        }
                        is CustomerSubScreen.OrderSuccess -> {
                            OrderSuccessScreen(
                                orderCode = sub.orderCode,
                                strings = strings,
                                onTrackOrder = { viewModel.goToOrdersAfterSuccess() },
                                onContinueShopping = { viewModel.continueShoppingAfterSuccess() }
                            )
                        }
                        CustomerSubScreen.None -> {
                            CustomerMainShell(
                                currentTab = customerTab,
                                cartCount = cartSummary.totalCount,
                                strings = strings,
                                onSelectTab = { viewModel.selectCustomerTab(it) }
                            ) {
                                when (customerTab) {
                                    CustomerTab.HOME -> CustomerHomeScreen(
                                        products = customerProducts,
                                        cartQuantities = cartQuantities,
                                        cartSummary = cartSummary,
                                        searchQuery = customerSearch,
                                        selectedCategory = selectedCategory,
                                        language = language,
                                        strings = strings,
                                        onSearchChange = viewModel::updateCustomerSearch,
                                        onSelectCategory = viewModel::selectCategory,
                                        onClearFilters = viewModel::clearCustomerFilters,
                                        onAddToCart = viewModel::addToCart,
                                        onDecrementCart = viewModel::decrementCart,
                                        onToggleLanguage = viewModel::toggleLanguage,
                                        onOpenAdmin = viewModel::openAdminEntry,
                                        onOpenCart = { viewModel.selectCustomerTab(CustomerTab.CART) }
                                    )
                                    CustomerTab.CART -> CustomerCartScreen(
                                        cartSummary = cartSummary,
                                        language = language,
                                        strings = strings,
                                        onAddToCart = viewModel::addToCart,
                                        onDecrementCart = viewModel::decrementCart,
                                        onRemoveFromCart = viewModel::removeFromCart,
                                        onBrowseGroceries = { viewModel.selectCustomerTab(CustomerTab.HOME) },
                                        onProceedToCheckout = viewModel::openCheckout
                                    )
                                    CustomerTab.ORDERS -> CustomerOrdersScreen(
                                        orders = allOrders,
                                        language = language,
                                        strings = strings,
                                        onBrowseGroceries = { viewModel.selectCustomerTab(CustomerTab.HOME) }
                                    )
                                    CustomerTab.PROFILE -> CustomerProfileScreen(
                                        profile = customerProfile,
                                        language = language,
                                        strings = strings,
                                        onSaveProfile = viewModel::saveCustomerProfile,
                                        onSelectLanguage = viewModel::setLanguage,
                                        onOpenAdminPanel = viewModel::openAdminEntry
                                    )
                                }
                            }
                        }
                    }
                }

                AppMode.ADMIN_LOGIN -> {
                    AdminLoginScreen(
                        language = language,
                        strings = strings,
                        onToggleLanguage = viewModel::toggleLanguage,
                        onBackToStore = viewModel::backToCustomerStore,
                        onLoginAttempt = viewModel::attemptAdminLogin
                    )
                }

                AppMode.ADMIN_PANEL -> {
                    BackHandler {
                        if (adminTab != AdminTab.DASHBOARD) {
                            viewModel.selectAdminTab(AdminTab.DASHBOARD)
                        } else {
                            viewModel.backToCustomerStore()
                        }
                    }

                    AdminMainShell(
                        currentTab = adminTab,
                        pendingOrdersCount = salesAnalytics.pendingOrders,
                        language = language,
                        strings = strings,
                        onSelectTab = viewModel::selectAdminTab,
                        onToggleLanguage = viewModel::toggleLanguage,
                        onSwitchToCustomer = viewModel::backToCustomerStore
                    ) {
                        when (adminTab) {
                            AdminTab.DASHBOARD -> AdminDashboardScreen(
                                analytics = salesAnalytics,
                                orders = allOrders,
                                language = language,
                                strings = strings,
                                onAcceptOrder = { id -> viewModel.updateOrderStatus(id, OrderStatus.CONFIRMED) },
                                onRejectOrder = { id -> viewModel.updateOrderStatus(id, OrderStatus.CANCELLED) }
                            )
                            AdminTab.PRODUCTS -> AdminProductsScreen(
                                products = adminProducts,
                                searchQuery = adminProductSearch,
                                selectedCategory = adminProductCategory,
                                language = language,
                                strings = strings,
                                onSearchChange = viewModel::updateAdminProductSearch,
                                onSelectCategory = viewModel::selectAdminProductCategory,
                                onSaveProduct = viewModel::saveProduct,
                                onDeleteProduct = viewModel::deleteProduct,
                                onAdjustStock = viewModel::adjustStock
                            )
                            AdminTab.ORDERS -> AdminOrdersScreen(
                                orders = adminOrders,
                                totalOrdersCount = allOrders.size,
                                statusCounts = adminOrderStatusCounts,
                                searchQuery = adminOrderSearch,
                                selectedFilter = adminOrderFilter,
                                language = language,
                                strings = strings,
                                onSearchChange = viewModel::updateAdminOrderSearch,
                                onSelectFilter = viewModel::selectAdminOrderFilter,
                                onUpdateStatus = viewModel::updateOrderStatus
                            )
                            AdminTab.SETTINGS -> AdminSettingsScreen(
                                language = language,
                                strings = strings,
                                onSelectLanguage = viewModel::setLanguage,
                                onResetDemoData = viewModel::resetDemoData,
                                onSwitchToCustomer = viewModel::backToCustomerStore,
                                onLogoutAdmin = viewModel::logoutAdmin
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CustomerMainShell(
    currentTab: CustomerTab,
    cartCount: Int,
    strings: com.example.ui.i18n.AppStrings,
    onSelectTab: (CustomerTab) -> Unit,
    content: @Composable () -> Unit
) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val useRail = maxWidth >= 600.dp

        if (useRail) {
            Row(modifier = Modifier.fillMaxSize()) {
                NavigationRail(
                    containerColor = GramWhite,
                    modifier = Modifier.fillMaxHeight()
                ) {
                    CustomerTab.entries.forEach { tab ->
                        val selected = currentTab == tab
                        val (label, activeIcon, inactiveIcon) = customerNavSpec(tab, strings)
                        NavigationRailItem(
                            selected = selected,
                            onClick = { onSelectTab(tab) },
                            icon = {
                                BadgedNavIcon(
                                    icon = if (selected) activeIcon else inactiveIcon,
                                    label = label,
                                    badgeCount = if (tab == CustomerTab.CART) cartCount else 0
                                )
                            },
                            label = { Text(label, maxLines = 1) },
                            colors = NavigationRailItemDefaults.colors(
                                selectedIconColor = GramGreenPrimary,
                                selectedTextColor = GramGreenDark,
                                indicatorColor = GramGreenContainer
                            ),
                            modifier = Modifier.testTag("nav_customer_${tab.name.lowercase()}")
                        )
                    }
                }
                Box(modifier = Modifier.weight(1f).fillMaxHeight()) {
                    content()
                }
            }
        } else {
            Column(modifier = Modifier.fillMaxSize()) {
                Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                    content()
                }
                NavigationBar(
                    containerColor = GramWhite,
                    tonalElevation = 4.dp
                ) {
                    CustomerTab.entries.forEach { tab ->
                        val selected = currentTab == tab
                        val (label, activeIcon, inactiveIcon) = customerNavSpec(tab, strings)
                        NavigationBarItem(
                            selected = selected,
                            onClick = { onSelectTab(tab) },
                            icon = {
                                BadgedNavIcon(
                                    icon = if (selected) activeIcon else inactiveIcon,
                                    label = label,
                                    badgeCount = if (tab == CustomerTab.CART) cartCount else 0
                                )
                            },
                            label = {
                                Text(
                                    text = label,
                                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                                    maxLines = 1
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = GramGreenPrimary,
                                selectedTextColor = GramGreenDark,
                                indicatorColor = GramGreenContainer,
                                unselectedIconColor = GramTextSecondary,
                                unselectedTextColor = GramTextSecondary
                            ),
                            modifier = Modifier.testTag("nav_customer_${tab.name.lowercase()}")
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AdminMainShell(
    currentTab: AdminTab,
    pendingOrdersCount: Int,
    language: com.example.ui.i18n.AppLanguage,
    strings: com.example.ui.i18n.AppStrings,
    onSelectTab: (AdminTab) -> Unit,
    onToggleLanguage: () -> Unit,
    onSwitchToCustomer: () -> Unit,
    content: @Composable () -> Unit
) {
    val title = when (currentTab) {
        AdminTab.DASHBOARD -> strings.adminDashboardTitle
        AdminTab.PRODUCTS -> strings.manageProductsTitle
        AdminTab.ORDERS -> strings.manageOrdersTitle
        AdminTab.SETTINGS -> strings.adminSettingsTitle
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Admin Top Header
        Surface(color = GramWhite, shadowElevation = 2.dp) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    GramKartLogoMark(size = 34.dp)
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.titleMedium,
                                color = GramGreenDark,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Surface(shape = RoundedCornerShape(6.dp), color = GramOrangeContainer) {
                                Text(
                                    text = strings.adminBtn.uppercase(),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = GramOrangeDark,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    LanguageTogglePill(language = language, onToggle = onToggleLanguage)
                    Surface(
                        onClick = onSwitchToCustomer,
                        shape = RoundedCornerShape(50),
                        color = GramGreenContainer,
                        border = BorderStroke(1.dp, GramBorder),
                        modifier = Modifier.testTag("admin_header_store_button")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Rounded.Storefront,
                                contentDescription = strings.switchToCustomer,
                                tint = GramGreenDark,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = strings.navHome,
                                style = MaterialTheme.typography.labelSmall,
                                color = GramGreenDark,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        BoxWithConstraints(modifier = Modifier.weight(1f).fillMaxWidth()) {
            val useRail = maxWidth >= 600.dp

            if (useRail) {
                Row(modifier = Modifier.fillMaxSize()) {
                    NavigationRail(containerColor = GramWhite) {
                        AdminTab.entries.forEach { tab ->
                            val selected = currentTab == tab
                            val (label, activeIcon, inactiveIcon) = adminNavSpec(tab, strings)
                            NavigationRailItem(
                                selected = selected,
                                onClick = { onSelectTab(tab) },
                                icon = {
                                    BadgedNavIcon(
                                        icon = if (selected) activeIcon else inactiveIcon,
                                        label = label,
                                        badgeCount = if (tab == AdminTab.ORDERS) pendingOrdersCount else 0
                                    )
                                },
                                label = { Text(label, maxLines = 1) },
                                colors = NavigationRailItemDefaults.colors(
                                    selectedIconColor = GramGreenPrimary,
                                    selectedTextColor = GramGreenDark,
                                    indicatorColor = GramGreenContainer
                                ),
                                modifier = Modifier.testTag("nav_admin_${tab.name.lowercase()}")
                            )
                        }
                    }
                    Box(modifier = Modifier.weight(1f).fillMaxHeight()) {
                        content()
                    }
                }
            } else {
                Column(modifier = Modifier.fillMaxSize()) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .background(GramBackground)
                    ) {
                        content()
                    }
                    NavigationBar(containerColor = GramWhite, tonalElevation = 4.dp) {
                        AdminTab.entries.forEach { tab ->
                            val selected = currentTab == tab
                            val (label, activeIcon, inactiveIcon) = adminNavSpec(tab, strings)
                            NavigationBarItem(
                                selected = selected,
                                onClick = { onSelectTab(tab) },
                                icon = {
                                    BadgedNavIcon(
                                        icon = if (selected) activeIcon else inactiveIcon,
                                        label = label,
                                        badgeCount = if (tab == AdminTab.ORDERS) pendingOrdersCount else 0
                                    )
                                },
                                label = {
                                    Text(
                                        text = label,
                                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                                        maxLines = 1
                                    )
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = GramGreenPrimary,
                                    selectedTextColor = GramGreenDark,
                                    indicatorColor = GramGreenContainer,
                                    unselectedIconColor = GramTextSecondary,
                                    unselectedTextColor = GramTextSecondary
                                ),
                                modifier = Modifier.testTag("nav_admin_${tab.name.lowercase()}")
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BadgedNavIcon(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    badgeCount: Int
) {
    if (badgeCount > 0) {
        BadgedBox(
            badge = {
                Badge(
                    containerColor = GramOrangeAccent,
                    contentColor = GramWhite
                ) {
                    Text(badgeCount.toString())
                }
            }
        ) {
            Icon(icon, contentDescription = label)
        }
    } else {
        Icon(icon, contentDescription = label)
    }
}

private fun customerNavSpec(
    tab: CustomerTab,
    strings: com.example.ui.i18n.AppStrings
) = when (tab) {
    CustomerTab.HOME -> Triple(strings.navHome, Icons.Filled.Home, Icons.Outlined.Home)
    CustomerTab.CART -> Triple(strings.navCart, Icons.Filled.ShoppingCart, Icons.Outlined.ShoppingCart)
    CustomerTab.ORDERS -> Triple(strings.navOrders, Icons.Filled.ReceiptLong, Icons.Outlined.ReceiptLong)
    CustomerTab.PROFILE -> Triple(strings.navProfile, Icons.Filled.Person, Icons.Outlined.Person)
}

private fun adminNavSpec(
    tab: AdminTab,
    strings: com.example.ui.i18n.AppStrings
) = when (tab) {
    AdminTab.DASHBOARD -> Triple(strings.adminNavDashboard, Icons.Filled.Dashboard, Icons.Outlined.Dashboard)
    AdminTab.PRODUCTS -> Triple(strings.adminNavProducts, Icons.Filled.Inventory2, Icons.Outlined.Inventory2)
    AdminTab.ORDERS -> Triple(strings.adminNavOrders, Icons.Filled.ReceiptLong, Icons.Outlined.ReceiptLong)
    AdminTab.SETTINGS -> Triple(strings.adminNavSettings, Icons.Filled.Settings, Icons.Outlined.Settings)
}
