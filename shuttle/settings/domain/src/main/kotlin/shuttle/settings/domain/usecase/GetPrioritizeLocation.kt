package shuttle.settings.domain.usecase

import kotlinx.coroutines.flow.first

class GetPrioritizeLocation(
    private val observePrioritizeLocation: ObservePrioritizeLocation
) {

    suspend operator fun invoke(): Boolean =
        observePrioritizeLocation().first()
}
