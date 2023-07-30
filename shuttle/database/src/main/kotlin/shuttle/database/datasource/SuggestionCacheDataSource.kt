package shuttle.database.datasource

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.invoke
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import shuttle.database.SuggestionCacheQueries
import shuttle.database.model.DatabaseCachedSuggestedApp
import shuttle.database.model.DatabaseSuggestionCache
import shuttle.database.util.suspendTransaction
import shuttle.performance.SuggestionsTracer
import shuttle.utils.kotlin.IoDispatcher

interface SuggestionCacheDataSource {

    suspend fun findCachedSuggestions(): List<DatabaseCachedSuggestedApp>

    suspend fun insertSuggestionCache(suggestionCache: List<DatabaseSuggestionCache>)
}

@Factory
internal class RealSuggestionCacheDataSource(
    private val cacheQueries: SuggestionCacheQueries,
    @Named(IoDispatcher) private val ioDispatcher: CoroutineDispatcher,
    private val tracer: SuggestionsTracer
) : SuggestionCacheDataSource {

    override suspend fun findCachedSuggestions(): List<DatabaseCachedSuggestedApp> = ioDispatcher {
        tracer.fromCache {
            cacheQueries.findAll().executeAsList()
        }
    }

    override suspend fun insertSuggestionCache(suggestionCache: List<DatabaseSuggestionCache>) {
        cacheQueries.suspendTransaction(ioDispatcher) {
            for (suggestion in suggestionCache) {
                insert(suggestion)
            }
        }
    }
}

class FakeSuggestionCacheDataSource : SuggestionCacheDataSource {

    override suspend fun findCachedSuggestions(): List<DatabaseCachedSuggestedApp> = emptyList()

    override suspend fun insertSuggestionCache(suggestionCache: List<DatabaseSuggestionCache>) {}
}
