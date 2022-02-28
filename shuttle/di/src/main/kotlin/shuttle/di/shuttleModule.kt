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
import shuttle.preferences.preferencesModule
import shuttle.settings.data.settingsDataModule
import shuttle.settings.domain.settingsDomainModule
import shuttle.settings.presentation.settingsPresentationModule
import shuttle.stats.data.statsDataModule
import shuttle.stats.domain.statsDomainModule
import shuttle.util.android.utilsAndroidModule

val shuttleModule =
    accessibilityModule +
        appsDataModule + appsDomainModule + appsPresentationModule +
        coordinatesDataModule + coordinatesDomainModule +
        databaseModule +
        predictionsDomainModule + predictionsPresentationModule +
        preferencesModule +
        settingsDataModule + settingsDomainModule + settingsPresentationModule +
        statsDataModule + statsDomainModule +
        utilsAndroidModule

