package org.xtras.mvi.actions

import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ActionTest {
    @Test
    fun `When I call consume, the flag should be set accordingly`() {
        val action = object : Action() {}

        assertFalse { action.isConsumed }

        action.consume()

        assertTrue { action.isConsumed }
    }

    @Test
    fun `When I call execute and consume, it should call the action only if its not consumed`() {
        val action = object : Action() {}

        var calledExecute = false

        action.executeAndConsume { calledExecute = true }

        assertTrue { calledExecute && action.isConsumed }
    }
}