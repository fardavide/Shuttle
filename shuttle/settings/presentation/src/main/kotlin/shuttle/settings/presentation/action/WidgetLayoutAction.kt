package shuttle.settings.presentation.action

sealed interface WidgetLayoutAction {

    data class UpdateAllowTwoLines(val value: Boolean) : WidgetLayoutAction
    data class UpdateColumns(val value: Int) : WidgetLayoutAction
    data class UpdateHorizontalSpacing(val value: Int) : WidgetLayoutAction
    data class UpdateIconsSize(val value: Int) : WidgetLayoutAction
    data class UpdateRows(val value: Int) : WidgetLayoutAction
    data class UpdateTextSize(val value: Int) : WidgetLayoutAction
    data class UpdateTransparency(val value: Int) : WidgetLayoutAction
    data class UpdateUseMaterialColors(val value: Boolean) : WidgetLayoutAction
    data class UpdateVerticalSpacing(val value: Int) : WidgetLayoutAction
}
