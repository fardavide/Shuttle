package cinescout.detekt

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.RuleSet
import io.gitlab.arturbosch.detekt.api.RuleSetProvider

class ShuttleRuleSetProvider : RuleSetProvider {

    override val ruleSetId: String = "Shuttle"

    override fun instance(config: Config): RuleSet {
        return RuleSet(
            ruleSetId,
            listOf(
                UseComposableActions()
            )
        )
    }
}
