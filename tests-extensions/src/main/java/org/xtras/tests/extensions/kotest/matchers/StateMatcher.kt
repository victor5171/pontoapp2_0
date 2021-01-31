package org.xtras.tests.extensions.kotest.matchers
import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.kotest.matchers.should

public inline fun <reified TState : Any> beOfState(noinline validation: ((TState) -> Unit)? = null): Matcher<Any> = object : Matcher<Any> {
    override fun test(value: Any): MatcherResult {
        val wasOfType = value is TState

        return MatcherResult(
            passed = wasOfType,
            failureMessage = "$value should be ${TState::class}",
            negatedFailureMessage = "$value should not be ${TState::class}!"
        ).also {
            if (wasOfType) {
                validation?.invoke(value as TState)
            }
        }
    }
}

public inline fun <reified TState: Any> Any.shouldBeOfState(noinline validation: ((TState) -> Unit)? = null): Unit = this should beOfState(validation)
