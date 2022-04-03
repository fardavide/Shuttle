plugins {
    id("shuttle.android")
}

moduleDependencies {

    utils.android()
}

dependencies {

    implementation(libs.bundles.base)

    implementation(libs.accompanist.permissions)
    implementation(libs.koin.android)

    testImplementation(libs.bundles.test.kotlin)
    androidTestImplementation(libs.bundles.test.android)
}
