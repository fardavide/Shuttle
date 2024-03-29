package shuttle.di

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import shuttle.accessibility.AccessibilityModule
import shuttle.analytics.AnalyticsModule
import shuttle.analytics.android.AnalyticsAndroidModule
import shuttle.apps.data.AppsDataModule
import shuttle.apps.domain.AppsDomainModule
import shuttle.apps.presentation.AppsPresentationModule
import shuttle.consents.domain.ConsentsDomainModule
import shuttle.consents.presentation.ConsentsPresentationModule
import shuttle.coordinates.data.CoordinatesDataModule
import shuttle.coordinates.domain.CoordinatesDomainModule
import shuttle.database.DatabaseModule
import shuttle.icons.data.IconsDataModule
import shuttle.icons.domain.IconsDomainModule
import shuttle.onboarding.domain.OnboardingDomainModule
import shuttle.onboarding.presentation.OnboardingPresentationModule
import shuttle.payments.data.PaymentsDataModule
import shuttle.payments.domain.PaymentsDomainModule
import shuttle.payments.presentation.PaymentsPresentationModule
import shuttle.performance.PerformanceModule
import shuttle.performance.android.PerformanceAndroidModule
import shuttle.permissions.domain.PermissionsDomainModule
import shuttle.permissions.presentation.PermissionsPresentationModule
import shuttle.predictions.domain.PredictionsDomainModule
import shuttle.predictions.presentation.PredictionsPresentationModule
import shuttle.settings.data.SettingsDataModule
import shuttle.settings.domain.SettingsDomainModule
import shuttle.settings.presentation.SettingsPresentationModule
import shuttle.stats.data.StatsDataModule
import shuttle.stats.domain.StatsDomainModule
import shuttle.util.android.UtilsAndroidModule
import shuttle.utils.kotlin.UtilsKotlinModule
import shuttle.widget.WidgetModule

@Module(
    includes = [
        AccessibilityModule::class,

        AnalyticsAndroidModule::class,
        AnalyticsModule::class,

        AppsDataModule::class,
        AppsDomainModule::class,
        AppsPresentationModule::class,

        ConsentsDomainModule::class,
        ConsentsPresentationModule::class,

        CoordinatesDataModule::class,
        CoordinatesDomainModule::class,

        DatabaseModule::class,

        IconsDataModule::class,
        IconsDomainModule::class,

        OnboardingDomainModule::class,
        OnboardingPresentationModule::class,

        PaymentsDataModule::class,
        PaymentsDomainModule::class,
        PaymentsPresentationModule::class,

        PerformanceAndroidModule::class,
        PerformanceModule::class,

        PermissionsDomainModule::class,
        PermissionsPresentationModule::class,

        PredictionsDomainModule::class,
        PredictionsPresentationModule::class,

        SettingsDataModule::class,
        SettingsDomainModule::class,
        SettingsPresentationModule::class,

        StatsDataModule::class,
        StatsDomainModule::class,

        UtilsAndroidModule::class,
        UtilsKotlinModule::class,

        WidgetModule::class
    ]
)
@ComponentScan
class ShuttleModule

const val AppVersionQualifier = shuttle.utils.kotlin.AppVersionQualifier
