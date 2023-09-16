package shuttle.utils.compose

import android.app.Activity
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass as AndroidWindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass as AndroidWindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass as AndroidWindowWidthSizeClass

val LocalWindowSizeClass = compositionLocalOf<WindowSizeClass> {
    error("CompositionLocal LocalWindowSizeClass not present")
}

@Composable
fun calculateWindowSizeClass(): WindowSizeClass {
    return if (LocalInspectionMode.current) {
        val configuration = LocalConfiguration.current
        val dpSize = DpSize(
            width = configuration.screenWidthDp.dp,
            height = configuration.screenHeightDp.dp
        )
        WindowSizeClass.calculateFromSize(dpSize)
    } else {
        val context = LocalContext.current as Activity
        WindowSizeClass.calculateFromActivity(context)
    }
}

data class WindowSizeClass(
    val width: WindowWidthSizeClass,
    val height: WindowHeightSizeClass
) {

    companion object {

        @Composable
        fun calculateFromActivity(activity: Activity): WindowSizeClass {
            val windowSizeClass = calculateWindowSizeClass(activity)
            return fromPlatformValue(windowSizeClass)
        }

        fun calculateFromSize(dpSize: DpSize): WindowSizeClass =
            fromPlatformValue(AndroidWindowSizeClass.calculateFromSize(dpSize))

        private fun fromPlatformValue(value: AndroidWindowSizeClass) = WindowSizeClass(
            width = WindowWidthSizeClass.fromPlatformValue(value.widthSizeClass),
            height = WindowHeightSizeClass.fromPlatformValue(value.heightSizeClass)
        )
    }
}

enum class WindowWidthSizeClass(val platformValue: AndroidWindowWidthSizeClass) {

    Compact(AndroidWindowWidthSizeClass.Compact),
    Medium(AndroidWindowWidthSizeClass.Medium),
    Expanded(AndroidWindowWidthSizeClass.Expanded);

    companion object {

        internal fun fromPlatformValue(value: AndroidWindowWidthSizeClass): WindowWidthSizeClass =
            entries.first { it.platformValue == value }
    }
}

enum class WindowHeightSizeClass(val platformValue: AndroidWindowHeightSizeClass) {

    Compact(AndroidWindowHeightSizeClass.Compact),
    Medium(AndroidWindowHeightSizeClass.Medium),
    Expanded(AndroidWindowHeightSizeClass.Expanded);

    companion object {

        internal fun fromPlatformValue(value: AndroidWindowHeightSizeClass): WindowHeightSizeClass =
            entries.first { it.platformValue == value }
    }
}
