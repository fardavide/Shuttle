package shuttle.utils.kotlin

import arrow.core.Either

fun <A, B> Either<A, B>.mapToUnit(): Either<A, Unit> = map { }
