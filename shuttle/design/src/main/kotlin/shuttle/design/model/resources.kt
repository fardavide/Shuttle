package shuttle.design.model

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

sealed interface TextRes {

    @JvmInline
    value class Plain(val value: String) : TextRes

    @JvmInline
    value class Resource(@StringRes val resId: Int) : TextRes

    companion object {

        operator fun invoke(string: String): TextRes =
            Plain(string)

        operator fun invoke(@StringRes resId: Int): TextRes =
            Resource(resId)
    }
}

@Composable
fun stringResource(textRes: TextRes): String = when (textRes) {
    is TextRes.Plain -> textRes.value
    is TextRes.Resource -> stringResource(id = textRes.resId)
}
