plugins {
    id("shuttle.android")
}

moduleDependencies {

    apps.domain()
    coordinates.domain()
    predictions.presentation()
    settings.domain()
    stats.domain()
}

dependencies {

    implementation(libs.bundles.base)

    implementation(libs.androidx.glance)
    implementation(libs.koin.android)

    testImplementation(libs.bundles.test.kotlin)
    androidTestImplementation(libs.bundles.test.android)
}
