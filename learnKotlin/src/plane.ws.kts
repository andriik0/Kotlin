open class Vehicle(val name: String, val wheelsNum: Int = 0)

open class Car(name: String, wheelsNum: Int, val spareWheelsNum: Int = 0)
    : Vehicle(name, wheelsNum)

open class Truck(name: String, wheelsNum: Int, spareWheelsNum: Int, val type: String)
    : Car(name, wheelsNum, spareWheelsNum)

fun wheelsNum(vehicle: Vehicle): Int {
    return if (vehicle.wheelsNum < 0) 0 else vehicle.wheelsNum
}

fun totalWheelsNum(car: Car): Int {
    return car.wheelsNum + car.spareWheelsNum
}

fun spareWheelsNum(car: Car): Int {
    return totalWheelsNum(car) - wheelsNum(car)
}

fun main() {
    val car = Car("Edison", 4, 2)
    val truck = Truck("Riebhell", 8, 2, "Heavy")
    println(spareWheelsNum(car) + spareWheelsNum(truck))
}