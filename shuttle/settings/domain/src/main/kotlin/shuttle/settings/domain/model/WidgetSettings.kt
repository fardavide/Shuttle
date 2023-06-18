package shuttle.settings.domain.model

data class WidgetSettings(
    val allowTwoLines: Boolean,
    val columnCount: Int,
    val horizontalSpacing: Dp,
    val iconsSize: Dp,
    val rowCount: Int,
    val textSize: Sp,
    val transparency: Int,
    val useMaterialColors: Boolean,
    val verticalSpacing: Dp
) {

    val itemCount: Int
        get() = columnCount * rowCount

    companion object {

        val ColumnsCountRange = 3..16
        val HorizontalSpacingRange = 2..16
        val IconsSizeRange = 24..56
        val RowsCountRange = 1..5
        val TextSizeRange = 6..18
        val TransparencyRange = 0..100
        val VerticalSpacingRange = 2..16

        val Default = WidgetSettings(
            allowTwoLines = false,
            columnCount = 5,
            horizontalSpacing = Dp(8),
            iconsSize = Dp(48),
            rowCount = 2,
            textSize = Sp(12),
            transparency = 70,
            useMaterialColors = true,
            verticalSpacing = Dp(8)
        )
    }
}

@JvmInline
value class Dp(val value: Int)

@JvmInline
value class Sp(val value: Int)
