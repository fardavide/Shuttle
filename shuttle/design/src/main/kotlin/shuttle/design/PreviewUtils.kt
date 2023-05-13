package shuttle.design

import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import shuttle.resources.R.drawable

object PreviewUtils {

    val ShuttleIconDrawable @Composable get() =
        AppCompatResources.getDrawable(LocalContext.current, drawable.ic_shuttle_foreground)!!

    object Dimens {

        object Medium {

            const val Width = 540
            const val Height = 900
        }
    }
}
