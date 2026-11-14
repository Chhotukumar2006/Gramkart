package com.example.ui.admin

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.DeleteOutline
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Image
import androidx.compose.material.icons.rounded.Inventory2
import androidx.compose.material.icons.rounded.LocalShipping
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Payments
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.material.icons.rounded.ReceiptLong
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.local.OrderWithItems
import com.example.data.local.ProductEntity
import com.example.data.model.OrderStatus
import com.example.data.model.ProductCategory
import com.example.ui.components.OrderStatusChip
import com.example.ui.components.PRESET_ARTWORK_KEYS
import com.example.ui.components.ProductArtwork
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
import com.example.ui.theme.GramPurple
import com.example.ui.theme.GramTextSecondary
import com.example.ui.theme.GramWhite

@Composable
fun AdminProductsScreen(
    products: List<ProductEntity>,
    searchQuery: String,
    selectedCategory: ProductCategory?,
    language: AppLanguage,
    strings: AppStrings,
    onSearchChange: (String) -> Unit,
    onSelectCategory: (ProductCategory?) -> Unit,
    onSaveProduct: (ProductEntity) -> Unit,
    onDeleteProduct: (Long) -> Unit,
    onAdjustStock: (Long, Int) -> Unit
) {
    var editingProduct by remember { mutableStateOf<ProductEntity?>(null) }
    var isAddingNew by remember { mutableStateOf(false) }
    var productToDelete by remember { mutableStateOf<ProductEntity?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchChange,
                    placeholder = { Text(strings.searchPlaceholder) },
                    leadingIcon = { Icon(Icons.Rounded.Search, contentDescription = null, tint = GramGreenPrimary) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(
                                onClick = { onSearchChange("") },
                                modifier = Modifier.testTag("admin_products_search_clear")
                            ) {
                                Icon(Icons.Rounded.Clear, contentDescription = "Clear search")
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = GramWhite,
                        unfocusedContainerColor = GramWhite
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("admin_products_search_input")
                )
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    item {
                        FilterChip(
                            selected = selectedCategory == null,
                            onClick = { onSelectCategory(null) },
                            label = { Text(strings.allCategory) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = GramGreenContainer,
                                selectedLabelColor = GramGreenDark
                            )
                        )
                    }
                    items(ProductCategory.entries) { cat ->
                        FilterChip(
                            selected = selectedCategory == cat,
                            onClick = { onSelectCategory(if (selectedCategory == cat) null else cat) },
                            label = { Text(cat.label(language)) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = GramGreenContainer,
                                selectedLabelColor = GramGreenDark
                            )
                        )
                    }
                }
            }

            if (products.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(strings.noProductsFound, color = GramTextSecondary)
                        if (searchQuery.isNotEmpty() || selectedCategory != null) {
                            OutlinedButton(onClick = {
                                onSearchChange("")
                                onSelectCategory(null)
                            }) {
                                Text(strings.clearFilters)
                            }
                        }
                    }
                }
            } else {
            LazyColumn(
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 88.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(products, key = { it.id }) { product ->
                    val cat = ProductCategory.fromId(product.category)
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = GramWhite),
                        border = BorderStroke(1.dp, GramBorder),
                        modifier = Modifier.testTag("admin_product_card_${product.id}")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            ProductArtwork(
                                imageKey = product.imageKey,
                                categoryId = product.category,
                                contentDescription = product.displayName(language),
                                modifier = Modifier.size(62.dp)
                            )
                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = product.displayName(language),
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold
                                )
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Surface(shape = RoundedCornerShape(6.dp), color = cat.bgTint) {
                                        Text(
                                            text = cat.label(language),
                                            style = MaterialTheme.typography.labelSmall,
                                            color = cat.tintColor,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                    Text(
                                        text = product.displayUnit(language),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = GramTextSecondary
                                    )
                                }
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Text(
                                        text = "₹${product.priceInr}",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = GramGreenDark,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = if (product.stock <= 5) GramOrangeContainer else GramGreenContainer
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                        ) {
                                            IconButton(
                                                onClick = { onAdjustStock(product.id, -1) },
                                                modifier = Modifier.size(26.dp)
                                            ) {
                                                Icon(
                                                    Icons.Rounded.Remove,
                                                    contentDescription = "Decrease stock",
                                                    modifier = Modifier.size(14.dp)
                                                )
                                            }
                                            Text(
                                                text = "${strings.stockLabel}: ${product.stock}",
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = if (product.stock <= 5) GramOrangeDark else GramGreenDark
                                            )
                                            IconButton(
                                                onClick = { onAdjustStock(product.id, 1) },
                                                modifier = Modifier.size(26.dp)
                                            ) {
                                                Icon(
                                                    Icons.Rounded.Add,
                                                    contentDescription = "Increase stock",
                                                    modifier = Modifier.size(14.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                            Column {
                                IconButton(
                                    onClick = { editingProduct = product },
                                    modifier = Modifier.testTag("edit_product_${product.id}")
                                ) {
                                    Icon(Icons.Rounded.Edit, contentDescription = "Edit", tint = GramGreenPrimary)
                                }
                                IconButton(
                                    onClick = { productToDelete = product },
                                    modifier = Modifier.testTag("delete_product_${product.id}")
                                ) {
                                    Icon(Icons.Rounded.DeleteOutline, contentDescription = "Delete", tint = GramError)
                                }
                            }
                        }
                    }
                }
            }
            }
        }

        FloatingActionButton(
            onClick = { isAddingNew = true },
            containerColor = GramOrangeAccent,
            contentColor = GramWhite,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .testTag("admin_add_product_fab")
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(Icons.Rounded.Add, contentDescription = null)
                Text(strings.addProductBtn, fontWeight = FontWeight.Bold)
            }
        }
    }

    if (isAddingNew || editingProduct != null) {
        ProductEditorDialog(
            initial = editingProduct,
            language = language,
            strings = strings,
            onDismiss = {
                isAddingNew = false
                editingProduct = null
            },
            onSave = { updated ->
                onSaveProduct(updated)
                isAddingNew = false
                editingProduct = null
            }
        )
    }

    productToDelete?.let { prod ->
        AlertDialog(
            onDismissRequest = { productToDelete = null },
            title = { Text(strings.deleteProductTitle) },
            text = { Text(strings.deleteProductMsg(prod.displayName(language))) },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteProduct(prod.id)
                        productToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GramError)
                ) {
                    Text(strings.deleteBtn)
                }
            },
            dismissButton = {
                TextButton(onClick = { productToDelete = null }) {
                    Text(strings.cancelBtn)
                }
            }
        )
    }
}

