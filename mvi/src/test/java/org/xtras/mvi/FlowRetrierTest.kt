package org.xtras.mvi

import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.TestCoroutineScope
import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class FlowRetrierTest {
    @Test
    fun `When I use a flow retrier, retry and cancel it, nothing should be sent to the flow after that`() {
        val conflatedBroadcastChannel = ConflatedBroadcastChannel<Int>()

        val flowRetrier = FlowRetrier(TestCoroutineScope(), conflatedBroadcastChannel.asFlow()) {
            -1
        }

        val stubPartialStateSender = StubPartialStateSender<Int>()

        flowRetrier.retry(stubPartialStateSender)

        conflatedBroadcastChannel.sendBlocking(1)

        assertTrue { stubPartialStateSender.sentPartialState(1) }

        conflatedBroadcastChannel.sendBlocking(2)

        assertTrue { stubPartialStateSender.sentPartialState(2) }

        flowRetrier.cancel()

        conflatedBroadcastChannel.sendBlocking(3)

        assertFalse { stubPartialStateSender.sentPartialState(3) }
    }

    @Test
    fun `When I flow retrier and an error happens, it should translate the error correctly`() {
        val flow = flow {
            emit(1)
            throw Exception()
        }

        val flowRetrier = FlowRetrier(TestCoroutineScope(), flow) {
            -1
        }

        val stubPartialStateSender = StubPartialStateSender<Int>()

        flowRetrier.retry(stubPartialStateSender)

        assertTrue { stubPartialStateSender.sentPartialState(1) }

        assertTrue { stubPartialStateSender.sentPartialState(-1) }
    }
}
