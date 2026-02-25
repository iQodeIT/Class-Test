package com.fairslice.logic

data class ParticipantIncome(
    val name: String,
    val income: Double,
    val type: IncomeType
)

enum class IncomeType {
    ANNUAL, MONTHLY, HOURLY
}

object SplitEngine {
    private const val HOURS_PER_YEAR = 2080.0
    private const val MONTHS_PER_YEAR = 12.0

    fun normalizeToAnnual(income: Double, type: IncomeType): Double {
        return when (type) {
            IncomeType.ANNUAL -> income
            IncomeType.MONTHLY -> income * MONTHS_PER_YEAR
            IncomeType.HOURLY -> income * HOURS_PER_YEAR
        }
    }

    fun calculateSplit(
        totalBillCents: Long,
        participants: List<ParticipantIncome>
    ): Map<String, Long> {
        if (participants.isEmpty()) return emptyMap()

        val normalizedIncomes = participants.map {
            it.name to normalizeToAnnual(it.income, it.type)
        }

        val totalIncome = normalizedIncomes.sumOf { it.second }
        if (totalIncome <= 0) {
            // If total income is 0, split equally
            val equalShare = totalBillCents / participants.size
            return participants.associate { it.name to equalShare }
        }

        return normalizedIncomes.associate { (name, income) ->
            name to ((income / totalIncome) * totalBillCents).toLong()
        }
    }
}
