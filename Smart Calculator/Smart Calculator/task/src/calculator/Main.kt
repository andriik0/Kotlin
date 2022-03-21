package calculator


fun main() {
    val vars = mutableMapOf<String, String>()
    while (true) {
        val str: String = readLine()!!.trim()

        if (str.isEmpty())
            continue

        if ("/exit" in str) {
            println("Bye!")
            break
        }

        if ("/help" in str) {
            println("The program calculates the sum of numbers")
            continue
        }

        if ('/' == str.first()) {
            println("Unknown command")
            continue
        }

        if ("=" in str) {
            if (varsAssignation(str, vars)) continue
        }
        if (expressionValidation(str, vars)) continue
        val numbers: String = expressionNormalize(str)
        val list = getList(numbers)
        val numbersStr = list.filter { element -> element.trim() != "" }
            .fold("") { numStr, element -> numStr + getVal(element.trim(), vars) }
        if (numbersStr.contains(Regex("[A-Za-z]+"))) {
            println("Unknown variable")
            continue
        }
        val mList: MutableList<String> = toPostfix(list, vars)
        println(calculatePostfix(mList))
    }
}

private fun calculatePostfix(mList: MutableList<String>): String {
    val stack: ArrayDeque<String> = ArrayDeque()
    for (item in mList) {
        if (item in "^*/+-") {
            val second = stack.removeLast().toBigInteger()
            val first = stack.removeLast().toBigInteger()
            val res = when (item) {
                "^" -> first.pow(second.toInt()).toString()
                "*" -> (first * second).toString()
                "/" -> if (second == "0".toBigInteger()) {
                    "0"
                } else {
                    (first / second).toString()
                }
                "+" -> (first + second).toString()
                "-" -> (first - second).toString()
                else -> "0"
            }
            stack.addLast(res)
        } else {
            stack.addLast(item)
        }
    }

    return stack.removeLast()
}

private fun toPostfix(
    list: List<String>,
    vars: MutableMap<String, String>
): MutableList<String> {
    val operatorsPriority: Map<String, Int> = mapOf("^" to 3, "*" to 2, "/" to 2, "+" to 1, "-" to 1)
    val stack: ArrayDeque<String> = ArrayDeque()
    val mList: MutableList<String> = mutableListOf()
    for (item in list) {
        if (item in "^*/+-") {
            if (stack.isEmpty() || stack.last() == "(") {
                stack.addLast(item)
            } else {
                if (operatorsPriority.getOrDefault(item, -1) > operatorsPriority.getOrDefault(stack.last(), -1)) {
                    stack.addLast(item)
                } else {
                    val operatorsStack: ArrayDeque<String> = ArrayDeque()
                    while (operatorsPriority.getOrDefault(item, -1) <= operatorsPriority.getOrDefault(
                            stack.last(),
                            -1
                        ) &&
                        stack.last() != "("
                    ) {
                        mList.add(stack.removeLast())
                        if (stack.isEmpty())
                            break
                    }
                    stack.addLast(item)
                    while (!operatorsStack.isEmpty()) {
                        stack.addLast(operatorsStack.removeLast())
                    }
                }
            }
        } else if (item == ")") {
            while (stack.last() != "(") {
                mList.add(stack.removeLast())
            }
            stack.removeLast()
        } else if (item == "(") {
            stack.addLast(item)
        } else {
            mList.add(getVal(item, vars))
        }
    }
    while (!stack.isEmpty()) {
        mList.add(stack.removeLast())
    }
    return mList
}

private fun getList(numbers: String): List<String> {
    return numbers
        .replace("^", " ^ ")
        .replace("(", " ( ")
        .replace(")", " ) ")
        .replace("*", " * ")
        .replace("/", " / ")
        .replace("+", " + ")
        .replace("-", " - ")
        .split(' ')
        .map { element -> element.trim() }
        .filter { element -> element != "" }
}

