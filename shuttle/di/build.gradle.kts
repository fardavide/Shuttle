plugins {
    id("shuttle.android")
}

moduleDependencies {

    accessibility()
    apps {
        data()
        domain()
        presentation()
    }
    coordinates {
        data()
        domain()
    }
    database()
    icons {
        data()
        domain()
        presentation()
    }
    permissions()
    predictions {
        domain()
        presentation()
    }
    settings {
        data()
        domain()
        presentation()
    }
    stats {
        data()
        domain()
    }
    utils.android()
}

dependencies {

    implementation(libs.bundles.base)

    testImplementation(libs.bundles.test.kotlin)
}
