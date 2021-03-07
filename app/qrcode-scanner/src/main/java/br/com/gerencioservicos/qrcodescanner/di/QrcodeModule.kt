package br.com.gerencioservicos.qrcodescanner.di

import br.com.gerencioservicos.qrcodescanner.viewmodel.QrcodeViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

object QrcodeModule {
    val module = module {
        viewModel {
            QrcodeViewModel(
                tryDecodeImage = get(),
                logger = get()
            )
        }
    }
}
