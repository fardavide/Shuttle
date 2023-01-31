plugins {
    kotlin("jvm")
}

dependencies {
    compileOnly(libs.detekt.api)

    testImplementation(libs.bundles.test.kotlin)
    testImplementation(libs.detekt.test)
}
