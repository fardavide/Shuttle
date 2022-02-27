plugins {
    kotlin("jvm")
}

moduleDependencies {

    apps.domain()
}

dependencies {

    implementation(libs.bundles.base)

    testImplementation(libs.bundles.test.kotlin)
}
