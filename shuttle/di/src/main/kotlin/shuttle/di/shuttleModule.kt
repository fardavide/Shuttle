package shuttle.di

import shuttle.accessibility.accessibilityModule
import shuttle.apps.data.appsDataModule
import shuttle.apps.domain.appsDomainModule
import shuttle.apps.presentation.appsPresentationModule
import shuttle.coordinates.data.coordinatesDataModule
import shuttle.coordinates.domain.coordinatesDomainModule
import shuttle.database.databaseModule
import shuttle.icons.data.iconsDataModule
import shuttle.icons.domain.iconsDomainModule
import shuttle.icons.presentation.iconsPresentationModule
import shuttle.permissions.permissionsModule
import shuttle.predictions.domain.predictionsDomainModule
import shuttle.predictions.presentation.predictionsPresentationModule
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
        iconsDataModule + iconsDomainModule + iconsPresentationModule +
        permissionsModule +
        predictionsDomainModule + predictionsPresentationModule +
        settingsDataModule + settingsDomainModule + settingsPresentationModule +
        statsDataModule + statsDomainModule +
        utilsAndroidModule

