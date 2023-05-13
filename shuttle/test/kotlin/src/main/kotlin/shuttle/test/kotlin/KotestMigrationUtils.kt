package shuttle.test.kotlin

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe

@Deprecated(
    message = "Use shouldBe instead",
    replaceWith = ReplaceWith("actual shouldBe expected", "io.kotest.matchers.shouldBe")
)
fun <T> AnnotationSpec.assertEquals(expected: T, actual: T) = actual shouldBe expected
