package br.com.gerencioservicos.qrcodescanner.viewmodel

import androidx.camera.core.ImageAnalysis
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
        private val imageAnalysis: ImageAnalysis,
        private val scannedCode: ScannedCode
    ) : QrcodeTransform() {

        override fun reduce(currentState: QrcodeState): QrcodeState {
            if (currentState is QrcodeState.ShowingCamera.CapturedCode) {
                throw UnsupportedOperationException("There's already a captured code!")
            }

            return QrcodeState.ShowingCamera.CapturedCode(imageAnalysis, scannedCode, currentState.actions)
        }
    }

    data class SetCapturing(
        private val imageAnalysis: ImageAnalysis
    ) : QrcodeTransform() {

        override fun reduce(currentState: QrcodeState): QrcodeState {
            return QrcodeState.ShowingCamera.Capturing(imageAnalysis, currentState.actions)
        }
    }
}
