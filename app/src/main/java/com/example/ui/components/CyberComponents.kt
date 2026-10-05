package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.CyberGreen
import com.example.ui.theme.DeepSpaceBlue
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.ElectricCyan
import com.example.ui.theme.NeonYellow
import com.example.ui.theme.NeonYellowBright
import com.example.ui.theme.ObsidianBlack
import com.example.ui.theme.PrankMagenta
import com.example.ui.theme.TacticalAmber
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

/**
 * Animated PUBG / Cyberpunk tactical background with moving grid lines,
 * neon yellow & electric cyan ambient glow orbs, and subtle radar rings.
 */
@Composable
fun CyberGridBackground(
    accentColor: Color = NeonYellow,
    secondaryColor: Color = ElectricCyan,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "cyber_bg")
    val gridShift by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "grid_shift"
    )
    val orbPulse by infiniteTransition.animateFloat(
        initialValue = 0.75f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "orb_pulse"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        // Deep obsidian to dark space gradient
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    ObsidianBlack,
                    DeepSpaceBlue,
                    Color(0xFF080D1A),
                    ObsidianBlack
                )
            )
        )

        // Top-left Neon Yellow ambient glow
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    accentColor.copy(alpha = 0.18f),
                    accentColor.copy(alpha = 0.05f),
                    Color.Transparent
                ),
                center = Offset(w * 0.15f, h * 0.18f),
                radius = w * 0.65f * orbPulse
            ),
            center = Offset(w * 0.15f, h * 0.18f),
            radius = w * 0.65f * orbPulse
        )

        // Bottom-right Electric Cyan ambient glow
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    secondaryColor.copy(alpha = 0.18f),
                    secondaryColor.copy(alpha = 0.04f),
                    Color.Transparent
                ),
                center = Offset(w * 0.85f, h * 0.78f),
                radius = w * 0.7f * (1.9f - orbPulse)
            ),
            center = Offset(w * 0.85f, h * 0.78f),
            radius = w * 0.7f * (1.9f - orbPulse)
        )

        // Tactical grid lines
        val spacing = 48.dp.toPx()
        val offsetY = gridShift * spacing
        val gridLineColor = secondaryColor.copy(alpha = 0.07f)

        var x = 0f
        while (x <= w) {
            drawLine(
                color = gridLineColor,
                start = Offset(x, 0f),
                end = Offset(x, h),
                strokeWidth = 1f
            )
            x += spacing
        }

        var y = -spacing + offsetY
        while (y <= h) {
            if (y >= 0f) {
                drawLine(
                    color = gridLineColor,
                    start = Offset(0f, y),
                    end = Offset(w, y),
                    strokeWidth = 1f
                )
            }
            y += spacing
        }

        // Subtle tactical crosshair in center
        val center = Offset(w * 0.5f, h * 0.42f)
        drawCircle(
            color = accentColor.copy(alpha = 0.06f),
            radius = w * 0.38f,
            center = center,
            style = Stroke(width = 1.5f)
        )
        drawCircle(
            color = secondaryColor.copy(alpha = 0.05f),
            radius = w * 0.24f,
            center = center,
            style = Stroke(width = 1f)
        )
    }
}

/**
 * Glassmorphism gaming card with frosted translucent surface, specular sheen,
 * neon border, and PUBG tactical HUD corner brackets.
 */
