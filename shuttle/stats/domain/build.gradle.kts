plugins {
    kotlin("jvm")
}

moduleDependencies {

    apps.domain()
    coordinates.domain()
}

dependencies {

    implementation(libs.bundles.base)

    implementation(libs.klock)

    testImplementation(libs.bundles.test.kotlin)
}
