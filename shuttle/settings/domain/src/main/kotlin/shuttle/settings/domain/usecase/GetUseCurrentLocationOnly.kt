package shuttle.settings.domain.usecase

import kotlinx.coroutines.flow.first

class GetUseCurrentLocationOnly(
    private val observeUseCurrentLocationOnly: ObserveUseCurrentLocationOnly
) {

    suspend operator fun invoke(): Boolean =
        observeUseCurrentLocationOnly().first()
}
