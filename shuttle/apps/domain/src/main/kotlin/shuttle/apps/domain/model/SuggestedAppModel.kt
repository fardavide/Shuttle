package shuttle.apps.domain.model

data class SuggestedAppModel(
    val id: AppId,
    val name: AppName,
    val isSuggested: Boolean
)
