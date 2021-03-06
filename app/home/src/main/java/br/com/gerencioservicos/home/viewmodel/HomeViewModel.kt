package br.com.gerencioservicos.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import br.com.gerencioservicos.usecases.GetPendingPermissions
import br.com.gerencioservicos.usecases.IsPermissionAllowed
import br.com.gerencioservicos.usecases.ListenToPermissionsAndWorklogs
import br.com.gerencioservicos.usecases.RetrieveVersion
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import org.xtras.mvi.JobTerminator
import org.xtras.mvi.Logger
import org.xtras.mvi.Reducer

internal class HomeViewModel(
    private val listenToPermissionsAndWorklogs: ListenToPermissionsAndWorklogs,
    private val isPermissionAllowed: IsPermissionAllowed,
    private val getPendingPermissions: GetPendingPermissions,
    private val retrieveVersion: RetrieveVersion,
    logger: Logger
) : ViewModel() {

    private val reducer = Reducer(
        coroutineScope = viewModelScope,
        initialState = HomeState.Loading,
        logger = logger,
        intentExecutor = this::executeIntent
    )

    val state = reducer.state

    init {
        execute(HomeIntent.Load)
    }

    fun execute(intent: HomeIntent) {
        reducer.executeIntent(intent)
    }

    private fun executeIntent(
        intent: HomeIntent,
        jobTerminator: JobTerminator<HomeIntent>
    ) = when (intent) {
        HomeIntent.Load -> executeLoad(jobTerminator)
        is HomeIntent.ClickedOnPermission -> executeClickedOnPermission(intent)
        HomeIntent.AddWorklog -> executeAddWorklog()
    }

    private fun executeLoad(jobTerminator: JobTerminator<HomeIntent>) = flow {
        jobTerminator.kill(HomeIntent.Load)

        try {
            emitAll(
                listenToPermissionsAndWorklogs().map {
                    val version = retrieveVersion()
                    HomeTransform.ChangePermissionsAndWorklogs(version, it)
                }
            )
        }
        catch(throwable: Throwable) {
            emit(HomeTransform.ShowFullscreenError(throwable))
        }
    }

    private fun executeClickedOnPermission(intent: HomeIntent.ClickedOnPermission) = flow {
        try {
            val isAllowed = isPermissionAllowed(intent.permissionType)
            if (!isAllowed) {
                emit(HomeTransform.AddAction(HomeAction.AskForPermission(intent.permissionType)))
            }
        } catch (exception: Exception) {
            emit(HomeTransform.AddAction(HomeAction.ShowGenericError(exception)))
            throw exception
        }
    }

    private fun executeAddWorklog() = flow {
        try {
            val pendingPermissions = getPendingPermissions()

            if (pendingPermissions.isEmpty()) {
                emit(HomeTransform.AddAction(HomeAction.OpenQrCodeScan))
                return@flow
            }

            emit(HomeTransform.AddAction(HomeAction.ShowWarningAboutPermissions(pendingPermissions)))
        } catch (exception: Exception) {
            emit(HomeTransform.AddAction(HomeAction.ShowGenericError(exception)))
            throw exception
        }
    }
}
