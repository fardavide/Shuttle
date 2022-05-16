plugins {
    id("shuttle.android")
}

shuttleAndroid {
    val version = System.getenv()["APP_VERSION"] ?: "1"
    useCompose()
    androidApp(
        id = "studio.forface.shuttle",
        versionCode = version.toInt(),
        versionName = version
    )
}

moduleDependencies {

    accessibility()
    apps.presentation()
    design()
    di()
    permissions.presentation()
    predictions.presentation()
    settings.presentation()
}

dependencies {

    implementation(platform(libs.firebase.bom))

    implementation(libs.bundles.base)
    implementation(libs.bundles.compose)

    implementation(libs.androidx.activity)
    implementation(libs.androidx.ktx)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.lifecycle.viewModel)
    implementation(libs.androidx.workManager)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)
    implementation(libs.koin.android)
    implementation(libs.koin.android.workManager)
    implementation(libs.navigation.compose)

    debugImplementation(libs.compose.uiTooling)

    testImplementation(libs.bundles.test.kotlin)
    testImplementation(libs.koin.test)
    androidTestImplementation(libs.bundles.test.android)
    androidTestImplementation(libs.compose.uiTest)
}
