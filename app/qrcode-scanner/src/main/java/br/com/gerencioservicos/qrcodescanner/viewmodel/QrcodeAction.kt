package br.com.gerencioservicos.qrcodescanner.viewmodel

import br.com.gerencioservicos.repository.qrcode.ScannedCode

internal sealed class QrcodeAction {
    class OpenFaceScanner(val scannedCode: ScannedCode) : QrcodeAction()
}
