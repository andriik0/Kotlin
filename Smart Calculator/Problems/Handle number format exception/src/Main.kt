fun parseCardNumber(cardNumber: String): Long {
    if (cardNumber.matches(Regex("\\d{4}\\s\\d{4}\\s\\d{4}\\s\\d{4}"))) {
    return cardNumber.replace(" ", "").toLong() }
    throw Exception("Invalid card number format")
}