@Composable
fun GlassmorphismCard(
    modifier: Modifier = Modifier,
    primaryBorderColor: Color = NeonYellow,
    secondaryBorderColor: Color = ElectricCyan,
    cornerRadius: Dp = 20.dp,
    content: @Composable BoxScope.() -> Unit
) {
    val shape = RoundedCornerShape(cornerRadius)
    Box(
        modifier = modifier
            .clip(shape)
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xE6141E36),
                        Color(0xD90C1222),
                        Color(0xE6111A30)
                    ),
                    start = Offset(0f, 0f),
                    end = Offset(900f, 1200f)
                )
            )
            .border(
                width = 1.5.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        primaryBorderColor.copy(alpha = 0.85f),
                        secondaryBorderColor.copy(alpha = 0.45f),
                        primaryBorderColor.copy(alpha = 0.25f),
                        secondaryBorderColor.copy(alpha = 0.85f)
                    )
                ),
                shape = shape
            )
            .drawWithContent {
                // Top glass specular highlight
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.09f),
                            Color.Transparent
                        ),
                        startY = 0f,
                        endY = size.height * 0.32f
                    )
                )

                drawContent()

                // Tactical HUD corner accents
                val bracketLen = 18.dp.toPx()
                val stroke = 3.dp.toPx()
                val pad = 6.dp.toPx()

                // Top-left bracket
                drawLine(
                    color = primaryBorderColor,
                    start = Offset(pad, pad + bracketLen),
                    end = Offset(pad, pad),
                    strokeWidth = stroke,
                    cap = StrokeCap.Round
                )
                drawLine(
                    color = primaryBorderColor,
                    start = Offset(pad, pad),
                    end = Offset(pad + bracketLen, pad),
                    strokeWidth = stroke,
                    cap = StrokeCap.Round
                )

                // Bottom-right bracket
                drawLine(
                    color = secondaryBorderColor,
                    start = Offset(size.width - pad, size.height - pad - bracketLen),
                    end = Offset(size.width - pad, size.height - pad),
                    strokeWidth = stroke,
                    cap = StrokeCap.Round
                )
                drawLine(
                    color = secondaryBorderColor,
                    start = Offset(size.width - pad - bracketLen, size.height - pad),
                    end = Offset(size.width - pad, size.height - pad),
                    strokeWidth = stroke,
                    cap = StrokeCap.Round
                )
            },
        content = content
    )
}

/**
 * Custom Tactical Radar & Hexagonal UC Spinner for Step 2a ("UC hisoblanmoqda...")
 */
