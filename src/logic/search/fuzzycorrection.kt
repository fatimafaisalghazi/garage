package logic.search

class FuzzyCorrection(private val validList: List<String>) {

    fun correct(input: String): String? {
        val cleanedInput = input.trim().lowercase()

        val bestMatch = validList.minByOrNull { levenshteinDistance(it.lowercase(), cleanedInput) }
        val bestDistance = bestMatch?.let { levenshteinDistance(it.lowercase(), cleanedInput) }

        val threshold = 3 // إذا كان الفرق أكثر من 3 حروف، نعتبره غير مقبول

        return if (bestDistance != null && bestDistance <= threshold) bestMatch else null
    }

    private fun levenshteinDistance(lhs: String, rhs: String): Int {
        val lhsLength = lhs.length
        val rhsLength = rhs.length

        val dp = Array(lhsLength + 1) { IntArray(rhsLength + 1) }

        for (i in 0..lhsLength) dp[i][0] = i
        for (j in 0..rhsLength) dp[0][j] = j

        for (i in 1..lhsLength) {
            for (j in 1..rhsLength) {
                val cost = if (lhs[i - 1] == rhs[j - 1]) 0 else 1
                dp[i][j] = minOf(
                    dp[i - 1][j] + 1,
                    dp[i][j - 1] + 1,
                    dp[i - 1][j - 1] + cost
                )
            }
        }

        return dp[lhsLength][rhsLength]
    }
}
