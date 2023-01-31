plugins {
    id("shuttle.android")
    id("com.squareup.sqldelight")
}

sqldelight {
    database("Database") {
        packageName = "shuttle.database"
        schemaOutputDirectory = file("src/main/sqldelight/shuttle/database/schemas")
        verifyMigrations = true
    }
}

dependencies {

    implementation(libs.bundles.base)
    implementation(libs.koin.android)
    implementation(libs.sqlDelight.android)
    implementation(libs.sqlDelight.coroutines)

    testImplementation(libs.bundles.test.kotlin)
    testImplementation(libs.sqlDelight.sqlite)

    androidTestImplementation(libs.bundles.test.android)
}
