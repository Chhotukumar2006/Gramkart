package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.data.model.ProductCategory
import com.example.ui.theme.GramGreenDark
import com.example.ui.theme.GramGreenPrimary
import com.example.ui.theme.GramOrangeAccent

val PRESET_ARTWORK_KEYS = listOf(
    "rice", "atta", "dal", "oil",
    "tomato", "potato", "onion", "spinach",
    "mango", "apple", "banana",
    "milk", "paneer", "curd",
    "medicine", "bandage", "sanitizer",
    "detergent", "dishwash", "soap"
)

@Composable
fun GramKartLogoMark(
    modifier: Modifier = Modifier,
    size: Dp = 38.dp
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(12.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(GramGreenPrimary, GramGreenDark)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize().padding(6.dp)) {
            val w = this.size.width
            val h = this.size.height

            // Orange sprouting leaf at top-center
            val leafPath = Path().apply {
                moveTo(w * 0.44f, h * 0.44f)
                quadraticTo(w * 0.36f, h * 0.14f, w * 0.68f, h * 0.08f)
                quadraticTo(w * 0.72f, h * 0.38f, w * 0.44f, h * 0.44f)
                close()
            }
            drawPath(leafPath, color = GramOrangeAccent)

            // Basket handle
            drawLine(
                color = Color.White,
                start = Offset(w * 0.22f, h * 0.28f),
                end = Offset(w * 0.34f, h * 0.46f),
                strokeWidth = w * 0.09f,
                cap = StrokeCap.Round
            )
            drawLine(
                color = Color.White,
                start = Offset(w * 0.78f, h * 0.28f),
                end = Offset(w * 0.66f, h * 0.46f),
                strokeWidth = w * 0.09f,
                cap = StrokeCap.Round
            )

            // Basket rim
            drawLine(
                color = Color.White,
                start = Offset(w * 0.12f, h * 0.48f),
                end = Offset(w * 0.88f, h * 0.48f),
                strokeWidth = w * 0.1f,
                cap = StrokeCap.Round
            )

            // Basket woven body
            val bodyPath = Path().apply {
                moveTo(w * 0.18f, h * 0.50f)
                lineTo(w * 0.27f, h * 0.88f)
                lineTo(w * 0.73f, h * 0.88f)
                lineTo(w * 0.82f, h * 0.50f)
                close()
            }
            drawPath(
                path = bodyPath,
                color = Color.White,
                style = Stroke(width = w * 0.09f, cap = StrokeCap.Round)
            )
            drawLine(
                color = Color.White.copy(alpha = 0.85f),
                start = Offset(w * 0.23f, h * 0.68f),
                end = Offset(w * 0.77f, h * 0.68f),
                strokeWidth = w * 0.07f
            )
            drawLine(
                color = Color.White.copy(alpha = 0.85f),
                start = Offset(w * 0.42f, h * 0.52f),
                end = Offset(w * 0.44f, h * 0.86f),
                strokeWidth = w * 0.07f
            )
            drawLine(
                color = Color.White.copy(alpha = 0.85f),
                start = Offset(w * 0.58f, h * 0.52f),
                end = Offset(w * 0.56f, h * 0.86f),
                strokeWidth = w * 0.07f
            )
        }
    }
}

