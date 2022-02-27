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
}

dependencies {

    implementation(libs.bundles.base)

    testImplementation(libs.bundles.test.kotlin)
}
