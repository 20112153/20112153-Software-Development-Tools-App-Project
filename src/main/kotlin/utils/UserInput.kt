package utils

/** Returns null when standard input is closed, instead of crashing the menu. */
fun readText(prompt: String): String? {
    print(prompt)
    return readlnOrNull()?.trim()
}

fun readInt(prompt: String): Int? = readText(prompt)?.toIntOrNull()
