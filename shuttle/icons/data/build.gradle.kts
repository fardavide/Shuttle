plugins {
    id("shuttle.android")
}

moduleDependencies {

    icons.domain()
}

dependencies {

    implementation(libs.bundles.base)
    implementation(libs.koin.android)

    testImplementation(libs.bundles.test.kotlin)
    androidTestImplementation(libs.bundles.test.android)
}
