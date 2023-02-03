package shuttle.di

import org.koin.ksp.generated.module
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
import shuttle.onboarding.domain.onboardingDomainModule
import shuttle.onboarding.presentation.onboardingPresentationModule
import shuttle.payments.data.paymentsDataModule
import shuttle.payments.domain.paymentsDomainModule
import shuttle.payments.presentation.paymentsPresentationModule
import shuttle.permissions.domain.permissionsDomainModule
import shuttle.permissions.presentation.permissionsPresentationModule
import shuttle.predictions.domain.predictionsDomainModule
import shuttle.predictions.presentation.predictionsPresentationModule
import shuttle.settings.data.SettingsDataModule
import shuttle.settings.domain.settingsDomainModule
import shuttle.settings.presentation.settingsPresentationModule
import shuttle.stats.data.statsDataModule
import shuttle.stats.domain.statsDomainModule
import shuttle.util.android.utilsAndroidModule
import shuttle.utils.kotlin.utilsKotlinModule

val shuttleModule =
    accessibilityModule +
        appsDataModule + appsDomainModule + appsPresentationModule +
        coordinatesDataModule + coordinatesDomainModule +
        databaseModule +
        iconsDataModule + iconsDomainModule + iconsPresentationModule +
        onboardingDomainModule + onboardingPresentationModule +
        paymentsDataModule + paymentsDomainModule + paymentsPresentationModule +
        permissionsDomainModule + permissionsPresentationModule +
        predictionsDomainModule + predictionsPresentationModule +
        SettingsDataModule().module + settingsDomainModule + settingsPresentationModule +
        statsDataModule + statsDomainModule +
        utilsAndroidModule + utilsKotlinModule

val AppVersionQualifier = shuttle.utils.kotlin.AppVersionQualifier
