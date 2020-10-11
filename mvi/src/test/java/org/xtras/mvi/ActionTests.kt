package org.xtras.mvi

import org.junit.Test
import org.xtras.mvi.actions.Action
import org.xtras.mvi.actions.newInsertingActions
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ActionTests {

    private class StubAction : Action()

    @Test
    fun `test adding actions and consuming them to see if they are not emitted anymore`() {
        val actionIterable = emptyList<Action>()

        val stubAction1 = StubAction()

        val newActionIterableWithCopiedItems1 = actionIterable.newInsertingActions(stubAction1)

        assertTrue {
            newActionIterableWithCopiedItems1.toList().toTypedArray().contentDeepEquals(arrayOf(stubAction1))
        }

        val stubAction2 = StubAction()

        val newActionIterableWithCopiedItems2 = newActionIterableWithCopiedItems1.newInsertingActions(stubAction2)

        assertTrue {
            newActionIterableWithCopiedItems2.toList().toTypedArray().contentDeepEquals(arrayOf(stubAction1, stubAction2))
        }

        val stubAction3 = StubAction()

        val newActionIterableWithCopiedItems3 = newActionIterableWithCopiedItems2.newInsertingActions(stubAction3)

        assertTrue {
            newActionIterableWithCopiedItems3.toList().toTypedArray().contentDeepEquals(arrayOf(stubAction1, stubAction2, stubAction3))
        }

        // Consuming Action2 to see if it's removed
        stubAction2.consume()

        newActionIterableWithCopiedItems3.toList().toTypedArray().contentDeepEquals(arrayOf(stubAction1, stubAction3))

        stubAction1.consume()
        stubAction3.consume()

        assertEquals(0, newActionIterableWithCopiedItems3.count())
    }
}
