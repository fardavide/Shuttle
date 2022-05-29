plugins {
    id("shuttle.android")
}

moduleDependencies {

    utils {
        kotlin()
    }
}

dependencies {

    implementation(libs.bundles.base)

    implementation(libs.android.billing)
    implementation(libs.klock)
    implementation(libs.koin.core)

    testImplementation(libs.bundles.test.kotlin)
}
