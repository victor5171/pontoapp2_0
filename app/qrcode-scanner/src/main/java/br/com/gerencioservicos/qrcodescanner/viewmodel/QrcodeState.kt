package br.com.gerencioservicos.qrcodescanner.viewmodel

import androidx.camera.core.ImageAnalysis
import br.com.gerencioservicos.repository.qrcode.ScannedCode
import org.xtras.mvi.Actions
import org.xtras.mvi.State

internal sealed class QrcodeState : State {
    abstract val actions: Actions<QrcodeAction>

    abstract fun copyWithActions(actions: Actions<QrcodeAction>): QrcodeState

    data class Loading(
        override val actions: Actions<QrcodeAction>
    ) : QrcodeState() {
        override fun copyWithActions(actions: Actions<QrcodeAction>): QrcodeState {
            return copy(actions = actions)
        }
    }

    sealed class ShowingCamera : QrcodeState() {
        abstract val imageAnalysis: ImageAnalysis

        data class Capturing(
            override val imageAnalysis: ImageAnalysis,
            override val actions: Actions<QrcodeAction>
        ) : ShowingCamera() {
            override fun copyWithActions(actions: Actions<QrcodeAction>): QrcodeState {
                return copy(actions = actions)
            }
        }

        data class CapturedCode(
            override val imageAnalysis: ImageAnalysis,
            val scannedCode: ScannedCode,
            override val actions: Actions<QrcodeAction>
        ) : ShowingCamera() {
            override fun copyWithActions(actions: Actions<QrcodeAction>): QrcodeState {
                return copy(actions = actions)
            }
        }
    }
}
