package br.com.gerencioservicos.navigation.di

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import br.com.gerencioservicos.home.navigation.HomeNavigation
import br.com.gerencioservicos.navigation.home.HomeNavigationImpl
import org.koin.dsl.module

object NavigationModule {
    val module = module {
        factory<HomeNavigation> { (fragment: Fragment) -> HomeNavigationImpl(fragment.findNavController()) }
    }
}
