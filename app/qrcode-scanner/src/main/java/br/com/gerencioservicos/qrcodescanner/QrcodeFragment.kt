package br.com.gerencioservicos.qrcodescanner

import android.os.Bundle
import android.view.View
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import br.com.gerencioservicos.qrcodescanner.viewmodel.QrcodeAction
import br.com.gerencioservicos.qrcodescanner.viewmodel.QrcodeIntent
import br.com.gerencioservicos.qrcodescanner.viewmodel.QrcodeState
import br.com.gerencioservicos.qrcodescanner.viewmodel.QrcodeViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import org.xtras.mvi.Actions

class QrcodeFragment : Fragment(R.layout.fragment_qrcode) {

    private val viewModel: QrcodeViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                is QrcodeState.CapturedCode -> {
                }
                is QrcodeState.Capturing -> {
                }
            }

            handleActions(view, it.actions)
        }

        viewModel.execute(QrcodeIntent.SetupCamera)
    }

    private fun handleActions(view: View, actions: Actions<QrcodeAction>) = actions.forEach {
        when (it) {
            is QrcodeAction.OpenFaceScanner -> TODO()
            is QrcodeAction.SetupCamera -> setupCamera(view, it.imageAnalysis)
            is QrcodeAction.ShowGenericError -> TODO()
        }
    }

    private fun setupCamera(view: View, imageAnalysis: ImageAnalysis) {
        val prvPreviewCode = view.findViewById<PreviewView>(R.id.prvPreviewCode)
        val context = requireContext()

        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(prvPreviewCode.surfaceProvider)
                }

            try {
                cameraProvider.unbindAll()

                cameraProvider.bindToLifecycle(
                    viewLifecycleOwner,
                    CameraSelector.DEFAULT_BACK_CAMERA,
                    preview,
                    imageAnalysis
                )
            } catch (exc: Exception) {
                throw exc
            }
        }, ContextCompat.getMainExecutor(context))
    }
}
