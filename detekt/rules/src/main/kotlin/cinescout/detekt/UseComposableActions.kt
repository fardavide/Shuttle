package cinescout.detekt

import cinescout.detekt.UseComposableActions.Companion.Threshold
import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.kotlin.psi.KtParameter

/**
 * Reports functions annotated as `@Composable` with [Threshold] or more lambda parameters
 * ```kotlin
 * // compliant code
 * @Composable
 * fun SomeComposable(
 *     onBack: () -> Unit
 * )
 *
 * // compliant code
 * fun NotComposable(
 *     first: () -> Unit,
 *     second: () -> Unit,
 *     third: () -> Unit,
 *     fourth: () -> Unit,
 * )
 *
 * // non-compliant code
 * @Composable
 * fun SomeComposable(
 *     onBack: () -> Unit,
 *     second: () -> Unit,
 *     third: () -> Unit,
 *     fourth: () -> Unit,
 * )
 * ```
 */
class UseComposableActions : Rule() {

    override val issue = Issue(
        javaClass.simpleName,
        Severity.Maintainability,
        Description,
        Debt.FIVE_MINS
    )

    override fun visitNamedFunction(function: KtNamedFunction) {
        super.visitNamedFunction(function)

        val annotationNames = function.annotationEntries.map { annotation -> annotation.shortName.toString() }
        if ("Composable" in annotationNames) {

            val lambdaParametersCount = function.valueParameters.count(::isNotComposableLambda)
            if (lambdaParametersCount >= Threshold) {
                report(CodeSmell(issue, Entity.atName(function), Message))
            }
        }
    }

    private fun isLambda(parameter: KtParameter) = ") -> " in parameter.text
    private fun isNotComposableLambda(parameter: KtParameter) =
        isLambda(parameter) && "@Composable" !in parameter.text

    private companion object {

        const val Description = "This rule reports a Composable functions with too many lambda parameters."
        const val Message = "Too many lambda parameters: wrap them into an Actions class instead."
        const val Threshold = 2
    }
}
