package org.xtras.mvi

import kotlin.reflect.KClass

public class StateRequiredNotFulfilledException(
    expectedState: KClass<*>,
    realState: KClass<*>
) : Exception() {

    override val message: String = "Unexpected state! State was $realState, but the expected was $expectedState"
}
