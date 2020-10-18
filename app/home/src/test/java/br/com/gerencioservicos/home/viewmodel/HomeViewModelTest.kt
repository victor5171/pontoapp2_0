package br.com.gerencioservicos.home.viewmodel

import br.com.gerencioservicos.repository.permissions.Permission
import br.com.gerencioservicos.repository.permissions.PermissionType
import br.com.gerencioservicos.usecases.IsPermissionAllowed
import br.com.gerencioservicos.usecases.ListenToPermissionsAndWorklogs
import br.com.gerencioservicos.usecases.PermissionsAndWorklogs
import br.com.gerencioservicos.usecases.RetrieveVersion
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class HomeViewModelTest {

    @Before
    fun before() {
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @Test
    fun `When I instantiate a HomeViewModel and the permissions succeed to load, it should emit Loaded in the end`() {
        val permissionsAndWorklogsFlow = MutableSharedFlow<PermissionsAndWorklogs>()

        val listenToPermissionsAndWorklogs = ListenToPermissionsAndWorklogs { permissionsAndWorklogsFlow }

        val isPermissionAllowed = IsPermissionAllowed {
            throw Exception("This should not be called")
        }

        val retrieveVersion = RetrieveVersion {
            "1.0"
        }

        val homeViewModel = HomeViewModel(listenToPermissionsAndWorklogs, isPermissionAllowed, retrieveVersion)

        assertEquals(HomeState.Loading, homeViewModel.state.value)

        val permissionAndWorklogs = PermissionsAndWorklogs(
            permissions = listOf(
                Permission(PermissionType.CAMERA, true),
                Permission(PermissionType.GPS, true)
            ),
            numberOfPendingWorklogs = 1
        )

        runBlocking {
            permissionsAndWorklogsFlow.emit(permissionAndWorklogs)
        }

        val loadedHomeState = homeViewModel.state.value as HomeState.Loaded

        assertTrue {
            loadedHomeState.version == "1.0" &&
            loadedHomeState.homeListItems.toTypedArray().contentDeepEquals(
                arrayOf(
                    HomeListItem.PermissionListItem(Permission(PermissionType.CAMERA, true)),
                    HomeListItem.PermissionListItem(Permission(PermissionType.GPS, true)),
                    HomeListItem.PendingSynchronizationListItem(1)
                )
            ) && loadedHomeState.actions.toList().isEmpty()
        }
    }

    @Test
    fun `When I instantiate a HomeViewModel and the permissions fail to load, it should emit Error in the end`() {
        val mockedThrowable = Throwable()

        val listenToPermissionsAndWorklogs = ListenToPermissionsAndWorklogs {
            flow { throw mockedThrowable }
        }

        val isPermissionAllowed = IsPermissionAllowed {
            throw Exception("This should not be called")
        }

        val retrieveVersion = RetrieveVersion {
            throw Exception("This should not be called")
        }

        val homeViewModel = HomeViewModel(listenToPermissionsAndWorklogs, isPermissionAllowed, retrieveVersion)

        val errorHomeState = homeViewModel.state.value as HomeState.Error

        assertEquals(mockedThrowable, errorHomeState.throwable)
    }

    @Test
    fun `When I have a HomeViewModel and click on a not allowed permission, it should trigger an AskPermission action`() {
        val permissionAndWorklogs = PermissionsAndWorklogs(
            permissions = listOf(
                Permission(PermissionType.CAMERA, false)
            ),
            numberOfPendingWorklogs = 0
        )

        val listenToPermissionsAndWorklogs = ListenToPermissionsAndWorklogs {
            flowOf(permissionAndWorklogs)
        }

        val isPermissionAllowed = IsPermissionAllowed {
            if (it == PermissionType.CAMERA) {
                return@IsPermissionAllowed false
            }

            throw Exception("This should not be called")
        }

        val retrieveVersion = RetrieveVersion {
            "1.0"
        }

        val homeViewModel = HomeViewModel(listenToPermissionsAndWorklogs, isPermissionAllowed, retrieveVersion)

        homeViewModel.executeIntention(HomeIntention.ClickedOnPermission(PermissionType.CAMERA))

        val loadedHomeState = homeViewModel.state.value as HomeState.Loaded

        val actions = loadedHomeState.actions.toList()

        assertTrue {
            val firstAction = actions.first()
            firstAction is HomeAction.AskForPermission && firstAction.permissionType == PermissionType.CAMERA
        }
    }

    @Test
    fun `When I have a HomeViewModel and click on an allowed permission, it should not trigger an AskPermission action`() {
        val permissionAndWorklogs = PermissionsAndWorklogs(
            permissions = listOf(
                Permission(PermissionType.CAMERA, true)
            ),
            numberOfPendingWorklogs = 0
        )

        val listenToPermissionsAndWorklogs = ListenToPermissionsAndWorklogs {
            flowOf(permissionAndWorklogs)
        }

        val isPermissionAllowed = IsPermissionAllowed {
            if (it == PermissionType.CAMERA) {
                return@IsPermissionAllowed true
            }

            throw Exception("This should not be called")
        }

        val retrieveVersion = RetrieveVersion {
            "1.0"
        }

        val homeViewModel = HomeViewModel(listenToPermissionsAndWorklogs, isPermissionAllowed, retrieveVersion)

        homeViewModel.executeIntention(HomeIntention.ClickedOnPermission(PermissionType.CAMERA))

        val loadedHomeState = homeViewModel.state.value as HomeState.Loaded

        assertTrue {
            loadedHomeState.actions.toList().isEmpty()
        }
    }
}
