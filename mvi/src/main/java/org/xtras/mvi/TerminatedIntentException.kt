package org.xtras.mvi

import kotlinx.coroutines.CancellationException

internal class TerminatedIntentException : CancellationException()
