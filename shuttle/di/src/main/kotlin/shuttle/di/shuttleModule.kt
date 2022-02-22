package shuttle.di

import shuttle.apps.data.appsDataModule
import shuttle.apps.domain.appsDomainModule
import shuttle.apps.presentation.appsPresentationModule
import shuttle.predictions.domain.predictionsDomainModule
import shuttle.stats.domain.statsDomainModule

val shuttleModule =
    appsDataModule + appsDomainModule + appsPresentationModule +
        predictionsDomainModule +
        statsDomainModule

