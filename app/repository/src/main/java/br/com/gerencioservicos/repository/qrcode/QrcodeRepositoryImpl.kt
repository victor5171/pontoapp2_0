package br.com.gerencioservicos.repository.qrcode

import android.media.Image
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.tasks.await

internal class QrcodeRepositoryImpl : QrcodeRepository {

    private val options = BarcodeScannerOptions.Builder()
        .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
        .build()

    private val barcodeScanner = BarcodeScanning.getClient(options)

    override suspend fun tryDecodeImage(image: Image, angle: Int, closeableResource: AutoCloseable): ScannedCode? {
        try {
            val inputImage = InputImage.fromMediaImage(image, angle)

            val barcodes = barcodeScanner.process(inputImage).await()

            val firstBarcode = barcodes.firstOrNull() ?: return null

            return CompanyScannedCode(1, "1")
        }
        finally {
            closeableResource.close()
        }
    }
}
