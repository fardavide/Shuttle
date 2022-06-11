plugins {
    kotlin("jvm")
}

moduleDependencies {

    settings.domain()
    utils.kotlin()
}

dependencies {

    implementation(libs.bundles.base)

    testImplementation(libs.bundles.test.kotlin)
}
