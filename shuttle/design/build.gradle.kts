plugins {
    id("shuttle.android")
}

shuttleAndroid.useCompose()

dependencies {

    implementation(libs.bundles.base)
    implementation(libs.bundles.compose)

    debugImplementation(libs.compose.uiTooling)

    testImplementation(libs.bundles.test.kotlin)
    androidTestImplementation(libs.bundles.test.android)
    androidTestImplementation(libs.compose.uiTest)
}
