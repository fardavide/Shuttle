plugins {
    id("shuttle.android")
}

moduleDependencies {

    apps.domain()
    database()
}

dependencies {

    implementation(libs.bundles.base)
    implementation(libs.koin.android)

    testImplementation(libs.bundles.test.kotlin)
    androidTestImplementation(libs.bundles.test.android)
}
