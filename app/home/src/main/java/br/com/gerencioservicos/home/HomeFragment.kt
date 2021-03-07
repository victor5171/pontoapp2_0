package br.com.gerencioservicos.home

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.fragment.app.Fragment
import br.com.gerencioservicos.home.navigation.HomeNavigation
import br.com.gerencioservicos.home.ui.AddWorklogFAB
import br.com.gerencioservicos.home.ui.LoadedScreen
import br.com.gerencioservicos.home.ui.PermissionNameTranslator
import br.com.gerencioservicos.home.viewmodel.HomeAction
import br.com.gerencioservicos.home.viewmodel.HomeIntent
import br.com.gerencioservicos.home.viewmodel.HomeState
import br.com.gerencioservicos.home.viewmodel.HomeViewModel
import br.com.gerencioservicos.repository.permissions.PermissionType
import br.com.gerencioservicos.styles.compose.AppTheme
import br.com.gerencioservicos.styles.compose.widgets.AppAlertDialog
import br.com.gerencioservicos.styles.compose.widgets.ErrorScreen
import br.com.gerencioservicos.styles.compose.widgets.LoadingScreen
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

internal class HomeFragment : Fragment() {

    private val navigation: HomeNavigation by inject { parametersOf(this) }

    private val viewModel: HomeViewModel by viewModel()

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {}

    private val requestMultiplePermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
        if (it.values.all { accepted -> accepted }) {
            viewModel.execute(HomeIntent.AddWorklog)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(inflater.context).apply {
            setContent {
                AppTheme {
                    val state by viewModel.state.collectAsState()

                    Scaffold(
                        floatingActionButton = {
                            if (state is HomeState.Loaded) {
                                AddWorklogFAB(onClick = { viewModel.execute(HomeIntent.AddWorklog) })
                            }
                        }
                    ) {
                        @Suppress("UnnecessaryVariable")
                        when(val smartCastState = state) {
                            is HomeState.Error -> ErrorScreen(
                                retryOnClick = { viewModel.execute(HomeIntent.Load) }
                            )
                            is HomeState.Loaded -> LoadedScreen(
                                loadedState = smartCastState,
                                onItemSelected = { viewModel.execute(HomeIntent.ClickedOnPermission(it)) }
                            )
                            HomeState.Loading -> LoadingScreen()
                        }
                    }

                    (state as? HomeState.Loaded)?.actions?.forEach {
                        ParseAction(homeAction = it)
                    }
                }
            }
        }
    }

    @Composable
    private fun ParseAction(homeAction: HomeAction) = when (homeAction) {
        is HomeAction.AskForPermission -> askForPermission(homeAction)
        is HomeAction.ShowWarningAboutPermissions -> ShowWarningAboutPermissions(homeAction)
        is HomeAction.OpenQrCodeScan -> navigation.goToQrCode()
        is HomeAction.ShowGenericError -> ShowGenericErrorMessage(homeAction)
    }

    @Composable
    private fun ShowWarningAboutPermissions(showWarningAboutPermissions: HomeAction.ShowWarningAboutPermissions) {
        val permissionNames = PermissionNameTranslator.translateAllWithComma(requireContext(), showWarningAboutPermissions.permissionTypes)

        val permissionIds = showWarningAboutPermissions.permissionTypes.map(this::getPermissionId).toTypedArray()

        AppAlertDialog(
            key = showWarningAboutPermissions,
            title = stringResource(R.string.give_permissions_warning_title),
            message = stringResource(R.string.give_permissions_warning_text, permissionNames),
            confirmText = stringResource(R.string.ok),
            cancelText = stringResource(R.string.cancel),
            onConfirm = { requestMultiplePermissionLauncher.launch(permissionIds) }
        )
    }

    @Composable
    private fun ShowGenericErrorMessage(showGenericError: HomeAction.ShowGenericError) {
        AppAlertDialog(
            key = showGenericError,
            title = stringResource(R.string.error_title),
            message = stringResource(R.string.error_message),
            confirmText = stringResource(R.string.ok)
        )
    }

    private fun askForPermission(askForPermissionAction: HomeAction.AskForPermission) {
        val permissionId = getPermissionId(askForPermissionAction.permissionType)
        requestPermissionLauncher.launch(permissionId)
    }

    private fun getPermissionId(permissionType: PermissionType) = when (permissionType) {
        PermissionType.CAMERA -> Manifest.permission.CAMERA
        PermissionType.GPS -> Manifest.permission.ACCESS_FINE_LOCATION
    }
}
