package shuttle.launchers

object Launchers {

    private const val ActionLauncher = "com.actionlauncher.playstore"
    private const val AndroidSettings = "com.android.settings"
    private const val MotorolaLauncher = "com.motorola.launcher3"
    private const val NovaLauncher = "com.teslacoilsw.launcher"
    private const val OneUiLauncher = "com.sec.android.app.launcher"
    private const val PixelLauncher = "com.google.android.apps.nexuslauncher"
    private const val SmartLauncher = "ginlemon.flowerfree"

    fun all(): List<String> = listOf(
        ActionLauncher,
        AndroidSettings,
        MotorolaLauncher,
        NovaLauncher,
        OneUiLauncher,
        PixelLauncher,
        SmartLauncher
    )
}
