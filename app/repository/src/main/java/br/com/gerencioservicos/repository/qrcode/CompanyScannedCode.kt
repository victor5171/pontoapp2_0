package br.com.gerencioservicos.repository.qrcode

import kotlinx.parcelize.Parcelize

@Parcelize
internal data class CompanyScannedCode(
    val userId: Long,
    val companyId: String
) : ScannedCode
