package shuttle.design.sample

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.graphics.drawable.toDrawable

object DrawableSample {

    val Empty: Drawable
        @Composable get() = Bitmap
            .createBitmap(1, 1, Bitmap.Config.ARGB_8888)
            .toDrawable(LocalContext.current.resources)
}
