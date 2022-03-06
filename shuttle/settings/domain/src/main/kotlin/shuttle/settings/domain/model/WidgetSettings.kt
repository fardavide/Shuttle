package shuttle.settings.domain.model

data class WidgetSettings(
    val rowsCount: Int,
    val columnsCount: Int,
    val iconsSize: Dp,
    val horizontalSpacing: Dp,
    val verticalSpacing: Dp,
    val textSize: Sp
) {

    companion object {

        val Default = WidgetSettings(
            rowsCount = 2,
            columnsCount = 5,
            iconsSize = Dp(48),
            horizontalSpacing = Dp(8),
            verticalSpacing = Dp(8),
            textSize = Sp(12)
        )
    }
}

@JvmInline
value class Dp(val value: Int)

@JvmInline
value class Sp(val value: Int)
