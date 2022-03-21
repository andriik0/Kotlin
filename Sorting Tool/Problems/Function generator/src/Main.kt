// TODO: provide three functions here
fun identity(item: Int):Int = item.toInt()

fun half(item: Int):Int = (item / 2).toInt()

fun zero(item: Int):Int = 0.toInt()


fun generate(functionName: String): (Int) -> Int {
    // TODO: provide implementation here
    return when (functionName) {
        "identity" -> ::identity
        "half" -> ::half
        else -> ::zero
    }
}

