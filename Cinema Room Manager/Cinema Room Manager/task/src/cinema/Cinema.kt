package cinema

import kotlin.math.round


fun main() {
    val menu: Array<String> = arrayOf("Show the seats", "Buy a ticket", "Statistics")
    var bookedSeats = mutableListOf<String>()
    var currentIncome = 0
    println("Enter the number of rows:")
    val rows = readLine()!!.toInt()
    println("Enter the number of seats in each row:")
    val seatsInRow = readLine()!!.toInt()
    while (true) {
        showMenu(menu)
        when (readLine()!!.toInt()) {
            1 -> printSeatsMap(rows, seatsInRow, bookedSeats)
            2 -> currentIncome += buySeat(rows, seatsInRow, bookedSeats)
            3 -> showStatistic(rows, seatsInRow, bookedSeats.size, currentIncome)
            else -> break
        }
    }
}

private fun showStatistic(rows: Int, seatsInRow: Int, tickets_purchased: Int, incomes: Int) {
    println("Number of purchased tickets: $tickets_purchased")
    println(
        "Percentage: ${
            (round(tickets_purchased.toFloat() / (rows * seatsInRow).toFloat() * 10000.0).toFloat() / 100.0).toFloat()
                .format(2)
        }%"
    )
    println("Current income: \$${incomes}")
    println("Total income: \$${getIncomes(rows, seatsInRow)}")


}

private fun buySeat(rows: Int, seatsInRow: Int, booked: MutableList<String>): Int {
    while (true) {
        println()
        println("Enter a row number:")
        val row = readLine()!!.toInt()
        println("Enter a seat number in that row:")
        val seat = readLine()!!.toInt()
        if (row !in 1..rows || seat !in 1..seatsInRow) {
            println()
            println("Wrong input!")
            continue
        }
        val bookSeat = row.toString() + seat.toString()
        if (bookSeat in booked) {
            println()
            println("That ticket has already been purchased!")
            continue
        }
        println()
        val price = getSeatPrice(rows, seatsInRow, row)
        println("Ticket price : \$${price}")

        booked.add(bookSeat)
        return price
    }
}

private fun showMenu(menu: Array<String>) {
    var index = 1
    println()
    for (item in menu) {
        println("$index. ${item}")
        index++
    }
    println("0. Exit")
}


private fun printSeatsMap(rows: Int, seats: Int, booked: List<String>) {
    println()
    println("Cinema:")
    print("  ")
    for (j in 1..seats) {
        print("$j ")
    }
    println()
    for (i in 1..rows) {
        print("$i ")
        for (j in 1..seats) {
            val seat = i.toString() + j.toString()
            if (seat in booked) {
                print("B ")
                continue
            }
            print("S ")
        }
        println()
    }
}

private fun getIncomes(rows: Int, seatsInRow: Int): Int {
    return if (rows * seatsInRow <= 60) {
        rows * seatsInRow * 10
    } else {
        val frontRows = rows / 2
        val backRows = rows - frontRows
        frontRows * seatsInRow * 10 +
                backRows * seatsInRow * 8
    }

}

private fun getSeatPrice(rows: Int, seatsInRow: Int, row: Int): Int {
    return if (rows * seatsInRow <= 60) {
        10
    } else {
        val frontRows = rows / 2
        if (row <= frontRows) {
            10
        } else {
            8
        }
    }
}

fun Float.format(digits: Int) = "%.${digits}f".format(this)