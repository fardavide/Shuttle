package shuttle.design.ui

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowInsetsCompat
import co.touchlab.kermit.Logger
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun ShuttleModalBottomSheet(
    onDismissRequest: () -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState(),
    content: @Composable ColumnScope.() -> Unit
) {
    val systemUiController = rememberSystemUiController()
    val statusBarColor = BottomSheetDefaults.ScrimColor
    val statusBarBaseAlpha = remember { statusBarColor.alpha }
    val windowHeight = getWindowHeightExcludingStatusBar()
    val sheetOffset = runCatching { sheetState.requireOffset() }.getOrDefault(windowHeight.toFloat())
    val alpha = ((windowHeight - sheetOffset) / windowHeight).coerceAtMost(statusBarBaseAlpha)
    Logger.withTag("ShuttleModalBottomSheet").v("sheetOffset=$sheetOffset, windowHeight=$windowHeight, alpha=$alpha")
    systemUiController.setStatusBarColor(color = statusBarColor.copy(alpha = alpha))
    DisposableEffect(Unit) {
        onDispose {
            systemUiController.setStatusBarColor(color = Color.Transparent)
        }
    }

    ModalBottomSheet(sheetState = sheetState, onDismissRequest = onDismissRequest, content = content)
}

@Composable
private fun getWindowHeightExcludingStatusBar(): Int {
    val activity = LocalContext.current as? Activity
    if (activity == null || Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
        return 1
    }

    return with(activity.windowManager) {
        val windowMetrics = currentWindowMetrics
        val insets = windowMetrics.windowInsets.getInsetsIgnoringVisibility(WindowInsetsCompat.Type.systemBars())
        windowMetrics.bounds.height() - insets.top
    }
}
