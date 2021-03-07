package br.com.gerencioservicos.navigation.home

import androidx.navigation.NavController
import br.com.gerencioservicos.home.navigation.HomeNavigation
import br.com.gerencioservicos.navigation.R

internal class HomeNavigationImpl(private val navController: NavController) : HomeNavigation {

    override fun goToQrCode() {
        navController.navigate(R.id.action_homeFragment_qrcodeFragment)
    }
}
