package shuttle.settings.presentation.resources.en

import shuttle.settings.presentation.resources.Strings

internal object EnStrings : Strings {

    override val AppIconContentDescription = "Application icon"
    override val SettingsTitle = "Settings"

    object Blacklist : Strings.Blacklist {

        override val Title = "Blacklist"
        override val Description = "Selected apps won't be proposed as suggestions"
    }

    object CheckPermissions : Strings.CheckPermissions {

        override val Title = "Check permissions"
        override val Description = "Verify the state of the required permissions"
    }

    object IconPack: Strings.IconPack {

        override val Title = "Icon packs"
        override val Description = "Choose an Icon Pack for your icons"
        override val SystemDefault = "System icons"
    }

    object WidgetLayout : Strings.WidgetLayout {

        override val Title = "Widget layout"
        override val Description = "Customise the layout of the Widget"

        override val RowsCount = "Rows count"
        override val ColumnsCount = "Column count"
        override val IconsSize = "Icons size"
        override val VerticalSpacing = "Vertical spacing between icons"
        override val HorizontalSpacing = "Horizontal spacing between icons"
        override val TextSize = "Text size"

        override val DpUnit = "Dp"
        override val SpUnit = "Sp"
    }
}
