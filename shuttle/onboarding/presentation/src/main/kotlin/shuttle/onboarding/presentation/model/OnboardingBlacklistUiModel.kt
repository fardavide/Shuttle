package shuttle.onboarding.presentation.model

import android.graphics.drawable.Drawable
import arrow.core.NonEmptyList

data class OnboardingBlacklistUiModel(
    val apps: NonEmptyList<OnboardingBlacklistAppUiModel>
)

data class OnboardingBlacklistAppUiModel(
    val name: String,
    val icon: Drawable,
    val isBlacklisted: Boolean
)
