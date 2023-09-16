package shuttle.design

import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview

@Preview(
    device = Devices.PHONE,
    name = AdaptivePreviews.Name.Phone
)
@Preview(
    device = AdaptivePreviews.Spec.PhoneLandScape,
    name = AdaptivePreviews.Name.PhoneLandscape
)
@Preview(
    device = Devices.FOLDABLE,
    name = AdaptivePreviews.Name.Foldable
)
@Preview(
    device = Devices.TABLET,
    name = AdaptivePreviews.Name.Tablet
)
annotation class PlainAdaptivePreviews


@Preview(
    backgroundColor = White,
    device = Devices.PHONE,
    name = AdaptivePreviews.Name.Phone,
    showBackground = true
)
@Preview(
    backgroundColor = White,
    device = AdaptivePreviews.Spec.PhoneLandScape,
    name = AdaptivePreviews.Name.PhoneLandscape,
    showBackground = true
)
@Preview(
    backgroundColor = White,
    device = Devices.FOLDABLE,
    name = AdaptivePreviews.Name.Foldable,
    showBackground = true
)
@Preview(
    backgroundColor = White,
    device = Devices.TABLET,
    name = AdaptivePreviews.Name.Tablet,
    showBackground = true
)
annotation class WithBackgroundAdaptivePreviews

@Preview(
    device = Devices.PHONE,
    name = AdaptivePreviews.Name.Phone,
    showSystemUi = true
)
@Preview(
    device = AdaptivePreviews.Spec.PhoneLandScape,
    name = AdaptivePreviews.Name.PhoneLandscape,
    showSystemUi = true
)
@Preview(
    device = Devices.FOLDABLE,
    name = AdaptivePreviews.Name.Foldable,
    showSystemUi = true
)
@Preview(
    device = Devices.TABLET,
    name = AdaptivePreviews.Name.Tablet,
    showSystemUi = true
)
annotation class WithSystemUiAdaptivePreviews

private object AdaptivePreviews {

    object Name {

        const val Foldable = "Foldable"
        const val Phone = "Phone"
        const val PhoneLandscape = "Phone Landscape"
        const val Tablet = "Tablet"
    }

    object Spec {

        const val PhoneLandScape = "spec:width=411dp,height=891dp,orientation=landscape"
    }
}

@Preview(locale = LocalePreviews.En)
@Preview(locale = LocalePreviews.It)
annotation class PlainLocalePreviews

@Preview(backgroundColor = White, locale = LocalePreviews.En, showBackground = true)
@Preview(backgroundColor = White, locale = LocalePreviews.It, showBackground = true)
annotation class WithBackgroundLocalePreviews

@Preview(locale = LocalePreviews.En, showSystemUi = true)
@Preview(locale = LocalePreviews.It, showSystemUi = true)
annotation class WithSystemUiLocalPreviews

private object LocalePreviews {

    const val En = "en"
    const val It = "it"
}

private const val White = 0xFFFFFFFF
