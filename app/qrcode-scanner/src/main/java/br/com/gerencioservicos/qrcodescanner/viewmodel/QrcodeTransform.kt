package br.com.gerencioservicos.qrcodescanner.viewmodel

import br.com.gerencioservicos.repository.qrcode.ScannedCode
import org.xtras.mvi.transform.Transform

internal sealed class QrcodeTransform : Transform<QrcodeState> {

    data class AddAction(
        private val action: QrcodeAction
    ) : QrcodeTransform() {

        override fun reduce(currentState: QrcodeState): QrcodeState {
            return currentState.copyWithActions(currentState.actions.add(action))
        }
    }

    data class SetScannedCode(
        private val scannedCode: ScannedCode
    ) : QrcodeTransform() {

        override fun reduce(currentState: QrcodeState): QrcodeState {
            if (currentState is QrcodeState.CapturedCode) {
                throw UnsupportedOperationException("There's already a captured code!")
            }

            return QrcodeState.CapturedCode(scannedCode, currentState.actions)
        }
    }

    object SetCapturing : QrcodeTransform() {

        override fun reduce(currentState: QrcodeState): QrcodeState {
            return QrcodeState.Capturing(currentState.actions)
        }
    }
}
