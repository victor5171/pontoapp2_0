package br.com.gerencioservicos.home

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import br.com.gerencioservicos.home.ui.PermissionNameTranslator
import br.com.gerencioservicos.home.viewmodel.HomeAction
import br.com.gerencioservicos.home.viewmodel.HomeIntent
import br.com.gerencioservicos.home.viewmodel.HomeState
import br.com.gerencioservicos.home.viewmodel.HomeViewModel
import br.com.gerencioservicos.repository.permissions.PermissionType
import kotlinx.android.synthetic.main.fragment_home.*
import org.koin.android.viewmodel.ext.android.viewModel

internal class HomeFragment : Fragment(R.layout.fragment_home) {

    private val viewModel: HomeViewModel by viewModel()

    private val adapter = HomeAdapterBuilder.buildForListAdapter(this::onPermissionClick)

    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {}

    private val requestMultiplePermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
        if (it.values.all { accepted -> accepted }) {
            viewModel.execute(HomeIntent.AddWorklog)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        buttonAddWorklog.setOnClickListener {
            viewModel.execute(HomeIntent.AddWorklog)
        }

        viewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                HomeState.Loading -> {}
                is HomeState.Error -> {}
                is HomeState.Loaded -> setLoadedState(it)
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
        is HomeAction.ShowWarningAboutPermissions -> showWarningAboutPermissions(homeAction)
        is HomeAction.OpenQrCodeScan -> TODO()
        is HomeAction.ShowGenericError -> showGenericErrorMessage()
    }

    private fun showWarningAboutPermissions(showWarningAboutPermissions: HomeAction.ShowWarningAboutPermissions) {
        val permissionNames = PermissionNameTranslator.translateAllWithComma(requireContext(), showWarningAboutPermissions.permissionTypes)

        val permissionIds = showWarningAboutPermissions.permissionTypes.map(this::getPermissionId).toTypedArray()

        AlertDialog
            .Builder(requireContext())
            .setTitle(R.string.give_permissions_warning_title)
            .setMessage(getString(R.string.give_permissions_warning_text, permissionNames))
            .setNegativeButton(R.string.cancel, null)
            .setPositiveButton(R.string.ok) { _, _ ->
                requestMultiplePermissionLauncher.launch(permissionIds)
            }
            .show()
    }

    private fun showGenericErrorMessage() {
        AlertDialog
            .Builder(requireContext())
            .setTitle(R.string.error_title)
            .setMessage(R.string.error_message)
            .setPositiveButton(R.string.ok, null)
            .show()
    }

    private fun askForPermission(askForPermissionAction: HomeAction.AskForPermission) {
        val permissionId = getPermissionId(askForPermissionAction.permissionType)
        requestPermissionLauncher.launch(permissionId)
    }

    private fun getPermissionId(permissionType: PermissionType) = when (permissionType) {
        PermissionType.CAMERA -> Manifest.permission.CAMERA
        PermissionType.GPS -> Manifest.permission.ACCESS_FINE_LOCATION
    }

    private fun onPermissionClick(permissionType: PermissionType) {
        viewModel.execute(HomeIntent.ClickedOnPermission(permissionType))
    }
}
