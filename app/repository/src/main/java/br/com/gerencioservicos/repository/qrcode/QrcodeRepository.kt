package br.com.gerencioservicos.repository.qrcode

import android.media.Image

interface QrcodeRepository {
    suspend fun tryDecodeImage(image: Image, angle: Int, closeableResource: AutoCloseable): ScannedCode?
}
