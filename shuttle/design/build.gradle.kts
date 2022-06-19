plugins {
    id("shuttle.android")
}

shuttleAndroid.useCompose()

moduleDependencies {

    test.compose()
    utils {
        android()
        kotlin()
    }
}

dependencies {

    ksp(libs.ksp.arrow)

    implementation(libs.bundles.base)
    implementation(libs.bundles.compose)

    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.navigation.compose)

    debugImplementation(libs.compose.uiTooling)

    testImplementation(libs.bundles.test.kotlin)
    androidTestImplementation(libs.bundles.test.android)
    androidTestImplementation(libs.compose.uiTest)
}
