plugins {
    kotlin("jvm")
}

moduleDependencies {

    apps.domain()
}

dependencies {

    implementation(libs.bundles.base)

    implementation(libs.klock)

    testImplementation(libs.bundles.test.kotlin)
}
