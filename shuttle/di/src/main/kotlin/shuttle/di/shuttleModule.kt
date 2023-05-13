package shuttle.di

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module
import org.koin.ksp.generated.module
import shuttle.accessibility.accessibilityModule
import shuttle.apps.data.AppsDataModule
import shuttle.apps.domain.AppsDomainModule
import shuttle.apps.presentation.AppsPresentationModule
import shuttle.coordinates.data.CoordinatesDataModule
import shuttle.coordinates.domain.CoordinatesDomainModule
import shuttle.database.DatabaseModule
import shuttle.icons.data.IconsDataModule
import shuttle.icons.domain.IconsDomainModule
import shuttle.onboarding.domain.OnboardingDomainModule
import shuttle.onboarding.presentation.OnboardingPresentationModule
import shuttle.payments.data.PaymentsDataModule
import shuttle.payments.domain.PaymentsDomainModule
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
        paymentsPresentationModule +
        permissionsDomainModule + permissionsPresentationModule +
        predictionsDomainModule +
        settingsDomainModule + settingsPresentationModule +
        statsDataModule + statsDomainModule

@Module(
    includes = [
        AppsDataModule::class,
        AppsDomainModule::class,
        AppsPresentationModule::class,

        CoordinatesDataModule::class,
        CoordinatesDomainModule::class,

        DatabaseModule::class,

        IconsDataModule::class,
        IconsDomainModule::class,

        OnboardingDomainModule::class,
        OnboardingPresentationModule::class,

        PaymentsDataModule::class,
        PaymentsDomainModule::class,

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

const val AppVersionQualifier = shuttle.utils.kotlin.AppVersionQualifier
