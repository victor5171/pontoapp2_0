package br.com.gerencioservicos.usecases

fun interface RetrieveVersion {
    operator fun invoke(): String
}
