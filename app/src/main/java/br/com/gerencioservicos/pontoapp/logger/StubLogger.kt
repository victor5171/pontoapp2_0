package br.com.gerencioservicos.pontoapp.logger

import org.xtras.mvi.Loggable
import org.xtras.mvi.Logger

internal object StubLogger : Logger {
    override fun logIntent(intent: Loggable) {}

    override fun logFailedIntent(intent: Loggable, throwable: Throwable) {}

    override fun logTransformedNewState(transform: Loggable, previousState: Loggable, newState: Loggable) {}

    override fun logFailedTransformNewState(transform: Loggable, state: Loggable, throwable: Throwable) {}
}
