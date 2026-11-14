package com.example.ui.customer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AdminPanelSettings
import androidx.compose.material.icons.rounded.Apps
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.ShoppingBag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.local.ProductEntity
import com.example.data.model.CartSummary
import com.example.data.model.ProductCategory
import com.example.ui.components.DemoNoticeStrip
import com.example.ui.components.GramKartLogoMark
import com.example.ui.components.LanguageTogglePill
import com.example.ui.components.ProductArtwork
import com.example.ui.components.QuantityStepper
import com.example.ui.components.categoryIcon
import com.example.ui.i18n.AppLanguage
import com.example.ui.i18n.AppStrings
import com.example.ui.theme.GramBorder
import com.example.ui.theme.GramError
import com.example.ui.theme.GramErrorContainer
import com.example.ui.theme.GramGreenContainer
import com.example.ui.theme.GramGreenDark
import com.example.ui.theme.GramGreenPrimary
import com.example.ui.theme.GramGreenSoft
import com.example.ui.theme.GramOrangeAccent
import com.example.ui.theme.GramOrangeContainer
import com.example.ui.theme.GramOrangeDark
import com.example.ui.theme.GramTextMuted
import com.example.ui.theme.GramTextSecondary
import com.example.ui.theme.GramWhite
import kotlinx.coroutines.launch

