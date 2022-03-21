package sorting

import java.io.File

fun  lengthLongestLine() {
    val fileName = "/Users/andreytp/Downloads/words_sequence.txt"
    println(File(fileName).useLines { it.toList() }.maxByOrNull { it.length }.toString().length )

}

fun _main() {
    val fileName = "/Users/andreytp/Downloads/words_with_numbers.txt"
//    File(fileName).forEachLine { println(it) }
   val listNum = File(fileName).useLines { it -> it.toList() }.filter { it.first().isDigit() }.sumOf { it.toInt() }
    println(listNum)
}

fun main1() {
    val fileName = "/Users/andreytp/Library/Application Support/JetBrains/IdeaIC2020.3/scratches/scratch.txt"
    val linesLength = File(fileName).length()
    val lines = File(fileName).readLines().size
    print("$linesLength $lines")
}

fun main2() {
    val fileName = "MyFile.txt"
    val myFile = File(fileName)

    myFile.writeText("1")
    repeat(2) {
        myFile.appendText("\n2")
    }
    print(myFile.readText())
}