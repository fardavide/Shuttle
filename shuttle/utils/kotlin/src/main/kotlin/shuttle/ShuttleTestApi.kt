package shuttle

@RequiresOptIn(
    level = RequiresOptIn.Level.ERROR,
    message = "This API is only for testing purposes. Do not use it in production code."
)
annotation class ShuttleTestApi

@ShuttleTestApi
fun notImplementedFake(): Nothing = throw NotImplementedError("Fake not implemented")
