package br.com.gerencioservicos.qrcodescanner.viewmodel

import android.annotation.SuppressLint
import androidx.camera.core.ImageAnalysis
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.gerencioservicos.usecases.TryDecodeImage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.xtras.mvi.Actions
import org.xtras.mvi.JobTerminator
import org.xtras.mvi.Logger
import org.xtras.mvi.Reducer
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

internal class QrcodeViewModel(
    private val tryDecodeImage: TryDecodeImage,
    logger: Logger
) : ViewModel() {

    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    private val imageAnalysis = ImageAnalysis.Builder()
        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
        .build()

    private val reducer = Reducer(
        coroutineScope = viewModelScope,
        initialState = QrcodeState.Loading(Actions()),
        logger = logger,
        intentExecutor = this::executeIntent
    )

    val state = reducer.state

    init {
        execute(QrcodeIntent.StartCamera)
    }

    fun execute(intent: QrcodeIntent) {
        reducer.executeIntent(intent)
    }

    private fun executeIntent(
        intent: QrcodeIntent,
        jobTerminator: JobTerminator<QrcodeIntent>
    ) = when (intent) {
        is QrcodeIntent.DecodeImage -> executeDecodeImage(intent)
        QrcodeIntent.StartCamera -> executeStartCamera()
        QrcodeIntent.AcceptCode -> executeAcceptCode()
    }

    private fun executeDecodeImage(intent: QrcodeIntent.DecodeImage) = flow<QrcodeTransform> {
        val scannedCode = tryDecodeImage(intent.image, intent.angle, intent.closeableResource)
            ?: return@flow

        imageAnalysis.clearAnalyzer()

        emit(QrcodeTransform.SetScannedCode(imageAnalysis, scannedCode))
    }

    private fun executeStartCamera() = flow<QrcodeTransform> {
        setAnalyser()
        emit(QrcodeTransform.SetCapturing(imageAnalysis))
    }

    private fun executeAcceptCode(): Flow<QrcodeTransform> = flow<QrcodeTransform> {
        val capturedCodeState: QrcodeState.ShowingCamera.CapturedCode = reducer.requireState()
        emit(QrcodeTransform.AddAction(QrcodeAction.OpenFaceScanner(capturedCodeState.scannedCode)))
    }

    @SuppressLint("UnsafeExperimentalUsageError")
    private fun setAnalyser() {
        imageAnalysis.setAnalyzer(executorService) {
            val image = it.image
            if (image != null) {
                execute(
                    QrcodeIntent.DecodeImage(
                        image,
                        it.imageInfo.rotationDegrees,
                        it
                    )
                )
            }
        }
    }

    override fun onCleared() {
        executorService.shutdown()
    }
}
