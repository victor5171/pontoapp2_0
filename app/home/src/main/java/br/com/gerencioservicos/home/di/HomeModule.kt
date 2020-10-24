package br.com.gerencioservicos.home.di

import br.com.gerencioservicos.home.viewmodel.HomeViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

object HomeModule {
    val module = module {
        viewModel {
            HomeViewModel(
                listenToPermissionsAndWorklogs = get(),
                isPermissionAllowed = get(),
                retrieveVersion = get(),
                getPendingPermissions = get()
            )
        }
    }
}
