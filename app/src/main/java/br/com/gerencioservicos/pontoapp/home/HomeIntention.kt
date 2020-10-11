package br.com.gerencioservicos.pontoapp.home

import org.xtras.mvi.Intention

internal sealed class HomeIntention : Intention {
    object Load : HomeIntention()
}
