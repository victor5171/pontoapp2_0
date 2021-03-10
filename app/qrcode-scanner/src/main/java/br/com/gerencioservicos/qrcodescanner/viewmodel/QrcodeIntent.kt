package br.com.gerencioservicos.qrcodescanner.viewmodel

import android.media.Image
import org.xtras.mvi.Intent

internal sealed class QrcodeIntent : Intent {
    data class DecodeImage(
        val image: Image,
        val angle: Int,
        val closeableResource: AutoCloseable
    ) : QrcodeIntent()

    object StartCamera : QrcodeIntent()

    object AcceptCode : QrcodeIntent()
}
