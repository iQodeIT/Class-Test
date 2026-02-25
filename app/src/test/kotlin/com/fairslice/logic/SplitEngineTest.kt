package com.fairslice.logic

import org.junit.Assert.assertEquals
import org.junit.Test

class SplitEngineTest {

    @Test
    fun `test proportional split with different incomes`() {
        val bill = 35000L // $350.00
        val participants = listOf(
            ParticipantIncome("Alice", 100000.0, IncomeType.ANNUAL),
            ParticipantIncome("Bob", 50000.0, IncomeType.ANNUAL),
            ParticipantIncome("Carol", 25000.0, IncomeType.ANNUAL)
        )

        val results = SplitEngine.calculateSplit(bill, participants)

        assertEquals(20000L, results["Alice"] ?: 0L)
        assertEquals(10000L, results["Bob"] ?: 0L)
        assertEquals(5000L, results["Carol"] ?: 0L)
    }

    @Test
    fun `test normalization from monthly and hourly`() {
        val bill = 30000L
        val participants = listOf(
            ParticipantIncome("Alice", 10000.0, IncomeType.MONTHLY), // 120k annual
            ParticipantIncome("Bob", 60000.0, IncomeType.ANNUAL)      // 60k annual
        )

        val results = SplitEngine.calculateSplit(bill, participants)

        // Total annual income = 180k. Alice is 2/3, Bob is 1/3.
        assertEquals(20000L, results["Alice"] ?: 0L)
        assertEquals(10000L, results["Bob"] ?: 0L)
    }

    @Test
    fun `test zero total income results in equal split`() {
        val bill = 10000L
        val participants = listOf(
            ParticipantIncome("Alice", 0.0, IncomeType.ANNUAL),
            ParticipantIncome("Bob", 0.0, IncomeType.ANNUAL)
        )

        val results = SplitEngine.calculateSplit(bill, participants)

        assertEquals(5000L, results["Alice"] ?: 0L)
        assertEquals(5000L, results["Bob"] ?: 0L)
    }
}
