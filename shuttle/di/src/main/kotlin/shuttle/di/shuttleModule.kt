package shuttle.di

import shuttle.apps.data.appsDataModule
import shuttle.apps.domain.appsDomainModule
import shuttle.apps.presentation.appsPresentationModule
import shuttle.database.databaseModule
import shuttle.predictions.domain.predictionsDomainModule
import shuttle.predictions.presentation.predictionsPresentationModule
import shuttle.stats.data.statsDataModule
import shuttle.stats.domain.statsDomainModule

val shuttleModule =
    appsDataModule + appsDomainModule + appsPresentationModule +
        databaseModule +
        predictionsDomainModule + predictionsPresentationModule +
        statsDataModule + statsDomainModule

