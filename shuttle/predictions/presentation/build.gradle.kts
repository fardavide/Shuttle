plugins {
    id("shuttle.android")
}

shuttleAndroid.useCompose()

moduleDependencies {

    apps.domain()
    design()
    predictions.domain()
    stats.domain()
    utils.android()
}

dependencies {

    implementation(libs.bundles.base)
    implementation(libs.bundles.compose)

    implementation(libs.accompanist.permissions)
    implementation(libs.androidx.glance)
    implementation(libs.androidx.lifecycle.viewModel)
    implementation(libs.koin.android)

    debugImplementation(libs.compose.uiTooling)

    testImplementation(libs.bundles.test.kotlin)
    androidTestImplementation(libs.bundles.test.android)
    androidTestImplementation(libs.compose.uiTest)
}