private fun expressionNormalize(str: String): String {
    var numbers: String = str
    while (numbers.contains("--") ||
        numbers.contains("++") ||
        numbers.contains("+-") ||
        numbers.contains("-+") ||
        numbers.contains("* ") ||
        numbers.contains("/ ") ||
        numbers.contains("  ") ||
        numbers.contains(" ") ||
        numbers.contains(") ") ||
        numbers.contains("( ") ||
        numbers.contains("^ ") ||
        numbers.contains("- ")
    ) {
        numbers = numbers.replace("--", "+")
        numbers = numbers.replace("++", "+")
        numbers = numbers.replace("+-", "-")
        numbers = numbers.replace("-+", "-")
        numbers = numbers.replace("  ", " ")
        numbers = numbers.replace(" ", "")
        numbers = numbers.replace("- ", "-")
        numbers = numbers.replace("/ ", "/")
        numbers = numbers.replace("* ", "*")
        numbers = numbers.replace("( ", "(")
        numbers = numbers.replace(") ", ")")
        numbers = numbers.replace("^ ", "^")
    }
    if (numbers.first() in "-+/*") {
        numbers = "0$numbers"
    }
    return numbers
}

private fun expressionValidation(
    str: String,
    vars: MutableMap<String, String>
): Boolean {
    val r11 = "[0-9]+\\s+[0-9]+"
    if (str.contains(Regex(r11))) {
        println("Invalid expression")
        return true
    }
    val r12 = "[0-9]*\\s*-+\\+*\$"
    if (str.contains(Regex(r12))) {
        println("Invalid expression")
        return true
    }

    val r13 = "^\\s*[^0-9+\\-\\s]\\s*$"
    if (str.matches(Regex(r13))) {
        val res = vars[str]
        if (res == null) {
            println("Unknown variable")
        } else {
            println(res)
        }
        return true
    }

    val r14 = "[0-9]*\\s*-*\\++\$"
    if (str.matches(Regex(r14))) {
        println("Invalid expression")
        return true
    }
    val r141 = "\\*{2,}"
    if (str.contains(Regex(r141))) {
        println("Invalid expression")
        return true
    }
    val r142 = "/{2,}"
    if (str.contains(Regex(r142))) {
        println("Invalid expression")
        return true
    }


    if (str.filter { it == '(' }.count() != str.filter { it == ')' }.count()) {
        println("Invalid expression")
        return true
    }
    return false
}

private fun varsAssignation(
    str: String,
    vars: MutableMap<String, String>
): Boolean {
    if (str.split("=").size > 2) {
        println("Invalid assignment")
        return true
    }

    val r0 = "^[A-Za-z]+\\s*=\\s*-*[0-9]+\\s*$"
    if (str.contains(Regex(r0))) {
        val varLst = str.split(Regex("\\s*=\\s*")).toList()
        vars[varLst[0].trim()] = varLst[1].trim()
        return true
    }
    val r01 = "^[A-Za-z]+\\s*=\\s*-*[A-Za-z]+\\s*$"
    if (str.contains(Regex(r01))) {
        val varLst = str.split(Regex("\\s*=\\s*")).toList()
        val varVal = vars[varLst[1].trim()]
        if (varVal == null) {
            println("Unknown variable")
        } else {
            vars[varLst[0].trim()] = varVal
        }
        return true
    }


    val r3 = "^\\s*[A-Za-z0-9]+\\s*="
    val r31 = "=\\s*[0-9]+$"
    if (str.contains(Regex(r3))) {
        if (str.contains(Regex(r31))) {
            println("Invalid identifier")
        } else {
            println("Invalid assignment")
        }
        return true
    }

    if (str.contains(Regex(r31))) {
        println("Invalid assignment")
        return true
    }

    val r4 = "=\\s*[A-Za-z0-9]+\\s*$"
    if (str.contains(Regex(r4))) {
        println("Invalid assignment")
        return true
    }

    val r2 = "^\\s*[A-Za-z]+\\s*=\\s*-*[A-Za-z]+\$"
    if (str.contains(Regex(r2))) {
        val varLst = str.split(Regex("\\s*=\\s*")).toList()
        val varVal = vars[varLst[1].trim()]
        if (varVal == null) {
            println("Unknown variable")
            return true
        }
        vars[varLst[0].trim()] = varVal.trim()
        return true
    }
    return false
}

fun getVal(elStr: String, vars: Map<String, String>): String {
    return if (elStr[0] == '-') {
        "-" + vars.getOrDefault(elStr.substring(1), elStr.substring(1))
    } else {
        vars.getOrDefault(elStr, elStr)
    }
}