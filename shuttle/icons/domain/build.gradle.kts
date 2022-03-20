plugins {
    id("shuttle.kotlin")
}

dependencies {

    implementation(libs.bundles.base)
    testImplementation(libs.bundles.test.kotlin)
}
