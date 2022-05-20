plugins {
    id("shuttle.android")
}

shuttleAndroid.useCompose()

moduleDependencies {

    accessibility()
    apps.domain()
    design()
    icons.domain()
    permissions.domain()
    settings.domain()
    utils {
        android()
        kotlin()
    }
}

dependencies {

    implementation(libs.bundles.base)
    implementation(libs.bundles.compose)

    implementation(libs.accompanist.permissions)
    implementation(libs.androidx.lifecycle.viewModel)
    implementation(libs.koin.android)
    implementation(libs.navigation.compose)

    debugImplementation(libs.compose.uiTooling)

    testImplementation(libs.bundles.test.kotlin)
    androidTestImplementation(libs.bundles.test.android)
    androidTestImplementation(libs.compose.uiTest)
}
