package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Eco
import androidx.compose.material.icons.rounded.LocalDrink
import androidx.compose.material.icons.rounded.LocalShipping
import androidx.compose.material.icons.rounded.MedicalServices
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material.icons.rounded.ShoppingBasket
import androidx.compose.material.icons.rounded.CleaningServices
import androidx.compose.material.icons.rounded.Inventory2
import androidx.compose.material.icons.rounded.LocalFlorist
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.model.OrderStatus
import com.example.data.model.ProductCategory
import com.example.ui.i18n.AppLanguage
import com.example.ui.theme.GramBorder
import com.example.ui.theme.GramError
import com.example.ui.theme.GramErrorContainer
import com.example.ui.theme.GramGreenContainer
import com.example.ui.theme.GramGreenDark
import com.example.ui.theme.GramGreenPrimary
import com.example.ui.theme.GramInfo
import com.example.ui.theme.GramInfoContainer
import com.example.ui.theme.GramOrangeContainer
import com.example.ui.theme.GramOrangeDark
import com.example.ui.theme.GramPurple
import com.example.ui.theme.GramPurpleContainer
import com.example.ui.theme.GramWhite
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun categoryIcon(category: ProductCategory): ImageVector = when (category) {
    ProductCategory.GROCERY -> Icons.Rounded.ShoppingBasket
    ProductCategory.VEGETABLES -> Icons.Rounded.Eco
    ProductCategory.FRUITS -> Icons.Rounded.LocalFlorist
    ProductCategory.DAIRY -> Icons.Rounded.LocalDrink
    ProductCategory.MEDICAL -> Icons.Rounded.MedicalServices
    ProductCategory.HOUSEHOLD -> Icons.Rounded.CleaningServices
}

@Composable
fun LanguageTogglePill(
    language: AppLanguage,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onToggle,
        shape = RoundedCornerShape(50),
        color = GramGreenContainer,
        border = BorderStroke(1.dp, GramGreenPrimary.copy(alpha = 0.25f)),
        modifier = modifier.testTag("language_toggle_button")
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "EN",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = if (language == AppLanguage.EN) FontWeight.Bold else FontWeight.Medium,
                color = if (language == AppLanguage.EN) GramGreenDark else GramGreenDark.copy(alpha = 0.55f)
            )
            Text(
                text = "/",
                style = MaterialTheme.typography.labelSmall,
                color = GramGreenDark.copy(alpha = 0.4f)
            )
            Text(
                text = "हिं",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = if (language == AppLanguage.HI) FontWeight.Bold else FontWeight.Medium,
                color = if (language == AppLanguage.HI) GramGreenDark else GramGreenDark.copy(alpha = 0.55f)
            )
        }
    }
}

@Composable
fun QuantityStepper(
    quantity: Int,
    canIncrement: Boolean,
    onDecrement: () -> Unit,
    onIncrement: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(GramGreenPrimary),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .clickable(onClick = onDecrement)
                .testTag("stepper_decrement"),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.Remove,
                contentDescription = "Decrease quantity",
                tint = GramWhite,
                modifier = Modifier.size(18.dp)
            )
        }
        Text(
            text = quantity.toString(),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = GramWhite,
            modifier = Modifier.padding(horizontal = 6.dp)
        )
        Box(
            modifier = Modifier
                .size(34.dp)
                .clickable(enabled = canIncrement, onClick = onIncrement)
                .testTag("stepper_increment"),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.Add,
                contentDescription = "Increase quantity",
                tint = if (canIncrement) GramWhite else GramWhite.copy(alpha = 0.4f),
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
fun OrderStatusChip(
    status: OrderStatus,
    language: AppLanguage,
    modifier: Modifier = Modifier
) {
    val (bgColor, fgColor, icon) = when (status) {
        OrderStatus.PENDING -> Triple(GramOrangeContainer, GramOrangeDark, Icons.Rounded.Schedule)
        OrderStatus.CONFIRMED -> Triple(GramInfoContainer, GramInfo, Icons.Rounded.CheckCircle)
        OrderStatus.PREPARING -> Triple(GramOrangeContainer, GramOrangeDark, Icons.Rounded.Inventory2)
        OrderStatus.OUT_FOR_DELIVERY -> Triple(GramPurpleContainer, GramPurple, Icons.Rounded.LocalShipping)
        OrderStatus.DELIVERED -> Triple(GramGreenContainer, GramGreenDark, Icons.Rounded.CheckCircle)
        OrderStatus.CANCELLED -> Triple(GramErrorContainer, GramError, Icons.Rounded.Close)
    }

    Surface(
        shape = RoundedCornerShape(50),
        color = bgColor,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = fgColor,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = status.label(language),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = fgColor
            )
        }
    }
}

@Composable
fun DemoNoticeStrip(
    text: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(GramOrangeContainer.copy(alpha = 0.75f))
            .border(1.dp, GramOrangeDark.copy(alpha = 0.2f), RoundedCornerShape(10.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(GramOrangeDark)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = GramOrangeDark,
            fontWeight = FontWeight.SemiBold
        )
    }
}

fun formatTimestamp(millis: Long): String {
    val sdf = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())
    return sdf.format(Date(millis))
}

@Composable
fun SubtleDivider(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(GramBorder.copy(alpha = 0.7f))
    )
}
