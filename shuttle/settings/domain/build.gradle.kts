plugins {
    kotlin("jvm")
}

moduleDependencies {

    apps.domain()
    stats.domain()
    utils.kotlin()
}

dependencies {

    implementation(libs.bundles.base)

    testImplementation(libs.bundles.test.kotlin)
}
