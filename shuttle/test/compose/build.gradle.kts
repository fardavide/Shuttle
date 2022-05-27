plugins {
    id("shuttle.android")
}

shuttleAndroid {
    useCompose()
}

moduleDependencies {

    design()
}

dependencies {

    implementation(libs.bundles.base)
    implementation(libs.bundles.compose)
    implementation(libs.bundles.test.android)

    implementation(libs.androidx.activity)
    implementation(libs.compose.uiTest)
}
