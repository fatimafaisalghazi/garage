package search


class FuzzyCorrection(private val availableNames: List<String>) {

    // ترجع أقرب اسم مشابه من القائمة
    fun correct(input: String): String {
        return availableNames.minByOrNull { levenshtein(it.lowercase(), input.lowercase()) } ?: input
    }

    private fun levenshtein(lhs: String, rhs: String): Int {
        val dp = Array(lhs.length + 1) { IntArray(rhs.length + 1) }
        for (i in 0..lhs.length) {
            for (j in 0..rhs.length) {
                dp[i][j] = when {
                    i == 0 -> j
                    j == 0 -> i
                    lhs[i - 1] == rhs[j - 1] -> dp[i - 1][j - 1]
                    else -> 1 + minOf(dp[i - 1][j], dp[i][j - 1], dp[i - 1][j - 1])
                }
            }
        }
        return dp[lhs.length][rhs.length]
    }
}
