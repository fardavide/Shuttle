plugins {
    id("shuttle.kotlin")
}

moduleDependencies {

    utils {
        kotlin()
    }
}

dependencies {

    implementation(libs.bundles.base)

    implementation(libs.klock)
    implementation(libs.koin.core)

    testImplementation(libs.bundles.test.kotlin)
}
