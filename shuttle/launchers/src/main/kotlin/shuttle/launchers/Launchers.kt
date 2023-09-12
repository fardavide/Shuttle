package shuttle.launchers

object Launchers {

    private const val AndroidSettings = "com.android.settings"
    private const val MotorolaLauncher = "com.motorola.launcher3"
    private const val NexusLauncher = "com.google.android.apps.nexuslauncher"
    private const val NovaLauncher = "com.teslacoilsw.launcher"
    private const val OneUiLauncher = "com.sec.android.app.launcher"

    fun all(): List<String> = listOf(
        AndroidSettings,
        MotorolaLauncher,
        NexusLauncher,
        NovaLauncher,
        OneUiLauncher
    )
}
