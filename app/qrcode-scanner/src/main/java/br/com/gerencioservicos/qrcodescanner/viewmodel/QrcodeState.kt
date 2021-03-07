package br.com.gerencioservicos.qrcodescanner.viewmodel

import br.com.gerencioservicos.repository.qrcode.ScannedCode
import org.xtras.mvi.Actions
import org.xtras.mvi.State

internal sealed class QrcodeState : State {
    abstract val actions: Actions<QrcodeAction>

    abstract fun copyWithActions(actions: Actions<QrcodeAction>): QrcodeState

    data class Capturing(override val actions: Actions<QrcodeAction>) : QrcodeState() {
        override fun copyWithActions(actions: Actions<QrcodeAction>): QrcodeState {
            return copy(actions = actions)
        }
    }

    data class CapturedCode(
        val scannedCode: ScannedCode,
        override val actions: Actions<QrcodeAction>
    ) : QrcodeState() {
        override fun copyWithActions(actions: Actions<QrcodeAction>): QrcodeState {
            return copy(actions = actions)
        }
    }
}
