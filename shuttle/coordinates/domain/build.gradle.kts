plugins {
    id("shuttle.kotlin")
}

dependencies {

    implementation(libs.bundles.base)

    implementation(libs.klock)

    testImplementation(libs.bundles.test.kotlin)
}
