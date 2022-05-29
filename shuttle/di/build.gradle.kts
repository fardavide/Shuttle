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
    payments {
        data()
        domain()
    }
    permissions {
        domain()
        presentation()
    }
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
    utils {
        android()
        kotlin()
    }
}

dependencies {

    implementation(libs.bundles.base)

    testImplementation(libs.bundles.test.kotlin)
}
