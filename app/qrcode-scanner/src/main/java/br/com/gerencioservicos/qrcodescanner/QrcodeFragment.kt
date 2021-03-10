package br.com.gerencioservicos.qrcodescanner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import br.com.gerencioservicos.qrcodescanner.ui.CapturedCodeScreen
import br.com.gerencioservicos.qrcodescanner.ui.CapturingScreen
import br.com.gerencioservicos.qrcodescanner.viewmodel.QrcodeAction
import br.com.gerencioservicos.qrcodescanner.viewmodel.QrcodeIntent
import br.com.gerencioservicos.qrcodescanner.viewmodel.QrcodeState
import br.com.gerencioservicos.qrcodescanner.viewmodel.QrcodeViewModel
import br.com.gerencioservicos.styles.compose.AppTheme
import br.com.gerencioservicos.styles.compose.widgets.LoadingScreen
import org.koin.android.viewmodel.ext.android.viewModel

class QrcodeFragment : Fragment() {

    private val viewModel: QrcodeViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(inflater.context).apply {
            setContent {
                AppTheme {
                    val state by viewModel.state.collectAsState()

                    @Suppress("UnnecessaryVariable")
                    when (val smartCastState = state) {
                        is QrcodeState.ShowingCamera -> {
                            Box {
                                CapturingScreen(imageAnalysis = smartCastState.imageAnalysis)
                                if (smartCastState is QrcodeState.ShowingCamera.CapturedCode) {
                                    CapturedCodeScreen(
                                        onSubmit = { viewModel.execute(QrcodeIntent.AcceptCode) },
                                        onCancel = { viewModel.execute(QrcodeIntent.StartCamera) }
                                    )
                                }
                            }
                        }
                        is QrcodeState.Loading -> LoadingScreen()
                    }

                    state.actions.forEach {
                        ParseAction(action = it)
                    }
                }
            }
        }
    }

    @Composable
    private fun ParseAction(action: QrcodeAction): Unit = when (action) {
        is QrcodeAction.OpenFaceScanner -> TODO()
    }
}
