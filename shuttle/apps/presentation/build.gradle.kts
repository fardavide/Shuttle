plugins {
    id("shuttle.android")
}

shuttleAndroid.useCompose()

moduleDependencies {

    apps.domain()
    design()
}

dependencies {

    implementation(libs.bundles.base)
    implementation(libs.bundles.compose)

    implementation(libs.androidx.lifecycle.viewModel)
    implementation(libs.koin.android)

    debugImplementation(libs.compose.uiTooling)

    testImplementation(libs.bundles.test.kotlin)
    androidTestImplementation(libs.bundles.test.android)
    androidTestImplementation(libs.compose.uiTest)
}
