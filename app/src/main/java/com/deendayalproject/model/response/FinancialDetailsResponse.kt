package com.deendayalproject.model.response

data class FinancialDetailsResponse(
    val annualTurnover: List<AnnualTurnover>?,
    val netWorth: List<NetWorth>?
)

data class AnnualTurnover(
    val year: String? = null,
    val turnoverAmount: Double?,
    var balanceSheetAttachment: String? = null
)

data class NetWorth(
    val year: String? = null,
    val turnoverAmount: Double?,
    var sfp18Attachment: String? = null
)

// wrapper to represent any year-wise financial item (annual turnover / net worth)
data class YearlyFinancialItem(
    val year: String?,
    val amount: Double?,
    val attachmentBase64: String?   // the base64 file (pdf/image) if present
)

fun com.deendayalproject.model.response.AnnualTurnover.toYearlyItem(): YearlyFinancialItem {
    return YearlyFinancialItem(
        year = this.year,
        amount = this.turnoverAmount,
        attachmentBase64 = this.balanceSheetAttachment
    )
}

fun com.deendayalproject.model.response.NetWorth.toYearlyItem(): YearlyFinancialItem {
    return YearlyFinancialItem(
        year = this.year,
        amount = this.turnoverAmount,
        attachmentBase64 = this.sfp18Attachment
    )
}