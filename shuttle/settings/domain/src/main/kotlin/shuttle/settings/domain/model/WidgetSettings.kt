package shuttle.settings.domain.model

data class WidgetSettings(
    val allowTwoLines: Boolean,
    val columnsCount: Int,
    val horizontalSpacing: Dp,
    val iconsSize: Dp,
    val rowsCount: Int,
    val textSize: Sp,
    val verticalSpacing: Dp
) {

    companion object {

        val ColumnsCountRange = 3..16
        val HorizontalSpacingRange = 2..16
        val IconsSizeRange = 24..56
        val RowsCountRange = 1..5
        val TextSizeRange = 6..18
        val VerticalSpacingRange = 2..16

        val Default = WidgetSettings(
            allowTwoLines = false,
            columnsCount = 5,
            horizontalSpacing = Dp(8),
            iconsSize = Dp(48),
            rowsCount = 2,
            textSize = Sp(12),
            verticalSpacing = Dp(8)
        )
    }
}

@JvmInline
value class Dp(val value: Int)

@JvmInline
value class Sp(val value: Int)