@Composable
fun TacticalUcLoader(
    progress: Float,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "uc_loader")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1600, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "loader_rot"
    )
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.92f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 700, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "loader_pulse"
    )

    Box(
        modifier = modifier.size(168.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2f, size.height / 2f)
            val maxR = size.minDimension / 2f - 8.dp.toPx()

            // Outer glowing ring
            drawCircle(
                color = ElectricCyan.copy(alpha = 0.2f),
                radius = maxR,
                center = center,
                style = Stroke(width = 3.dp.toPx())
            )

            // Progress arc
            drawArc(
                brush = Brush.sweepGradient(
                    colors = listOf(
                        ElectricCyan,
                        NeonYellow,
                        NeonYellowBright,
                        ElectricCyan
                    )
                ),
                startAngle = -90f,
                sweepAngle = 360f * progress,
                useCenter = false,
                topLeft = Offset(center.x - maxR, center.y - maxR),
                size = Size(maxR * 2f, maxR * 2f),
                style = Stroke(width = 7.dp.toPx(), cap = StrokeCap.Round)
            )

            // Rotating inner tactical dashed ring
            rotate(degrees = rotation, pivot = center) {
                val innerR = maxR * 0.76f
                for (i in 0 until 6) {
                    drawArc(
                        color = NeonYellow.copy(alpha = 0.75f),
                        startAngle = i * 60f,
                        sweepAngle = 32f,
                        useCenter = false,
                        topLeft = Offset(center.x - innerR, center.y - innerR),
                        size = Size(innerR * 2f, innerR * 2f),
                        style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                    )
                }
            }

            // Counter-rotating cyber hexagon
            rotate(degrees = -rotation * 0.7f, pivot = center) {
                val hexR = maxR * 0.54f
                val path = Path()
                for (i in 0 until 6) {
                    val angleRad = (i * 60.0 - 30.0) * PI / 180.0
                    val px = center.x + (hexR * cos(angleRad)).toFloat()
                    val py = center.y + (hexR * sin(angleRad)).toFloat()
                    if (i == 0) path.moveTo(px, py) else path.lineTo(px, py)
                }
                path.close()
                drawPath(
                    path = path,
                    color = ElectricCyan.copy(alpha = 0.5f),
                    style = Stroke(width = 2.dp.toPx())
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.scale(pulse)
        ) {
            Text(
                text = "UC",
                style = MaterialTheme.typography.headlineMedium,
                color = NeonYellowBright,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                text = "${(progress * 100).roundToInt()}%",
                style = MaterialTheme.typography.titleLarge,
                color = ElectricCyan,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

/**
 * Rich UC Icon & Currency Graphic for Step 2b ("🎉 600 UC TUSHDI!").
 * Combines the generated 3D UC bundle illustration with a custom glowing
 * hexagonal UC emblem badge and animated energy rings.
 */
@Composable
fun UcRewardGraphicDisplay(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "uc_reward_graphic")
    val haloRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 6000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "halo_rot"
    )
    val floatOffset by infiniteTransition.animateFloat(
        initialValue = -6f,
        targetValue = 6f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "float_offset"
    )

    Box(
        modifier = modifier
            .size(210.dp)
            .offset { IntOffset(0, floatOffset.dp.roundToPx()) },
        contentAlignment = Alignment.Center
    ) {
        // Rotating neon yellow & cyan energy rays behind the UC graphic
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2f, size.height / 2f)
            val radius = size.minDimension / 2f

            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        NeonYellow.copy(alpha = 0.42f),
                        ElectricCyan.copy(alpha = 0.18f),
                        Color.Transparent
                    ),
                    center = center,
                    radius = radius
                ),
                center = center,
                radius = radius
            )

            rotate(degrees = haloRotation, pivot = center) {
                // Outer hexagonal golden frame
                val hexPath = Path()
                val hexR = radius * 0.92f
                for (i in 0 until 6) {
                    val rad = (i * 60.0) * PI / 180.0
                    val x = center.x + (hexR * cos(rad)).toFloat()
                    val y = center.y + (hexR * sin(rad)).toFloat()
                    if (i == 0) hexPath.moveTo(x, y) else hexPath.lineTo(x, y)
                }
                hexPath.close()
                drawPath(
                    path = hexPath,
                    brush = Brush.linearGradient(
                        colors = listOf(NeonYellowBright, ElectricCyan, TacticalAmber)
                    ),
                    style = Stroke(width = 3.5.dp.toPx())
                )
            }
        }

        // High-tech generated UC bundle artwork inside a hexagonal/rounded frame
        Surface(
            modifier = Modifier
                .size(152.dp)
                .border(
                    width = 2.5.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(NeonYellowBright, ElectricCyan, NeonYellow)
                    ),
                    shape = RoundedCornerShape(28.dp)
                ),
            shape = RoundedCornerShape(28.dp),
            color = ObsidianBlack,
            tonalElevation = 12.dp
        ) {
            Box(contentAlignment = Alignment.Center) {
                Image(
                    painter = painterResource(id = R.drawable.img_uc_bundle),
                    contentDescription = "600 UC Currency Graphic",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                // Bottom gradient overlay with custom vector UC banknote emblem
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Transparent,
                                    ObsidianBlack.copy(alpha = 0.85f)
                                )
                            )
                        )
                )
            }
        }

        // Floating Golden "+600 UC" Tactical Badge at the bottom of the graphic
        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = 6.dp),
            shape = CutCornerShape(topStart = 12.dp, bottomEnd = 12.dp, topEnd = 4.dp, bottomStart = 4.dp),
            color = NeonYellow,
            border = BorderStroke(1.5.dp, Color.White)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Mini custom UC coin icon
                Canvas(modifier = Modifier.size(18.dp)) {
                    drawCircle(color = ObsidianBlack)
                    drawCircle(
                        color = NeonYellowBright,
                        radius = size.minDimension * 0.34f,
                        style = Stroke(width = 2.dp.toPx())
                    )
                }
                Text(
                    text = "+600 UC CASH",
                    style = MaterialTheme.typography.labelLarge,
                    color = ObsidianBlack,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }
    }
}

