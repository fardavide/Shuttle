plugins {
    id("shuttle.android")
}

dependencies {

    implementation(libs.bundles.base)
    implementation(libs.androidx.dataStore.preferences)
    implementation(libs.koin.android)

    testImplementation(libs.bundles.test.kotlin)
    testImplementation(libs.sqlDelight.sqlite)

    androidTestImplementation(libs.bundles.test.android)
}
