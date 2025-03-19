package comp.basic.simplecounter.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun winLayout(): winInfo {
    val configuration  = LocalConfiguration.current
    return winInfo(
        screenWidthInfo = when {
            configuration.screenWidthDp < 600 -> winInfo.WindowType.Compact
            configuration.screenWidthDp < 840 -> winInfo.WindowType.Medium
            else -> winInfo.WindowType.Extended
        },
        screenHeightInfo = when {
            configuration.screenHeightDp < 480 -> winInfo.WindowType.Compact
            configuration.screenHeightDp < 900 -> winInfo.WindowType.Medium
            else -> winInfo.WindowType.Extended
        },
        screenWidth = configuration.screenWidthDp.dp,
        screenHeight = configuration.screenHeightDp.dp
    )
}

data class winInfo(
    val screenWidthInfo: WindowType,
    val screenHeightInfo: WindowType,
    val screenHeight: Dp,
    val screenWidth: Dp
) {
    sealed class WindowType {
        object Compact: WindowType()
        object Medium: WindowType()
        object Extended: WindowType()
    }
}
