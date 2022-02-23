plugins {
    id("shuttle.android")
}

moduleDependencies {

    apps.domain()
    database()
    stats.domain()
}

dependencies {

    implementation(libs.bundles.base)
    implementation(libs.koin.android)
    implementation(libs.klock)

    testImplementation(libs.bundles.test.kotlin)
    androidTestImplementation(libs.bundles.test.android)
}