@Composable
fun ProductArtwork(
    imageKey: String,
    categoryId: String,
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    val category = ProductCategory.fromId(categoryId)
    val isCustomUri = imageKey.startsWith("content://") || imageKey.startsWith("file://")

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(
                Brush.radialGradient(
                    colors = listOf(Color.White, category.bgTint.copy(alpha = 0.75f))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        if (isCustomUri) {
            AsyncImage(
                model = imageKey,
                contentDescription = contentDescription,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            Canvas(modifier = Modifier.fillMaxSize().padding(12.dp)) {
                val w = size.width
                val h = size.height
                when (imageKey) {
                    "tomato" -> {
                        drawCircle(
                            brush = Brush.radialGradient(
                                colors = listOf(Color(0xFFFF6B6B), Color(0xFFDC2626)),
                                center = Offset(w * 0.42f, h * 0.48f),
                                radius = w * 0.45f
                            ),
                            radius = w * 0.34f,
                            center = Offset(w * 0.5f, h * 0.56f)
                        )
                        drawCircle(
                            color = Color.White.copy(alpha = 0.4f),
                            radius = w * 0.08f,
                            center = Offset(w * 0.37f, h * 0.43f)
                        )
                        val stem = Path().apply {
                            moveTo(w * 0.5f, h * 0.24f)
                            lineTo(w * 0.35f, h * 0.18f)
                            lineTo(w * 0.48f, h * 0.29f)
                            lineTo(w * 0.66f, h * 0.20f)
                            close()
                        }
                        drawPath(stem, color = Color(0xFF15803D))
                    }
                    "potato" -> {
                        drawOval(
                            brush = Brush.linearGradient(
                                listOf(Color(0xFFE5C08D), Color(0xFFB4844C))
                            ),
                            topLeft = Offset(w * 0.14f, h * 0.28f),
                            size = Size(w * 0.62f, h * 0.46f)
                        )
                        drawOval(
                            brush = Brush.linearGradient(
                                listOf(Color(0xFFD9AE73), Color(0xFFA0713C))
                            ),
                            topLeft = Offset(w * 0.34f, h * 0.45f),
                            size = Size(w * 0.52f, h * 0.38f)
                        )
                        drawCircle(Color(0xFF78512A), radius = w * 0.025f, center = Offset(w * 0.40f, h * 0.46f))
                        drawCircle(Color(0xFF78512A), radius = w * 0.02f, center = Offset(w * 0.65f, h * 0.62f))
                    }
                    "onion" -> {
                        drawCircle(
                            brush = Brush.radialGradient(
                                listOf(Color(0xFFD946EF), Color(0xFF86198F))
                            ),
                            radius = w * 0.33f,
                            center = Offset(w * 0.5f, h * 0.58f)
                        )
                        val tip = Path().apply {
                            moveTo(w * 0.42f, h * 0.28f)
                            lineTo(w * 0.5f, h * 0.12f)
                            lineTo(w * 0.58f, h * 0.28f)
                            close()
                        }
                        drawPath(tip, color = Color(0xFFA21CAF))
                        drawArc(
                            color = Color.White.copy(alpha = 0.3f),
                            startAngle = 140f,
                            sweepAngle = 80f,
                            useCenter = false,
                            topLeft = Offset(w * 0.26f, h * 0.34f),
                            size = Size(w * 0.48f, h * 0.48f),
                            style = Stroke(width = w * 0.03f)
                        )
                    }
                    "spinach" -> {
                        val leaf1 = Path().apply {
                            moveTo(w * 0.5f, h * 0.82f)
                            quadraticTo(w * 0.15f, h * 0.45f, w * 0.45f, h * 0.14f)
                            quadraticTo(w * 0.80f, h * 0.45f, w * 0.5f, h * 0.82f)
                        }
                        drawPath(leaf1, color = Color(0xFF16A34A))
                        val leaf2 = Path().apply {
                            moveTo(w * 0.52f, h * 0.84f)
                            quadraticTo(w * 0.85f, h * 0.55f, w * 0.72f, h * 0.26f)
                            quadraticTo(w * 0.42f, h * 0.52f, w * 0.52f, h * 0.84f)
                        }
                        drawPath(leaf2, color = Color(0xFF15803D))
                        drawLine(
                            Color(0xFFBBF7D0),
                            Offset(w * 0.5f, h * 0.84f),
                            Offset(w * 0.46f, h * 0.28f),
                            strokeWidth = w * 0.03f
                        )
                    }
                    "mango" -> {
                        val mango = Path().apply {
                            moveTo(w * 0.52f, h * 0.20f)
                            cubicTo(w * 0.85f, h * 0.22f, w * 0.82f, h * 0.72f, w * 0.46f, h * 0.84f)
                            cubicTo(w * 0.18f, h * 0.88f, w * 0.18f, h * 0.42f, w * 0.52f, h * 0.20f)
                        }
                        drawPath(
                            mango,
                            brush = Brush.linearGradient(
                                listOf(Color(0xFFFBBF24), Color(0xFFF97316))
                            )
                        )
                        val leaf = Path().apply {
                            moveTo(w * 0.52f, h * 0.20f)
                            quadraticTo(w * 0.68f, h * 0.06f, w * 0.78f, h * 0.18f)
                            quadraticTo(w * 0.62f, h * 0.26f, w * 0.52f, h * 0.20f)
                        }
                        drawPath(leaf, color = Color(0xFF15803D))
                    }
                    "apple" -> {
                        drawCircle(
                            brush = Brush.radialGradient(
                                listOf(Color(0xFFF87171), Color(0xFFB91C1C)),
                                center = Offset(w * 0.38f, h * 0.46f)
                            ),
                            radius = w * 0.34f,
                            center = Offset(w * 0.5f, h * 0.58f)
                        )
                        drawLine(
                            Color(0xFF78350F),
                            Offset(w * 0.5f, h * 0.26f),
                            Offset(w * 0.52f, h * 0.12f),
                            strokeWidth = w * 0.06f,
                            cap = StrokeCap.Round
                        )
                        val leaf = Path().apply {
                            moveTo(w * 0.52f, h * 0.18f)
                            quadraticTo(w * 0.68f, h * 0.10f, w * 0.72f, h * 0.22f)
                            quadraticTo(w * 0.60f, h * 0.26f, w * 0.52f, h * 0.18f)
                        }
                        drawPath(leaf, color = Color(0xFF16A34A))
                    }
                    "banana" -> {
                        val banana1 = Path().apply {
                            moveTo(w * 0.20f, h * 0.62f)
                            quadraticTo(w * 0.50f, h * 0.88f, w * 0.82f, h * 0.34f)
                            quadraticTo(w * 0.66f, h * 0.66f, w * 0.20f, h * 0.62f)
                        }
                        drawPath(banana1, color = Color(0xFFFACC15))
                        val banana2 = Path().apply {
                            moveTo(w * 0.22f, h * 0.48f)
                            quadraticTo(w * 0.48f, h * 0.72f, w * 0.76f, h * 0.24f)
                            quadraticTo(w * 0.62f, h * 0.50f, w * 0.22f, h * 0.48f)
                        }
                        drawPath(banana2, color = Color(0xFFEAB308))
                        drawCircle(Color(0xFF713F12), radius = w * 0.04f, center = Offset(w * 0.79f, h * 0.29f))
                    }
                    "milk" -> {
                        drawRoundRect(
                            color = Color(0xFF0284C7),
                            topLeft = Offset(w * 0.30f, h * 0.22f),
                            size = Size(w * 0.40f, h * 0.64f),
                            cornerRadius = CornerRadius(w * 0.08f)
                        )
                        drawRect(
                            color = Color.White,
                            topLeft = Offset(w * 0.32f, h * 0.38f),
                            size = Size(w * 0.36f, h * 0.34f)
                        )
                        drawRoundRect(
                            color = Color(0xFF0369A1),
                            topLeft = Offset(w * 0.38f, h * 0.13f),
                            size = Size(w * 0.24f, h * 0.11f),
                            cornerRadius = CornerRadius(w * 0.04f)
                        )
                        drawCircle(Color(0xFF38BDF8), radius = w * 0.08f, center = Offset(w * 0.50f, h * 0.55f))
                    }
                    "paneer" -> {
                        drawRoundRect(
                            color = Color(0xFFFDFBF7),
                            topLeft = Offset(w * 0.20f, h * 0.34f),
                            size = Size(w * 0.52f, h * 0.40f),
                            cornerRadius = CornerRadius(w * 0.06f)
                        )
                        drawRoundRect(
                            color = Color(0xFFCBD5E1),
                            topLeft = Offset(w * 0.20f, h * 0.34f),
                            size = Size(w * 0.52f, h * 0.40f),
                            cornerRadius = CornerRadius(w * 0.06f),
                            style = Stroke(width = w * 0.03f)
                        )
                        drawRoundRect(
                            color = Color(0xFFFEF3C7),
                            topLeft = Offset(w * 0.44f, h * 0.48f),
                            size = Size(w * 0.36f, h * 0.30f),
                            cornerRadius = CornerRadius(w * 0.05f)
                        )
                    }
                    "curd" -> {
                        val cup = Path().apply {
                            moveTo(w * 0.24f, h * 0.34f)
                            lineTo(w * 0.76f, h * 0.34f)
                            lineTo(w * 0.68f, h * 0.82f)
                            lineTo(w * 0.32f, h * 0.82f)
                            close()
                        }
                        drawPath(cup, color = Color(0xFF38BDF8))
                        drawRoundRect(
                            color = Color(0xFF0284C7),
                            topLeft = Offset(w * 0.20f, h * 0.26f),
                            size = Size(w * 0.60f, h * 0.10f),
                            cornerRadius = CornerRadius(w * 0.04f)
                        )
                        drawCircle(Color.White, radius = w * 0.12f, center = Offset(w * 0.50f, h * 0.58f))
                    }
                    "medicine" -> {
                        drawRoundRect(
                            color = Color(0xFFE2E8F0),
                            topLeft = Offset(w * 0.24f, h * 0.16f),
                            size = Size(w * 0.52f, h * 0.68f),
                            cornerRadius = CornerRadius(w * 0.08f)
                        )
                        for (row in 0..2) {
                            for (col in 0..1) {
                                val cx = w * (0.38f + col * 0.24f)
                                val cy = h * (0.30f + row * 0.19f)
                                drawCircle(Color(0xFFDC2626), radius = w * 0.07f, center = Offset(cx, cy))
                            }
                        }
                    }
                    "bandage" -> {
                        drawRoundRect(
                            color = Color(0xFFFDE68A),
                            topLeft = Offset(w * 0.14f, h * 0.36f),
                            size = Size(w * 0.72f, h * 0.28f),
                            cornerRadius = CornerRadius(w * 0.14f)
                        )
                        drawRoundRect(
                            color = Color.White,
                            topLeft = Offset(w * 0.38f, h * 0.38f),
                            size = Size(w * 0.24f, h * 0.24f),
                            cornerRadius = CornerRadius(w * 0.04f)
                        )
                        drawLine(Color(0xFFDC2626), Offset(w * 0.5f, h * 0.43f), Offset(w * 0.5f, h * 0.57f), strokeWidth = w * 0.045f)
                        drawLine(Color(0xFFDC2626), Offset(w * 0.43f, h * 0.5f), Offset(w * 0.57f, h * 0.5f), strokeWidth = w * 0.045f)
                    }
                    "sanitizer" -> {
                        drawRoundRect(
                            color = Color(0xFF2DD4BF),
                            topLeft = Offset(w * 0.30f, h * 0.32f),
                            size = Size(w * 0.40f, h * 0.54f),
                            cornerRadius = CornerRadius(w * 0.10f)
                        )
                        drawRect(Color(0xFF0F766E), Offset(w * 0.44f, h * 0.18f), Size(w * 0.12f, h * 0.14f))
                        drawRoundRect(Color(0xFF0F766E), Offset(w * 0.38f, h * 0.14f), Size(w * 0.26f, h * 0.06f), CornerRadius(w * 0.03f))
                        drawCircle(Color.White, radius = w * 0.11f, center = Offset(w * 0.5f, h * 0.58f))
                    }
                    "detergent", "dishwash", "soap" -> {
                        val primary = when (imageKey) {
                            "detergent" -> Color(0xFF2563EB)
                            "dishwash" -> Color(0xFF84CC16)
                            else -> Color(0xFF16A34A)
                        }
                        drawRoundRect(
                            color = primary,
                            topLeft = Offset(w * 0.24f, h * 0.22f),
                            size = Size(w * 0.52f, h * 0.62f),
                            cornerRadius = CornerRadius(w * 0.10f)
                        )
                        drawCircle(Color.White.copy(alpha = 0.85f), radius = w * 0.14f, center = Offset(w * 0.5f, h * 0.52f))
                        drawCircle(Color(0xFFFACC15), radius = w * 0.07f, center = Offset(w * 0.68f, h * 0.26f))
                    }
                    else -> {
                        // Rice, Atta, Dal, Oil staples
                        val bagColor = when (imageKey) {
                            "rice" -> Color(0xFFD97706)
                            "atta" -> Color(0xFFCA8A04)
                            "dal" -> Color(0xFFF59E0B)
                            else -> Color(0xFFEAB308)
                        }
                        drawRoundRect(
                            color = bagColor,
                            topLeft = Offset(w * 0.24f, h * 0.22f),
                            size = Size(w * 0.52f, h * 0.64f),
                            cornerRadius = CornerRadius(w * 0.10f)
                        )
                        drawRoundRect(
                            color = Color.White.copy(alpha = 0.9f),
                            topLeft = Offset(w * 0.30f, h * 0.34f),
                            size = Size(w * 0.40f, h * 0.34f),
                            cornerRadius = CornerRadius(w * 0.06f)
                        )
                        drawCircle(
                            color = GramGreenPrimary,
                            radius = w * 0.08f,
                            center = Offset(w * 0.50f, h * 0.51f)
                        )
                    }
                }
            }
        }
    }
}
