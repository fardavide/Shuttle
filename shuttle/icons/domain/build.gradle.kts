plugins {
    id("shuttle.android")
}

moduleDependencies {

    apps.domain()
}

dependencies {

    implementation(libs.bundles.base)
    testImplementation(libs.bundles.test.kotlin)
}
