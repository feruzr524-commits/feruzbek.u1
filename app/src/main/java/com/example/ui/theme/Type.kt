package com.example.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.R

val OrbitronFontFamily = FontFamily(
    Font(R.font.orbitron, FontWeight.Normal),
    Font(R.font.orbitron, FontWeight.Bold),
    Font(R.font.orbitron, FontWeight.ExtraBold)
)

val ChakraPetchFontFamily = FontFamily(
    Font(R.font.chakra_petch, FontWeight.Normal),
    Font(R.font.chakra_petch, FontWeight.Medium),
    Font(R.font.chakra_petch, FontWeight.SemiBold),
    Font(R.font.chakra_petch, FontWeight.Bold)
)

val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = OrbitronFontFamily,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 38.sp,
        lineHeight = 44.sp,
        letterSpacing = 1.sp
    ),
    displayMedium = TextStyle(
        fontFamily = OrbitronFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 30.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.8.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = OrbitronFontFamily,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 26.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.8.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = OrbitronFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.5.sp
    ),
    titleLarge = TextStyle(
        fontFamily = ChakraPetchFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        lineHeight = 26.sp,
        letterSpacing = 0.4.sp
    ),
    titleMedium = TextStyle(
        fontFamily = ChakraPetchFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 17.sp,
        lineHeight = 23.sp,
        letterSpacing = 0.2.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = ChakraPetchFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.2.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = ChakraPetchFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.2.sp
    ),
    labelLarge = TextStyle(
        fontFamily = OrbitronFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 15.sp,
        lineHeight = 20.sp,
        letterSpacing = 1.sp
    ),
    labelSmall = TextStyle(
        fontFamily = ChakraPetchFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 11.sp,
        lineHeight = 15.sp,
        letterSpacing = 0.6.sp
    )
)
