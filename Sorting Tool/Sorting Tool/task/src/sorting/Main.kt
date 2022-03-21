package sorting

import java.io.File
import java.io.FileReader
import java.util.*
import kotlin.math.round
import kotlin.io.println as println1

fun main(args: Array<String>) {
    var sortingType = "natural"
    if ("-sortingType" in args) {
        val sortTypeIndex = args.indexOf("-sortingType")
        if (args.size > sortTypeIndex + 1 && args[sortTypeIndex + 1].trim() in setOf("byCount", "natural")) {
            sortingType = args[sortTypeIndex + 1].trim()
        } else {
            println1("No sorting type defined!")
            return
        }
    }
    var dataType = "word"
    if ("-dataType" in args) {
        val dataTypeIndex = args.indexOf("-dataType")
        if (args.size > dataTypeIndex + 1 && args[dataTypeIndex + 1].trim() in setOf("long",
                "word",
                "line")
        ) {
            dataType = args[dataTypeIndex + 1].trim()
        } else {
            println1("No data type defined!")
            return
        }
    }
    var readFromFile = false
    var readFileName = ""
    if ("-inputFile" in args) {
        val inputFileIndex = args.indexOf("-inputFile")
        if (args.size > inputFileIndex + 1 && args[inputFileIndex + 1].trim().first() != '-') {
            readFromFile = true
            readFileName = args[inputFileIndex + 1].trim()
        }
    }

    var writeToFile = false
    var writeFileName = ""
    if ("-outputFile" in args) {
        val inputFileIndex = args.indexOf("-outputFile")
        if (args.size > inputFileIndex + 1 && args[inputFileIndex + 1].trim().first() != '-') {
            writeToFile = true
            writeFileName = args[inputFileIndex + 1].trim()
        }
    }

    val unknownParams = args.filter { it.first() == '-' }
        .filter {
            it !in listOf(
                "-sortingType",
                "-dataType",
                "-inputFile",
                "-outputFile"
            )
        }

    if (unknownParams.isNotEmpty()) {
        for (item in unknownParams) {
            println1("\"$item\" isn not a valid parameter. It will be skipped.")
        }
    }

    when (dataType) {
        "long" -> longSort(
            sortingType,
            readFromFile,
            readFileName,
            writeToFile,
            writeFileName
        )
        else -> stringsSort(sortingType, dataType, readFromFile,
            readFileName,
            writeToFile,
            writeFileName)
    }
}

@Suppress("UNCHECKED_CAST")
private fun longSort(
    sortingType: String,
    readFromFile: Boolean,
    readFileName: String,
    writeToFile: Boolean,
    WriteFileName: String,
): Int {
    val str = mutableListOf<Long>()
    val scanner = if (readFromFile) {
        Scanner(FileReader(File(readFileName))).useDelimiter("\n")
    } else {
        Scanner(System.`in`)
    }
    while (scanner.hasNextLine()) {
        val input = scanner.nextLine()
        for (item in input.split(" ")) {
            if (item.contains(Regex("[^-*\\d+]"))) {
                println1("\"${item}\" is not a long. It will be skipped.")
                continue
            }
            if (item.isNotEmpty()) {
                str += item.toLong()
            }
        }
    }
    val outputFun: (String) -> Unit = if (writeToFile) File(WriteFileName)::appendText else ::println1

    outputFun("Total numbers: ${str.size}.")
    if (sortingType == "natural") {
        outputFun("Sorted data: " + str.sortedWith(compareBy { it }).joinToString(separator = " "))
        return 0
    }
    val sorted = str.groupingBy { it }.eachCount().entries.sortedWith(compareBy({ it.value }, { it.key }))
    for (mItem in sorted) {
        val item: Map.Entry<Double, Int> = mItem as Map.Entry<Double, Int>
        outputFun(
            "${item.key.toInt()}: ${item.value} time(s), " +
                    "${round(100 * item.value.toDouble() / str.size.toDouble()).toInt()}%"
        )
    }
    return 0
}

fun stringsSort(
    sortingType: String,
    dataType: String,
    readFromFile: Boolean,
    readFileName: String,
    writeToFile: Boolean,
    WriteFileName: String,
): Int {
    val str = mutableListOf<String>()
    val scanner = if (readFromFile) {
        Scanner(FileReader(File(readFileName))).useDelimiter("\n")
    } else {
        Scanner(System.`in`)
    }
    while (scanner.hasNextLine()) {
        val input = scanner.nextLine()

        if (dataType == "word") {
            str += input.split(' ').filter { it.isNotEmpty() }
        } else {
            str += input
        }


    }
    val outputFun: (String) -> Unit = if (writeToFile) File(WriteFileName)::appendText else ::println1
    outputFun("Total ${dataType}s: ${str.size}.")
    if (sortingType == "natural") {
        outputFun(
            "Sorted data: " + str.sorted().joinToString(
                separator = if (dataType == "word") {
                    " "
                } else {
                    "\n"
                }
            )
        )
        return 0
    }
    val sorted = str.groupingBy { it }.eachCount().entries.sortedWith(compareBy({ it.value }, { it.key }))
    for (mItem in sorted) {
        val item: Map.Entry<String, Int> = mItem
        outputFun(
            "${item.key}: ${item.value} time(s), " +
                    "${round(100 * item.value.toDouble() / str.size.toDouble()).toInt()}%"
        )
    }
    return 0
}

private fun intSort(): Int {
    val str = mutableListOf<Long>()
    val scanner = Scanner(System.`in`)
    while (scanner.hasNextLong()) {
        val input = scanner.nextLong()
        str += input
    }
    str.sort()
    println1("Total numbers: ${str.size}.")
    println1("Sorted data: " + str.joinToString(separator = " "))
    return 0
}