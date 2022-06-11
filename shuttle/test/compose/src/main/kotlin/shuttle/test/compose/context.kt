package shuttle.test.compose

import android.content.Context
import androidx.annotation.StringRes
import androidx.test.core.app.ApplicationProvider

val context: Context get() = ApplicationProvider.getApplicationContext()

internal fun getString(@StringRes resId: Int) = context.getString(resId)