private data class FloatingEmojiItem(
    val emoji: String,
    val xFraction: Float,
    val speedMultiplier: Float,
    val phaseOffset: Float,
    val sizeSp: Int
)

/**
 * Full-screen animated funny emoji shower for Step 3 ("😂 ALDANDIMMM!").
 */
@Composable
fun FunnyEmojiRainOverlay(
    modifier: Modifier = Modifier
) {
    val emojis = remember {
        listOf(
            FloatingEmojiItem("😂", 0.08f, 1.0f, 0.0f, 30),
            FloatingEmojiItem("🤣", 0.20f, 1.3f, 0.25f, 34),
            FloatingEmojiItem("😈", 0.33f, 0.9f, 0.6f, 28),
            FloatingEmojiItem("🤡", 0.47f, 1.15f, 0.15f, 32),
            FloatingEmojiItem("😎", 0.61f, 1.05f, 0.45f, 30),
            FloatingEmojiItem("😂", 0.74f, 1.25f, 0.75f, 36),
            FloatingEmojiItem("🎉", 0.86f, 0.95f, 0.35f, 28),
            FloatingEmojiItem("🤣", 0.93f, 1.1f, 0.85f, 32),
            FloatingEmojiItem("💸", 0.14f, 1.2f, 0.52f, 26),
            FloatingEmojiItem("🐓", 0.54f, 1.35f, 0.90f, 28),
            FloatingEmojiItem("😜", 0.80f, 1.0f, 0.10f, 30)
        )
    }

    val infiniteTransition = rememberInfiniteTransition(label = "emoji_rain")
    val masterProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "emoji_progress"
    )
    val wobble by infiniteTransition.animateFloat(
        initialValue = -12f,
        targetValue = 12f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "emoji_wobble"
    )

    androidx.compose.foundation.layout.BoxWithConstraints(
        modifier = modifier.fillMaxSize()
    ) {
        val maxW = maxWidth
        val maxH = maxHeight

        emojis.forEachIndexed { idx, item ->
            val rawY = (masterProgress * item.speedMultiplier + item.phaseOffset) % 1f
            val yPos = maxH * rawY
            val xWobble = if (idx % 2 == 0) wobble else -wobble
            val xPos = (maxW * item.xFraction) + xWobble.dp

            Text(
                text = item.emoji,
                fontSize = item.sizeSp.sp,
                modifier = Modifier
                    .offset(x = xPos, y = yPos)
            )
        }
    }
}

/**
 * Animated celebratory golden & cyan particle burst for the 600 UC drop screen.
 */
@Composable
fun GoldenParticleBurstOverlay(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "golden_burst")
    val burstProgress by infiniteTransition.animateFloat(
        initialValue = 0.15f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "burst_p"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val center = Offset(size.width * 0.5f, size.height * 0.38f)
        val maxRadius = size.minDimension * 0.55f
        val particleColors = listOf(NeonYellowBright, ElectricCyan, CyberGreen, TacticalAmber, PrankMagenta)

        for (i in 0 until 20) {
            val angle = (i * 18.0) * PI / 180.0
            val dist = maxRadius * burstProgress * (0.65f + (i % 3) * 0.18f)
            val px = center.x + (dist * cos(angle)).toFloat()
            val py = center.y + (dist * sin(angle)).toFloat()
            val alpha = (1f - burstProgress).coerceIn(0f, 1f)
            val color = particleColors[i % particleColors.size].copy(alpha = alpha)

            drawCircle(
                color = color,
                radius = (8f - 4f * burstProgress).dp.toPx().coerceAtLeast(2f),
                center = Offset(px, py)
            )
        }
    }
}
