package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.GramKartRepository
import com.example.data.PlaceOrderResult
import com.example.data.local.GramKartDatabase
import com.example.data.local.OrderWithItems
import com.example.data.local.ProductEntity
import com.example.data.local.UserPreferencesRepository
import com.example.data.model.CartSummary
import com.example.data.model.CustomerProfile
import com.example.data.model.OrderStatus
import com.example.data.model.ProductCategory
import com.example.data.model.SalesAnalytics
import com.example.ui.i18n.AppLanguage
import com.example.ui.i18n.stringsFor
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppMode {
    CUSTOMER,
    ADMIN_LOGIN,
    ADMIN_PANEL
}

enum class CustomerTab {
    HOME,
    CART,
    ORDERS,
    PROFILE
}

sealed interface CustomerSubScreen {
    data object None : CustomerSubScreen
    data object Checkout : CustomerSubScreen
    data class OrderSuccess(val orderCode: String) : CustomerSubScreen
}

enum class AdminTab {
    DASHBOARD,
    PRODUCTS,
    ORDERS,
    SETTINGS
}

class GramKartViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: GramKartRepository

    init {
        val db = GramKartDatabase.getInstance(application)
        val prefs = UserPreferencesRepository(application)
        repository = GramKartRepository(db, prefs)
        viewModelScope.launch {
            repository.ensureSeeded()
        }
    }

    private val _appMode = MutableStateFlow(AppMode.CUSTOMER)
    val appMode: StateFlow<AppMode> = _appMode.asStateFlow()

    private val _customerTab = MutableStateFlow(CustomerTab.HOME)
    val customerTab: StateFlow<CustomerTab> = _customerTab.asStateFlow()

    private val _customerSubScreen = MutableStateFlow<CustomerSubScreen>(CustomerSubScreen.None)
    val customerSubScreen: StateFlow<CustomerSubScreen> = _customerSubScreen.asStateFlow()

    private val _adminTab = MutableStateFlow(AdminTab.DASHBOARD)
    val adminTab: StateFlow<AdminTab> = _adminTab.asStateFlow()

    private val _customerSearch = MutableStateFlow("")
    val customerSearch: StateFlow<String> = _customerSearch.asStateFlow()

    private val _selectedCategory = MutableStateFlow<ProductCategory?>(null)
    val selectedCategory: StateFlow<ProductCategory?> = _selectedCategory.asStateFlow()

    private val _adminProductSearch = MutableStateFlow("")
    val adminProductSearch: StateFlow<String> = _adminProductSearch.asStateFlow()

    private val _adminProductCategory = MutableStateFlow<ProductCategory?>(null)
    val adminProductCategory: StateFlow<ProductCategory?> = _adminProductCategory.asStateFlow()

    private val _adminOrderStatusFilter = MutableStateFlow<OrderStatus?>(null)
    val adminOrderStatusFilter: StateFlow<OrderStatus?> = _adminOrderStatusFilter.asStateFlow()

    private val _adminOrderSearch = MutableStateFlow("")
    val adminOrderSearch: StateFlow<String> = _adminOrderSearch.asStateFlow()

    private val _snackbarEvents = MutableSharedFlow<String>(extraBufferCapacity = 4)
    val snackbarEvents = _snackbarEvents.asSharedFlow()

    val language: StateFlow<AppLanguage> = repository.languageFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AppLanguage.EN)

    val allProducts: StateFlow<List<ProductEntity>> = repository.productsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val filteredCustomerProducts: StateFlow<List<ProductEntity>> = combine(
        repository.productsFlow,
        _customerSearch,
        _selectedCategory
    ) { products, query, category ->
        val trimmed = query.trim().lowercase()
        products.filter { product ->
            val matchesCat = category == null || product.category == category.id
            val matchesQuery = trimmed.isEmpty() ||
                product.nameEn.lowercase().contains(trimmed) ||
                product.nameHi.lowercase().contains(trimmed) ||
                product.descriptionEn.lowercase().contains(trimmed) ||
                product.category.lowercase().contains(trimmed)
            matchesCat && matchesQuery
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val filteredAdminProducts: StateFlow<List<ProductEntity>> = combine(
        repository.productsFlow,
        _adminProductSearch,
        _adminProductCategory
    ) { products, query, category ->
        val trimmed = query.trim().lowercase()
        products.filter { product ->
            val matchesCat = category == null || product.category == category.id
            val matchesQuery = trimmed.isEmpty() ||
                product.nameEn.lowercase().contains(trimmed) ||
                product.nameHi.lowercase().contains(trimmed)
            matchesCat && matchesQuery
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val cartSummary: StateFlow<CartSummary> = repository.cartSummaryFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), CartSummary())

    val cartQuantities: StateFlow<Map<Long, Int>> = repository.cartQuantitiesMapFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())

    val allOrders: StateFlow<List<OrderWithItems>> = repository.ordersFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val filteredAdminOrders: StateFlow<List<OrderWithItems>> = combine(
        repository.ordersFlow,
        _adminOrderStatusFilter,
        _adminOrderSearch
    ) { orders, filter, query ->
        val trimmed = query.trim().lowercase()
        orders.filter { item ->
            val matchesStatus = filter == null || OrderStatus.fromId(item.order.status) == filter
            val matchesQuery = trimmed.isEmpty() ||
                item.order.orderCode.lowercase().contains(trimmed) ||
                item.order.customerName.lowercase().contains(trimmed) ||
                item.order.customerPhone.lowercase().contains(trimmed)
            matchesStatus && matchesQuery
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val adminOrderStatusCounts: StateFlow<Map<OrderStatus, Int>> = combine(
        repository.ordersFlow,
        _adminOrderSearch
    ) { orders, _ ->
        OrderStatus.entries.associateWith { status ->
            orders.count { OrderStatus.fromId(it.order.status) == status }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())

    val customerProfile: StateFlow<CustomerProfile> = repository.customerProfileFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), CustomerProfile())

    val isAdminLoggedIn: StateFlow<Boolean> = repository.isAdminLoggedInFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val salesAnalytics: StateFlow<SalesAnalytics> = repository.salesAnalyticsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SalesAnalytics())

    // Customer Navigation & Filtering
    fun selectCustomerTab(tab: CustomerTab) {
        _customerSubScreen.value = CustomerSubScreen.None
        _customerTab.value = tab
    }

    fun openCheckout() {
        _customerSubScreen.value = CustomerSubScreen.Checkout
    }

    fun closeSubScreen() {
        _customerSubScreen.value = CustomerSubScreen.None
    }

    fun updateCustomerSearch(query: String) {
        _customerSearch.value = query
    }

    fun selectCategory(category: ProductCategory?) {
        _selectedCategory.value = category
    }

    fun clearCustomerFilters() {
        _customerSearch.value = ""
        _selectedCategory.value = null
    }

    // Cart Actions
    fun addToCart(productId: Long) {
        viewModelScope.launch { repository.addToCart(productId) }
    }

    fun decrementCart(productId: Long) {
        viewModelScope.launch { repository.decrementCart(productId) }
    }

    fun removeFromCart(productId: Long) {
        viewModelScope.launch { repository.removeFromCart(productId) }
    }

    fun placeOrder(
        name: String,
        phone: String,
        address: String,
        notes: String
    ) {
        viewModelScope.launch {
            val strings = stringsFor(language.value)
            when (val result = repository.placeOrder(name, phone, address, notes)) {
                is PlaceOrderResult.Success -> {
                    _customerSubScreen.value = CustomerSubScreen.OrderSuccess(result.orderCode)
                }
                PlaceOrderResult.StockAdjusted -> {
                    _snackbarEvents.tryEmit(strings.errStockInsufficient)
                    _customerSubScreen.value = CustomerSubScreen.None
                    _customerTab.value = CustomerTab.CART
                }
                PlaceOrderResult.EmptyCart -> {
                    _customerSubScreen.value = CustomerSubScreen.None
                    _customerTab.value = CustomerTab.CART
                }
            }
        }
    }

    fun goToOrdersAfterSuccess() {
        _customerSubScreen.value = CustomerSubScreen.None
        _customerTab.value = CustomerTab.ORDERS
    }

    fun continueShoppingAfterSuccess() {
        _customerSubScreen.value = CustomerSubScreen.None
        _customerTab.value = CustomerTab.HOME
    }

    // Profile & Language
    fun toggleLanguage() {
        viewModelScope.launch {
            val next = if (language.value == AppLanguage.EN) AppLanguage.HI else AppLanguage.EN
            repository.setLanguage(next)
        }
    }

    fun setLanguage(lang: AppLanguage) {
        viewModelScope.launch { repository.setLanguage(lang) }
    }

    fun saveCustomerProfile(profile: CustomerProfile) {
        viewModelScope.launch {
            repository.updateCustomerProfile(profile)
            _snackbarEvents.tryEmit(stringsFor(language.value).detailsSavedMsg)
        }
    }

    // Mode Switching & Admin Auth
    fun openAdminEntry() {
        _appMode.value = if (isAdminLoggedIn.value) AppMode.ADMIN_PANEL else AppMode.ADMIN_LOGIN
    }

    fun backToCustomerStore() {
        _appMode.value = AppMode.CUSTOMER
    }

    fun attemptAdminLogin(email: String, password: String): Boolean {
        val valid = email.trim().equals(DEMO_ADMIN_EMAIL, ignoreCase = true) &&
            password == DEMO_ADMIN_PASSWORD
        if (valid) {
            viewModelScope.launch { repository.setAdminLoggedIn(true) }
            _adminTab.value = AdminTab.DASHBOARD
            _appMode.value = AppMode.ADMIN_PANEL
        }
        return valid
    }

    fun logoutAdmin() {
        viewModelScope.launch { repository.setAdminLoggedIn(false) }
        _appMode.value = AppMode.CUSTOMER
    }

    // Admin Panel Actions
    fun selectAdminTab(tab: AdminTab) {
        _adminTab.value = tab
    }

    fun updateAdminProductSearch(query: String) {
        _adminProductSearch.value = query
    }

    fun selectAdminProductCategory(category: ProductCategory?) {
        _adminProductCategory.value = category
    }

    fun selectAdminOrderFilter(status: OrderStatus?) {
        _adminOrderStatusFilter.value = status
    }

    fun updateAdminOrderSearch(query: String) {
        _adminOrderSearch.value = query
    }

    fun saveProduct(product: ProductEntity) {
        viewModelScope.launch { repository.saveProduct(product) }
    }

    fun deleteProduct(productId: Long) {
        viewModelScope.launch { repository.deleteProduct(productId) }
    }

    fun adjustStock(productId: Long, delta: Int) {
        viewModelScope.launch { repository.adjustProductStock(productId, delta) }
    }

    fun updateOrderStatus(orderId: Long, status: OrderStatus) {
        viewModelScope.launch { repository.updateOrderStatus(orderId, status) }
    }

    fun resetDemoData() {
        viewModelScope.launch {
            repository.resetDemoData()
            _snackbarEvents.tryEmit(stringsFor(language.value).resetDoneMsg)
        }
    }

    companion object {
        const val DEMO_ADMIN_EMAIL = "admin@gramkart.demo"
        const val DEMO_ADMIN_PASSWORD = "Admin123"
    }
}
