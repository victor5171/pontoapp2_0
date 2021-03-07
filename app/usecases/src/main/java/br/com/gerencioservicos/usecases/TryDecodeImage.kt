package br.com.gerencioservicos.usecases

import android.media.Image
import br.com.gerencioservicos.repository.qrcode.ScannedCode

interface TryDecodeImage {
    suspend operator fun invoke(image: Image, angle: Int, closeableResource: AutoCloseable): ScannedCode?
}
