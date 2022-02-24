plugins {
    id("shuttle.android")
}

moduleDependencies {

    coordinates.domain()
}

dependencies {

    implementation(libs.bundles.base)
    implementation(libs.klock)
    implementation(libs.koin.android)
    implementation(libs.playServices.location)

    testImplementation(libs.bundles.test.kotlin)
    androidTestImplementation(libs.bundles.test.android)
}
