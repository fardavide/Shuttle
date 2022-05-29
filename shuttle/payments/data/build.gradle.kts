plugins {
    id("shuttle.android")
}

moduleDependencies {

    payments.domain()
    utils {
        android()
        kotlin()
    }
}

dependencies {

    implementation(libs.bundles.base)

    implementation(libs.android.billing)
    implementation(libs.klock)
    implementation(libs.koin.android)

    testImplementation(libs.bundles.test.kotlin)
    androidTestImplementation(libs.bundles.test.android)
}
