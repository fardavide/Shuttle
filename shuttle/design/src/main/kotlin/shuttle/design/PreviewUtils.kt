package shuttle.design

import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

object PreviewUtils {

    val ShuttleIconDrawable @Composable get() =
        AppCompatResources.getDrawable(LocalContext.current, R.drawable.ic_shuttle_foreground)!!

    object Dimens {

        object Medium {

            const val Width = 540
            const val Height = 900
        }
    }
}
