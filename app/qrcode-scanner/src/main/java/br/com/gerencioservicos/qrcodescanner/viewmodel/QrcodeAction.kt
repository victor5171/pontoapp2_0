package br.com.gerencioservicos.qrcodescanner.viewmodel

import androidx.camera.core.ImageAnalysis
import br.com.gerencioservicos.repository.qrcode.ScannedCode

internal sealed class QrcodeAction {
    class OpenFaceScanner(val scannedCode: ScannedCode) : QrcodeAction()
    class SetupCamera(val imageAnalysis: ImageAnalysis) : QrcodeAction()
    class ShowGenericError(val throwable: Throwable) : QrcodeAction()
}
