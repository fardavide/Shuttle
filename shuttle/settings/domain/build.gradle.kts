plugins {
    kotlin("jvm")
}

moduleDependencies {

    apps.domain()
    stats.domain()
}

dependencies {

    implementation(libs.bundles.base)

    testImplementation(libs.bundles.test.kotlin)
}
