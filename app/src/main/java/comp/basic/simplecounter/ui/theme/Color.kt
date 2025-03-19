package comp.basic.simplecounter.ui.theme

import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val OrangeBold = Color(0xFFF14A00)
val OrangeDark = Color(0xFFFF5722)
val OrangeLight = Color(0xFFE07B39)

val WhiteClean = Color(0xFFFFFFFF)
val Darkboard = Color(0xFF2F2D2D)

sealed class ThemeColors(
    val background: Color,
    val surface: Color,
    val primary: Color,
    val text: Color,
    val secondary: Color
)  {
    object Night: ThemeColors(
        background = Darkboard,
        surface = Darkboard,
        primary = OrangeDark,
        text = Color.White,
        secondary = Color.Gray
    )
    object Day: ThemeColors(
        background = WhiteClean,
        surface = WhiteClean,
        primary = OrangeBold,
        secondary = OrangeLight,
        text = Color.Black
    )
}