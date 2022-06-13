package shuttle.onboarding.presentation.model

import android.graphics.drawable.Drawable

data class OnboardingBlacklistUiModel(
    val apps: List<OnboardingBlacklistAppUiModel>
)

data class OnboardingBlacklistAppUiModel(
    val name: String,
    val icon: Drawable,
    val isBlacklisted: Boolean
)
