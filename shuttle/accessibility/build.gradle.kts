plugins {
    id("shuttle.android")
}

moduleDependencies {

    apps.domain()
    coordinates.domain()
    stats.domain()
}

dependencies {

    implementation(libs.bundles.base)

    implementation(libs.koin.android)

    testImplementation(libs.bundles.test.kotlin)
    androidTestImplementation(libs.bundles.test.android)
}
