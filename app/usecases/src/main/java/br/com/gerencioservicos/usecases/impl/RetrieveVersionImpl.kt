package br.com.gerencioservicos.usecases.impl

import br.com.gerencioservicos.usecases.RetrieveVersion

internal class RetrieveVersionImpl(private val version: String) : RetrieveVersion {
    override fun invoke() = version
}
