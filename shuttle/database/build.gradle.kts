plugins {
    id("shuttle.android")
    id("com.squareup.sqldelight")
}

dependencies {

    implementation(libs.bundles.base)
    implementation(libs.koin.android)
    implementation(libs.sqlDelight.android)

    testImplementation(libs.bundles.test.kotlin)
    testImplementation(libs.sqlDelight.sqlite)

    androidTestImplementation(libs.bundles.test.android)
}
