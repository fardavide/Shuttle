package shuttle.settings.domain.model

data class WidgetSettings(
    val rowsCount: Int,
    val columnsCount: Int,
    val iconsSize: Dp,
    val horizontalSpacing: Dp,
    val verticalSpacing: Dp,
    val textSize: Sp,
    val allowTwoLines: Boolean
) {

    companion object {

        val RowsCountRange = 1..5
        val ColumnsCountRange = 3..16
        val IconsSizeRange = 24..56
        val HorizontalSpacingRange = 2..16
        val VerticalSpacingRange = 2..16
        val TextSizeRange = 6..18

        val Default = WidgetSettings(
            rowsCount = 2,
            columnsCount = 5,
            iconsSize = Dp(48),
            horizontalSpacing = Dp(8),
            verticalSpacing = Dp(8),
            textSize = Sp(12),
            allowTwoLines = false
        )
    }
}

@JvmInline
value class Dp(val value: Int)

@JvmInline
value class Sp(val value: Int)
