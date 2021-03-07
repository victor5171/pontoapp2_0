package br.com.gerencioservicos.usecases.impl

import android.media.Image
import br.com.gerencioservicos.repository.qrcode.QrcodeRepository
import br.com.gerencioservicos.repository.qrcode.ScannedCode
import br.com.gerencioservicos.usecases.TryDecodeImage

internal class TryDecodeImageImpl(
    private val qrcodeRepository: QrcodeRepository
) : TryDecodeImage {

    override suspend fun invoke(
        image: Image,
        angle: Int,
        closeableResource: AutoCloseable
    ): ScannedCode? {
        return qrcodeRepository.tryDecodeImage(image, angle, closeableResource)
    }
}
