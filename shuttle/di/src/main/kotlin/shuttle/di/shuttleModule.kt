package shuttle.di

import shuttle.accessibility.accessibilityModule
import shuttle.apps.data.appsDataModule
import shuttle.apps.domain.appsDomainModule
import shuttle.apps.presentation.appsPresentationModule
import shuttle.coordinates.data.coordinatesDataModule
import shuttle.coordinates.domain.coordinatesDomainModule
import shuttle.database.databaseModule
import shuttle.predictions.domain.predictionsDomainModule
import shuttle.predictions.presentation.predictionsPresentationModule
import shuttle.stats.data.statsDataModule
import shuttle.stats.domain.statsDomainModule

val shuttleModule =
    accessibilityModule +
        appsDataModule + appsDomainModule + appsPresentationModule +
        coordinatesDomainModule + coordinatesDataModule +
        databaseModule +
        predictionsDomainModule + predictionsPresentationModule +
        statsDataModule + statsDomainModule

