package shuttle.di

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module
import org.koin.ksp.generated.module
import shuttle.accessibility.accessibilityModule
import shuttle.apps.data.AppsDataModule
import shuttle.apps.domain.AppsDomainModule
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
import shuttle.predictions.presentation.PredictionsPresentationModule
import shuttle.settings.data.SettingsDataModule
import shuttle.settings.domain.settingsDomainModule
import shuttle.settings.presentation.settingsPresentationModule
import shuttle.stats.data.statsDataModule
import shuttle.stats.domain.statsDomainModule
import shuttle.util.android.UtilsAndroidModule
import shuttle.utils.kotlin.UtilsKotlinModule
import shuttle.widget.WidgetModule

val shuttleModule =
    ShuttleModule().module +
        accessibilityModule +
        appsPresentationModule +
        coordinatesDataModule + coordinatesDomainModule +
        databaseModule +
        iconsDataModule + iconsDomainModule + iconsPresentationModule +
        onboardingDomainModule + onboardingPresentationModule +
        paymentsDataModule + paymentsDomainModule + paymentsPresentationModule +
        permissionsDomainModule + permissionsPresentationModule +
        predictionsDomainModule +
        settingsDomainModule + settingsPresentationModule +
        statsDataModule + statsDomainModule

@Module(
    includes = [
        AppsDataModule::class,
        AppsDomainModule::class,

        PredictionsPresentationModule::class,

        SettingsDataModule::class,

        UtilsAndroidModule::class,
        UtilsKotlinModule::class,

        WidgetModule::class
    ]
)
@ComponentScan
class ShuttleModule

@Factory internal class Empty

val AppVersionQualifier = shuttle.utils.kotlin.AppVersionQualifier
