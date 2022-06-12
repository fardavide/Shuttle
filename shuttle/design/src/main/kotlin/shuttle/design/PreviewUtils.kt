package shuttle.design

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import studio.forface.shuttle.design.R

object PreviewUtils {

    val ShuttleIconDrawable @Composable get() = LocalContext.current.getDrawable(R.drawable.ic_shuttle_foreground)!!

    object Dimens {

        object Medium {

            const val Width = 540
            const val Height = 900
        }
    }
}
