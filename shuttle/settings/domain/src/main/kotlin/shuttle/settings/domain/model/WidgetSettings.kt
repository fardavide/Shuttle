package shuttle.settings.domain.model

data class WidgetSettings(
    val rowsCount: Int,
    val columnsCount: Int,
    val iconSize: Dp,
    val spacing: Dp,
    val textSize: Sp
) {

    companion object {

        val Default = WidgetSettings(
            rowsCount = 2,
            columnsCount = 5,
            iconSize = Dp(48),
            spacing = Dp(8),
            textSize = Sp(12)
        )
    }
}

@JvmInline
value class Dp(val value: Int)

@JvmInline
value class Sp(val value: Int)