@Composable
fun CustomerHomeScreen(
    products: List<ProductEntity>,
    cartQuantities: Map<Long, Int>,
    cartSummary: CartSummary,
    searchQuery: String,
    selectedCategory: ProductCategory?,
    language: AppLanguage,
    strings: AppStrings,
    onSearchChange: (String) -> Unit,
    onSelectCategory: (ProductCategory?) -> Unit,
    onClearFilters: () -> Unit,
    onAddToCart: (Long) -> Unit,
    onDecrementCart: (Long) -> Unit,
    onToggleLanguage: () -> Unit,
    onOpenAdmin: () -> Unit,
    onOpenCart: () -> Unit
) {
    val gridState = rememberLazyGridState()
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Pinned Brand Header + Real-Time Search Bar at Top of Product List
            Surface(
                color = MaterialTheme.colorScheme.background,
                shadowElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            GramKartLogoMark(size = 40.dp)
                            Column {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = strings.appName,
                                        style = MaterialTheme.typography.headlineSmall,
                                        color = GramGreenDark,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = GramOrangeContainer
                                    ) {
                                        Text(
                                            text = strings.demoBadge,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = GramOrangeDark,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                                Text(
                                    text = "₹ INR • Local Demo",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = GramTextSecondary
                                )
                            }
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            LanguageTogglePill(
                                language = language,
                                onToggle = onToggleLanguage
                            )
                            Surface(
                                onClick = onOpenAdmin,
                                shape = RoundedCornerShape(50),
                                color = GramWhite,
                                border = BorderStroke(1.dp, GramBorder),
                                modifier = Modifier.testTag("header_admin_button")
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.AdminPanelSettings,
                                        contentDescription = strings.adminBtn,
                                        tint = GramGreenPrimary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = strings.adminBtn,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = GramGreenDark
                                    )
                                }
                            }
                        }
                    }

                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = onSearchChange,
                        placeholder = {
                            Text(
                                text = strings.searchPlaceholder,
                                style = MaterialTheme.typography.bodyMedium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Rounded.Search,
                                contentDescription = null,
                                tint = GramGreenPrimary
                            )
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(
                                    onClick = { onSearchChange("") },
                                    modifier = Modifier.testTag("home_search_clear")
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Clear,
                                        contentDescription = "Clear search",
                                        tint = GramTextSecondary
                                    )
                                }
                            }
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = GramWhite,
                            unfocusedContainerColor = GramWhite,
                            focusedBorderColor = GramGreenPrimary,
                            unfocusedBorderColor = GramBorder
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("home_search_field")
                    )
                }
            }
            HorizontalDivider(thickness = 1.dp, color = GramBorder.copy(alpha = 0.6f))

            BoxWithConstraints(modifier = Modifier.weight(1f).fillMaxWidth()) {
                val columns = when {
                    maxWidth >= 840.dp -> 4
                    maxWidth >= 600.dp -> 3
                    else -> 2
                }

                LazyVerticalGrid(
                    state = gridState,
                    columns = GridCells.Fixed(columns),
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        top = 10.dp,
                        bottom = if (cartSummary.totalCount > 0) 92.dp else 20.dp
                    ),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("customer_product_grid")
                ) {
                    item(span = { GridItemSpan(columns) }) {
                        DemoNoticeStrip(
                            text = strings.demoBannerNotice,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    // Hero Banner (hide when actively searching so results appear immediately)
                    if (searchQuery.isBlank() && selectedCategory == null) {
                        item(span = { GridItemSpan(columns) }) {
                            HeroGroceryBanner(
                                strings = strings,
                                onShopNow = {
                                    scope.launch { gridState.animateScrollToItem(2) }
                                }
                            )
                        }
                    }

                // Category Section Header & Chips
                item(span = { GridItemSpan(columns) }) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = strings.categoriesTitle,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            item {
                                CategoryChipCard(
                                    label = strings.allCategory,
                                    selected = selectedCategory == null,
                                    tint = GramGreenPrimary,
                                    bgTint = GramGreenContainer,
                                    icon = Icons.Rounded.Apps,
                                    onClick = { onSelectCategory(null) },
                                    testTag = "category_all"
                                )
                            }
                            items(ProductCategory.entries) { cat ->
                                CategoryChipCard(
                                    label = cat.label(language),
                                    selected = selectedCategory == cat,
                                    tint = cat.tintColor,
                                    bgTint = cat.bgTint,
                                    icon = categoryIcon(cat),
                                    onClick = {
                                        onSelectCategory(if (selectedCategory == cat) null else cat)
                                    },
                                    testTag = "category_${cat.id.lowercase()}"
                                )
                            }
                        }
                    }
                }

                // Products Section Header
                item(span = { GridItemSpan(columns) }) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = selectedCategory?.label(language) ?: strings.popularProducts,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = strings.itemsCount(products.size),
                            style = MaterialTheme.typography.labelMedium,
                            color = GramTextSecondary
                        )
                    }
                }

                // Empty Filter State or Product Cards
                if (products.isEmpty()) {
                    item(span = { GridItemSpan(columns) }) {
                        Surface(
                            shape = RoundedCornerShape(18.dp),
                            color = GramWhite,
                            border = BorderStroke(1.dp, GramBorder),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(28.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Search,
                                    contentDescription = null,
                                    tint = GramGreenPrimary,
                                    modifier = Modifier.size(40.dp)
                                )
                                Text(
                                    text = strings.noProductsFound,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                OutlinedButton(
                                    onClick = onClearFilters,
                                    border = BorderStroke(1.dp, GramGreenPrimary)
                                ) {
                                    Text(strings.clearFilters, color = GramGreenPrimary)
                                }
                            }
                        }
                    }
                } else {
                    items(products, key = { it.id }) { product ->
                        val qtyInCart = cartQuantities[product.id] ?: 0
                        ProductCard(
                            product = product,
                            qtyInCart = qtyInCart,
                            language = language,
                            strings = strings,
                            onAdd = { onAddToCart(product.id) },
                            onDecrement = { onDecrementCart(product.id) }
                        )
                    }
                }
            }
        }
        }

        // Floating Mini Cart Bar above Bottom Nav
        AnimatedVisibility(
            visible = cartSummary.totalCount > 0,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Surface(
                onClick = onOpenCart,
                shape = RoundedCornerShape(16.dp),
                color = GramGreenPrimary,
                shadowElevation = 8.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("floating_cart_bar")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(GramWhite.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.ShoppingBag,
                                contentDescription = null,
                                tint = GramWhite,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Column {
                            Text(
                                text = strings.itemsLabel(cartSummary.totalCount),
                                style = MaterialTheme.typography.labelMedium,
                                color = GramWhite.copy(alpha = 0.9f)
                            )
                            Text(
                                text = "₹${cartSummary.grandTotalInr}",
                                style = MaterialTheme.typography.titleMedium,
                                color = GramWhite,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = GramOrangeAccent
                    ) {
                        Text(
                            text = "${strings.viewCart} →",
                            style = MaterialTheme.typography.labelLarge,
                            color = GramWhite,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HeroGroceryBanner(
    strings: AppStrings,
    onShopNow: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(22.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(176.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.img_hero_groceries),
                contentDescription = "Fresh groceries hero banner",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                GramGreenDark.copy(alpha = 0.92f),
                                GramGreenPrimary.copy(alpha = 0.78f),
                                Color.Transparent
                            )
                        )
                    )
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(18.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Surface(
                    shape = RoundedCornerShape(50),
                    color = GramOrangeAccent
                ) {
                    Text(
                        text = strings.heroBadge,
                        style = MaterialTheme.typography.labelSmall,
                        color = GramWhite,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = strings.heroTitle,
                        style = MaterialTheme.typography.titleLarge.copy(lineHeight = 22.sp),
                        color = GramWhite,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = strings.heroSubtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = GramWhite.copy(alpha = 0.9f),
                        maxLines = 2
                    )
                }

                Button(
                    onClick = onShopNow,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GramWhite,
                        contentColor = GramGreenDark
                    ),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
                    modifier = Modifier.height(34.dp)
                ) {
                    Text(
                        text = strings.shopFreshCta,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun CategoryChipCard(
    label: String,
    selected: Boolean,
    tint: Color,
    bgTint: Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit,
    testTag: String
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = if (selected) GramGreenPrimary else GramWhite,
        border = BorderStroke(
            width = 1.dp,
            color = if (selected) GramGreenPrimary else GramBorder
        ),
        shadowElevation = if (selected) 3.dp else 0.dp,
        modifier = Modifier.testTag(testTag)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(if (selected) GramWhite.copy(alpha = 0.22f) else bgTint),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = if (selected) GramWhite else tint,
                    modifier = Modifier.size(16.dp)
                )
            }
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = if (selected) GramWhite else MaterialTheme.colorScheme.onSurface,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun ProductCard(
    product: ProductEntity,
    qtyInCart: Int,
    language: AppLanguage,
    strings: AppStrings,
    onAdd: () -> Unit,
    onDecrement: () -> Unit
) {
    val outOfStock = product.stock <= 0
    val lowStock = product.stock in 1..5
    val discount = product.discountPercent

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = GramWhite),
        border = BorderStroke(1.dp, GramBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("product_card_${product.id}")
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
            ) {
                ProductArtwork(
                    imageKey = product.imageKey,
                    categoryId = product.category,
                    contentDescription = product.displayName(language),
                    modifier = Modifier.fillMaxSize()
                )
                if (discount > 0 && !outOfStock) {
                    Surface(
                        shape = RoundedCornerShape(topStart = 12.dp, bottomEnd = 10.dp),
                        color = GramOrangeAccent,
                        modifier = Modifier.align(Alignment.TopStart)
                    ) {
                        Text(
                            text = "$discount% OFF",
                            style = MaterialTheme.typography.labelSmall,
                            color = GramWhite,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                        )
                    }
                }
                if (outOfStock || lowStock) {
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = if (outOfStock) GramErrorContainer else GramOrangeContainer,
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(6.dp)
                    ) {
                        Text(
                            text = if (outOfStock) strings.outOfStock else strings.onlyLeft(product.stock),
                            style = MaterialTheme.typography.labelSmall,
                            color = if (outOfStock) GramError else GramOrangeDark,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = product.displayName(language),
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 2,
                    minLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = product.displayUnit(language),
                    style = MaterialTheme.typography.bodySmall,
                    color = GramTextSecondary
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "₹${product.priceInr}",
                        style = MaterialTheme.typography.titleMedium,
                        color = GramGreenDark,
                        fontWeight = FontWeight.ExtraBold
                    )
                    if (product.mrpInr > product.priceInr) {
                        Text(
                            text = "₹${product.mrpInr}",
                            style = MaterialTheme.typography.bodySmall.copy(
                                textDecoration = TextDecoration.LineThrough
                            ),
                            color = GramTextMuted
                        )
                    }
                }

                if (outOfStock) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = GramErrorContainer.copy(alpha = 0.6f)
                    ) {
                        Text(
                            text = strings.outOfStock,
                            style = MaterialTheme.typography.labelSmall,
                            color = GramError,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
                        )
                    }
                } else if (qtyInCart > 0) {
                    QuantityStepper(
                        quantity = qtyInCart,
                        canIncrement = qtyInCart < product.stock,
                        onDecrement = onDecrement,
                        onIncrement = onAdd
                    )
                } else {
                    OutlinedButton(
                        onClick = onAdd,
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.5.dp, GramGreenPrimary),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = GramGreenSoft,
                            contentColor = GramGreenPrimary
                        ),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 0.dp),
                        modifier = Modifier
                            .height(34.dp)
                            .testTag("add_to_cart_${product.id}")
                    ) {
                        Text(
                            text = strings.addBtn,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }
        }
    }
}
