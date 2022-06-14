plugins {
    id("shuttle.android")
}

moduleDependencies {

    apps.domain()
    coordinates.domain()
    database()
    settings.domain()
    stats.domain()
    utils.kotlin()
}

dependencies {

    implementation(libs.bundles.base)
    implementation(libs.androidx.workManager)
    implementation(libs.koin.android)
    implementation(libs.koin.android.workManager)
    implementation(libs.klock)

    testImplementation(libs.bundles.test.kotlin)
    androidTestImplementation(libs.bundles.test.android)
}