@Composable
private fun ProductEditorDialog(
    initial: ProductEntity?,
    language: AppLanguage,
    strings: AppStrings,
    onDismiss: () -> Unit,
    onSave: (ProductEntity) -> Unit
) {
    val context = LocalContext.current
    var nameEn by remember { mutableStateOf(initial?.nameEn ?: "") }
    var nameHi by remember { mutableStateOf(initial?.nameHi ?: "") }
    var category by remember { mutableStateOf(ProductCategory.fromId(initial?.category ?: ProductCategory.GROCERY.id)) }
    var priceText by remember { mutableStateOf(initial?.priceInr?.toString() ?: "") }
    var mrpText by remember { mutableStateOf(initial?.mrpInr?.toString() ?: "") }
    var unitEn by remember { mutableStateOf(initial?.unitEn ?: "1 kg") }
    var unitHi by remember { mutableStateOf(initial?.unitHi ?: "1 किग्रा") }
    var stockText by remember { mutableStateOf(initial?.stock?.toString() ?: "10") }
    var imageKey by remember { mutableStateOf(initial?.imageKey ?: "rice") }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            try {
                context.contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (_: SecurityException) {
                // Temporary permission still works for current session
            }
            imageKey = uri.toString()
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = GramWhite),
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .padding(vertical = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = if (initial == null) strings.addProductTitle else strings.editProductTitle,
                    style = MaterialTheme.typography.headlineSmall,
                    color = GramGreenDark
                )

                OutlinedTextField(
                    value = nameEn,
                    onValueChange = { nameEn = it },
                    label = { Text(strings.productNameEn) },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("editor_name_en")
                )
                OutlinedTextField(
                    value = nameHi,
                    onValueChange = { nameHi = it },
                    label = { Text(strings.productNameHi) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Text(strings.categoryLabel, style = MaterialTheme.typography.labelLarge)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(ProductCategory.entries) { cat ->
                        FilterChip(
                            selected = category == cat,
                            onClick = { category = cat },
                            label = { Text(cat.label(language)) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = GramGreenContainer,
                                selectedLabelColor = GramGreenDark
                            )
                        )
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = priceText,
                        onValueChange = { priceText = it.filter { c -> c.isDigit() } },
                        label = { Text(strings.priceInrLabel) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("editor_price_inr")
                    )
                    OutlinedTextField(
                        value = mrpText,
                        onValueChange = { mrpText = it.filter { c -> c.isDigit() } },
                        label = { Text(strings.mrpInrLabel) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = unitEn,
                        onValueChange = { unitEn = it },
                        label = { Text(strings.unitEnLabel) },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = stockText,
                        onValueChange = { stockText = it.filter { c -> c.isDigit() } },
                        label = { Text(strings.stockQtyLabel) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("editor_stock_qty")
                    )
                }

                OutlinedTextField(
                    value = unitHi,
                    onValueChange = { unitHi = it },
                    label = { Text(strings.unitHiLabel) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(strings.selectArtworkLabel, style = MaterialTheme.typography.labelLarge)
                    OutlinedButton(
                        onClick = {
                            photoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Rounded.Image, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(strings.pickPhotoBtn)
                    }
                }

                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(PRESET_ARTWORK_KEYS) { key ->
                        val selected = imageKey == key
                        Box(
                            modifier = Modifier
                                .size(54.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .border(
                                    width = if (selected) 2.5.dp else 1.dp,
                                    color = if (selected) GramGreenPrimary else GramBorder,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clickable { imageKey = key }
                        ) {
                            ProductArtwork(
                                imageKey = key,
                                categoryId = category.id,
                                contentDescription = key,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(strings.cancelBtn)
                    }
                    Button(
                        onClick = {
                            val price = priceText.toIntOrNull()?.coerceAtLeast(1) ?: 0
                            val mrp = mrpText.toIntOrNull()?.coerceAtLeast(price) ?: price
                            val stock = stockText.toIntOrNull()?.coerceAtLeast(0) ?: 0
                            if (nameEn.isNotBlank() && price > 0) {
                                onSave(
                                    ProductEntity(
                                        id = initial?.id ?: 0L,
                                        nameEn = nameEn.trim(),
                                        nameHi = nameHi.trim().ifEmpty { nameEn.trim() },
                                        category = category.id,
                                        priceInr = price,
                                        mrpInr = mrp,
                                        unitEn = unitEn.trim().ifEmpty { "1 pc" },
                                        unitHi = unitHi.trim().ifEmpty { unitEn.trim() },
                                        stock = stock,
                                        imageKey = imageKey,
                                        descriptionEn = initial?.descriptionEn ?: "",
                                        descriptionHi = initial?.descriptionHi ?: ""
                                    )
                                )
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = GramGreenPrimary),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("save_product_button")
                    ) {
                        Text(strings.saveProductBtn, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun AdminOrdersScreen(
    orders: List<OrderWithItems>,
    totalOrdersCount: Int,
    statusCounts: Map<OrderStatus, Int>,
    searchQuery: String,
    selectedFilter: OrderStatus?,
    language: AppLanguage,
    strings: AppStrings,
    onSearchChange: (String) -> Unit,
    onSelectFilter: (OrderStatus?) -> Unit,
    onUpdateStatus: (Long, OrderStatus) -> Unit
) {
    var selectedOrderIdForDetails by remember { mutableStateOf<Long?>(null) }
    val selectedOrderForDetails = remember(orders, selectedOrderIdForDetails) {
        orders.find { it.order.id == selectedOrderIdForDetails }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchChange,
                placeholder = { Text(strings.searchOrdersPlaceholder) },
                leadingIcon = {
                    Icon(Icons.Rounded.Search, contentDescription = null, tint = GramGreenPrimary)
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { onSearchChange("") }) {
                            Icon(Icons.Rounded.Clear, contentDescription = "Clear search")
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = GramWhite,
                    unfocusedContainerColor = GramWhite
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("admin_orders_search_input")
            )
        }

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                FilterChip(
                    selected = selectedFilter == null,
                    onClick = { onSelectFilter(null) },
                    label = { Text("${strings.allCategory} ($totalOrdersCount)") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = GramGreenContainer,
                        selectedLabelColor = GramGreenDark
                    ),
                    modifier = Modifier.testTag("order_filter_all")
                )
            }
            items(OrderStatus.entries) { status ->
                val count = statusCounts[status] ?: 0
                FilterChip(
                    selected = selectedFilter == status,
                    onClick = { onSelectFilter(if (selectedFilter == status) null else status) },
                    label = { Text("${status.label(language)} ($count)") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = GramGreenContainer,
                        selectedLabelColor = GramGreenDark
                    ),
                    modifier = Modifier.testTag("order_filter_${status.id.lowercase()}")
                )
            }
        }

        if (orders.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(strings.noOrdersTitle, color = GramTextSecondary)
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(orders, key = { it.order.id }) { orderWithItems ->
                    AdminOrderCard(
                        orderWithItems = orderWithItems,
                        language = language,
                        strings = strings,
                        onViewDetails = { selectedOrderIdForDetails = orderWithItems.order.id },
                        onUpdateStatus = { newStatus ->
                            onUpdateStatus(orderWithItems.order.id, newStatus)
                        }
                    )
                }
            }
        }
    }

    if (selectedOrderForDetails != null) {
        AdminOrderDetailsDialog(
            orderWithItems = selectedOrderForDetails,
            language = language,
            strings = strings,
            onDismiss = { selectedOrderIdForDetails = null },
            onUpdateStatus = { newStatus ->
                onUpdateStatus(selectedOrderForDetails.order.id, newStatus)
            }
        )
    }
}

@Composable
private fun AdminOrderCard(
    orderWithItems: OrderWithItems,
    language: AppLanguage,
    strings: AppStrings,
    onViewDetails: () -> Unit,
    onUpdateStatus: (OrderStatus) -> Unit
) {
    val order = orderWithItems.order
    val status = OrderStatus.fromId(order.status)
    var statusMenuExpanded by remember { mutableStateOf(false) }

    Card(
        onClick = onViewDetails,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = GramWhite),
        border = BorderStroke(1.dp, GramBorder),
        modifier = Modifier.testTag("admin_order_card_${order.orderCode}")
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = GramGreenContainer
                        ) {
                            Text(
                                text = order.orderCode,
                                style = MaterialTheme.typography.titleSmall,
                                color = GramGreenDark,
                                fontWeight = FontWeight.ExtraBold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                        Text(
                            text = strings.itemsCount(orderWithItems.items.sumOf { it.quantity }),
                            style = MaterialTheme.typography.labelMedium,
                            color = GramTextSecondary
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = formatTimestamp(order.createdAtMillis),
                        style = MaterialTheme.typography.bodySmall,
                        color = GramTextSecondary
                    )
                }
                Box {
                    Surface(
                        onClick = { statusMenuExpanded = true },
                        shape = RoundedCornerShape(50),
                        modifier = Modifier.testTag("admin_status_dropdown_${order.orderCode}")
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            OrderStatusChip(status = status, language = language)
                            Icon(
                                Icons.Rounded.ArrowDropDown,
                                contentDescription = strings.updateStatusLabel,
                                modifier = Modifier.padding(end = 4.dp)
                            )
                        }
                    }
                    DropdownMenu(
                        expanded = statusMenuExpanded,
                        onDismissRequest = { statusMenuExpanded = false }
                    ) {
                        OrderStatus.entries.forEach { option ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = option.label(language),
                                        fontWeight = if (option == status) FontWeight.Bold else FontWeight.Normal
                                    )
                                },
                                onClick = {
                                    statusMenuExpanded = false
                                    onUpdateStatus(option)
                                }
                            )
                        }
                    }
                }
            }

            // Customer Info Box
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = GramGreenContainer.copy(alpha = 0.35f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(10.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "${strings.customerInfoLabel}: ${order.customerName}",
                        style = MaterialTheme.typography.labelLarge
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Rounded.Phone, contentDescription = null, modifier = Modifier.size(14.dp), tint = GramGreenDark)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(order.customerPhone, style = MaterialTheme.typography.bodySmall)
                    }
                    Row(verticalAlignment = Alignment.Top) {
                        Icon(Icons.Rounded.LocationOn, contentDescription = null, modifier = Modifier.size(14.dp), tint = GramGreenDark)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(order.deliveryAddress, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }

            // Items preview
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                orderWithItems.items.forEach { item ->
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
                OutlinedButton(
                    onClick = onViewDetails,
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, GramGreenPrimary.copy(alpha = 0.45f)),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                    modifier = Modifier.testTag("admin_view_details_${order.orderCode}")
                ) {
                    Icon(Icons.Rounded.Visibility, contentDescription = null, modifier = Modifier.size(16.dp), tint = GramGreenPrimary)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(strings.viewOrderDetails, color = GramGreenDark, style = MaterialTheme.typography.labelMedium)
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(order.paymentMethod, style = MaterialTheme.typography.labelSmall, color = GramTextSecondary)
                    Text(
                        text = "₹${order.totalInr}",
                        style = MaterialTheme.typography.titleLarge,
                        color = GramGreenDark,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }

            // Contextual Quick Action Buttons (Accept / Reject / Stage Progression)
            when (status) {
                OrderStatus.PENDING -> {
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Button(
                            onClick = { onUpdateStatus(OrderStatus.CONFIRMED) },
                            colors = ButtonDefaults.buttonColors(containerColor = GramGreenPrimary),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("admin_accept_${order.orderCode}")
                        ) {
                            Icon(Icons.Rounded.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(strings.acceptOrder)
                        }
                        OutlinedButton(
                            onClick = { onUpdateStatus(OrderStatus.CANCELLED) },
                            border = BorderStroke(1.dp, GramError),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = GramError),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("admin_reject_${order.orderCode}")
                        ) {
                            Icon(Icons.Rounded.Close, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(strings.rejectOrder)
                        }
                    }
                }
                OrderStatus.CONFIRMED -> {
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Button(
                            onClick = { onUpdateStatus(OrderStatus.PREPARING) },
                            colors = ButtonDefaults.buttonColors(containerColor = GramOrangeAccent),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("admin_prepare_${order.orderCode}")
                        ) {
                            Icon(Icons.Rounded.Inventory2, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(strings.startPreparing)
                        }
                        OutlinedButton(
                            onClick = { onUpdateStatus(OrderStatus.CANCELLED) },
                            border = BorderStroke(1.dp, GramError),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = GramError),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.testTag("admin_reject_${order.orderCode}")
                        ) {
                            Text(strings.rejectOrder)
                        }
                    }
                }
                OrderStatus.PREPARING -> {
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Button(
                            onClick = { onUpdateStatus(OrderStatus.OUT_FOR_DELIVERY) },
                            colors = ButtonDefaults.buttonColors(containerColor = GramPurple),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("admin_dispatch_${order.orderCode}")
                        ) {
                            Icon(Icons.Rounded.LocalShipping, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(strings.markOutForDelivery)
                        }
                        OutlinedButton(
                            onClick = { onUpdateStatus(OrderStatus.CANCELLED) },
                            border = BorderStroke(1.dp, GramError),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = GramError),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(strings.rejectOrder)
                        }
                    }
                }
                OrderStatus.OUT_FOR_DELIVERY -> {
                    Button(
                        onClick = { onUpdateStatus(OrderStatus.DELIVERED) },
                        colors = ButtonDefaults.buttonColors(containerColor = GramGreenPrimary),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("admin_deliver_${order.orderCode}")
                    ) {
                        Icon(Icons.Rounded.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(strings.markDelivered)
                    }
                }
                OrderStatus.DELIVERED, OrderStatus.CANCELLED -> Unit
            }
        }
    }
}

@Composable
private fun AdminOrderDetailsDialog(
    orderWithItems: OrderWithItems,
    language: AppLanguage,
    strings: AppStrings,
    onDismiss: () -> Unit,
    onUpdateStatus: (OrderStatus) -> Unit
) {
    val order = orderWithItems.order
    val status = OrderStatus.fromId(order.status)

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = GramWhite),
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .padding(vertical = 16.dp)
                .testTag("admin_order_details_dialog")
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "${strings.orderDetailsTitle} • ${order.orderCode}",
                            style = MaterialTheme.typography.titleLarge,
                            color = GramGreenDark,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            text = formatTimestamp(order.createdAtMillis),
                            style = MaterialTheme.typography.bodySmall,
                            color = GramTextSecondary
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Rounded.Close, contentDescription = strings.closeBtn)
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OrderStatusChip(status = status, language = language)
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = GramGreenContainer.copy(alpha = 0.5f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Rounded.Payments, contentDescription = null, modifier = Modifier.size(14.dp), tint = GramGreenDark)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(order.paymentMethod, style = MaterialTheme.typography.labelSmall, color = GramGreenDark)
                        }
                    }
                }

                // Customer Information
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = GramGreenContainer.copy(alpha = 0.35f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(strings.customerInfoLabel, style = MaterialTheme.typography.titleSmall, color = GramGreenDark)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Rounded.Person, contentDescription = null, modifier = Modifier.size(16.dp), tint = GramGreenPrimary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(order.customerName, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Rounded.Phone, contentDescription = null, modifier = Modifier.size(16.dp), tint = GramGreenPrimary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(order.customerPhone, style = MaterialTheme.typography.bodyMedium)
                        }
                        Row(verticalAlignment = Alignment.Top) {
                            Icon(Icons.Rounded.LocationOn, contentDescription = null, modifier = Modifier.size(16.dp), tint = GramGreenPrimary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(order.deliveryAddress, style = MaterialTheme.typography.bodyMedium)
                        }
                        if (order.notes.isNotBlank()) {
                            Row(verticalAlignment = Alignment.Top) {
                                Icon(Icons.Rounded.ReceiptLong, contentDescription = null, modifier = Modifier.size(16.dp), tint = GramOrangeDark)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("${strings.orderNotesLabel}: ${order.notes}", style = MaterialTheme.typography.bodySmall, color = GramOrangeDark)
                            }
                        }
                    }
                }

                // Itemized Products
                Text(
                    text = strings.itemsCount(orderWithItems.items.sumOf { it.quantity }),
                    style = MaterialTheme.typography.titleSmall
                )
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    orderWithItems.items.forEach { item ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            ProductArtwork(
                                imageKey = item.imageKey,
                                categoryId = item.categoryId,
                                contentDescription = item.displayName(language),
                                modifier = Modifier.size(44.dp)
                            )
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = item.displayName(language),
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "${item.quantity} x ₹${item.unitPriceInr} (${if (language == AppLanguage.HI && item.unitHi.isNotBlank()) item.unitHi else item.unitEn})",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = GramTextSecondary
                                )
                            }
                            Text(
                                text = "₹${item.unitPriceInr * item.quantity}",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = GramGreenDark
                            )
                        }
                    }
                }

                SubtleDivider(modifier = Modifier.fillMaxWidth().height(1.dp))

                // Bill Breakdown
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(strings.itemSubtotal, style = MaterialTheme.typography.bodyMedium, color = GramTextSecondary)
                        Text("₹${order.subtotalInr}", style = MaterialTheme.typography.bodyMedium)
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(strings.deliveryFee, style = MaterialTheme.typography.bodyMedium, color = GramTextSecondary)
                        Text(
                            text = if (order.deliveryFeeInr == 0) strings.freeLabel else "₹${order.deliveryFeeInr}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (order.deliveryFeeInr == 0) GramGreenPrimary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(strings.grandTotal, style = MaterialTheme.typography.titleMedium)
                        Text(
                            text = "₹${order.totalInr}",
                            style = MaterialTheme.typography.titleLarge,
                            color = GramGreenDark,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }

                SubtleDivider(modifier = Modifier.fillMaxWidth().height(1.dp))

                // Direct Status Update Controls inside Details Dialog
                Text(strings.updateStatusLabel, style = MaterialTheme.typography.titleSmall)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(OrderStatus.entries) { option ->
                        FilterChip(
                            selected = status == option,
                            onClick = { onUpdateStatus(option) },
                            label = { Text(option.label(language)) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = GramGreenContainer,
                                selectedLabelColor = GramGreenDark
                            ),
                            modifier = Modifier.testTag("details_status_${option.id.lowercase()}")
                        )
                    }
                }

                if (status == OrderStatus.PENDING) {
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Button(
                            onClick = { onUpdateStatus(OrderStatus.CONFIRMED) },
                            colors = ButtonDefaults.buttonColors(containerColor = GramGreenPrimary),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Rounded.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(strings.acceptOrder)
                        }
                        OutlinedButton(
                            onClick = { onUpdateStatus(OrderStatus.CANCELLED) },
                            border = BorderStroke(1.dp, GramError),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = GramError),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Rounded.Close, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(strings.rejectOrder)
                        }
                    }
                }

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = GramGreenDark),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(strings.closeBtn, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
