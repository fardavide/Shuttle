package cinescout.detekt

import io.gitlab.arturbosch.detekt.test.lint
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe

internal class UseComposableActionsTest : AnnotationSpec() {

    private val rule = UseComposableActions()

    @Test
    fun `reports Composable with lambda parameters above or same as the threshold`() {
        // given
        val expected = 1
        val code = """
            @Composable
            fun SomeScreen(
                first: () -> Unit,
                second: () -> Unit
            )
        """.trimIndent()

        // when
        val findings = rule.lint(code)

        // then
        findings.size shouldBe expected
    }

    @Test
    fun `does not report Composable with lambda parameters below the threshold`() {
        // given
        val expected = 0
        val code = """
            @Composable
            fun SomeScreen(
                onBack: () -> Unit
            )
        """.trimIndent()

        // when
        val findings = rule.lint(code)

        // then
        findings.size shouldBe expected
    }

    @Test
    fun `does not report not Composable with lambda parameters above or same as the threshold`() {
        // given
        val expected = 0
        val code = """
            fun NotComposable(
                first: () -> Unit,
                second: () -> Unit,
                third: () -> Unit
            )
        """.trimIndent()

        // when
        val findings = rule.lint(code)

        // then
        findings.size shouldBe expected
    }

    @Test
    fun `ignores lambda annotated as Composable`() {
        // given
        val expected = 0
        val code = """
            @Composable
            fun SomeScreen(
                first: () -> Unit,
                second: @Composable () -> Unit
            )
        """.trimIndent()

        // when
        val findings = rule.lint(code)

        // then
        findings.size shouldBe expected
    }
}
