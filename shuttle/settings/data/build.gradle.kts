plugins {
    id("shuttle.android")
}

moduleDependencies {

    database()
    settings.domain()
}

dependencies {

    implementation(libs.bundles.base)
    implementation(libs.koin.android)

    testImplementation(libs.bundles.test.kotlin)
    androidTestImplementation(libs.bundles.test.android)
}
