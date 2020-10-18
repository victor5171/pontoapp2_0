package br.com.gerencioservicos.home

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import br.com.gerencioservicos.home.viewmodel.HomeAction
import br.com.gerencioservicos.home.viewmodel.HomeIntention
import br.com.gerencioservicos.home.viewmodel.HomeState
import br.com.gerencioservicos.home.viewmodel.HomeViewModel
import br.com.gerencioservicos.repository.permissions.PermissionType
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.flow.collect
import org.koin.android.viewmodel.ext.android.viewModel

internal class HomeFragment : Fragment(R.layout.fragment_home) {

    private val viewModel: HomeViewModel by viewModel()

    private val adapter = HomeAdapterBuilder.buildForListAdapter(this::onPermissionClick)

    private val requestCameraPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {}

    private val requestGpsPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.state.collect {
                when (it) {
                    HomeState.Loading -> {}
                    is HomeState.Error -> {}
                    is HomeState.Loaded -> setLoadedState(it)
                }
            }
        }
    }

    private fun setupRecyclerView() {
        recyclerView.adapter = adapter

        val divider = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        recyclerView.addItemDecoration(divider)
    }

    private fun setLoadedState(homeState: HomeState.Loaded) {
        adapter.submitList(homeState.homeListItems)

        @SuppressLint("SetTextI18n")
        textViewVersion.text = "v${homeState.version}"

        homeState.actions.forEach(this::parseAction)
    }

    private fun parseAction(homeAction: HomeAction) = when (homeAction) {
        is HomeAction.AskForPermission -> askForPermission(homeAction)
    }

    private fun askForPermission(askForPermissionAction: HomeAction.AskForPermission) = askForPermissionAction.executeAndConsume {
        when (askForPermissionAction.permissionType) {
            PermissionType.CAMERA -> requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            PermissionType.GPS -> requestGpsPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun onPermissionClick(permissionType: PermissionType) {
        viewModel.executeIntention(HomeIntention.ClickedOnPermission(permissionType))
    }
}
