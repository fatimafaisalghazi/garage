package language




import java.util.*

class LocalizationManager(private val localeCode: String) {
    private val bundle: ResourceBundle

    init {
        val locale = Locale(localeCode)
        bundle = try {
            ResourceBundle.getBundle("strings", locale)
        } catch (e: MissingResourceException) {
            println("⚠️ Missing resource for '$localeCode', falling back to English.")
            ResourceBundle.getBundle("strings", Locale("en"))
        }
    }

    fun get(key: String): String {
        return try {
            bundle.getString(key)
        } catch (e: MissingResourceException) {
            "❓[$key]"
        }
    }
}

fun main() {
    print("Choose language: [en/ar]\n> ")
    val choice = readlnOrNull()?.lowercase()

    val languageCode = when (choice) {
        "ar" -> "ar"
        "en" -> "en"
        else -> {
            println("⚠️ Invalid input. Defaulting to English.")
            "en"
        }
    }

    val localization = LocalizationManager(languageCode)
    println(localization.get("welcome"))
}

