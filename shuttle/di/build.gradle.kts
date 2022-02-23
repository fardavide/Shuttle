plugins {
    id("shuttle.android")
}

moduleDependencies {

    apps {
        data()
        domain()
        presentation()
    }
    database()
    predictions {
        domain()
        presentation()
    }
    stats {
        data()
        domain()
    }
}

dependencies {

    implementation(libs.bundles.base)

    testImplementation(libs.bundles.test.kotlin)
}
