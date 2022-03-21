package seamcarving

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import kotlin.math.max
import kotlin.math.pow
import javax.imageio.ImageIO as imIO

const val PARAMS_COUNT: Int = 4

fun main(args: Array<String>) {

    if (findSeam(args)) return
//    if (calcPixelEnergy(args)) return
//    if (negativeImage(args)) return

//    drawRectangle()

}

private fun getPixelEnergy(x: Int, y: Int, image: BufferedImage): Double {
    val xForXGradient: Int = when (x) {
        0 -> x + 1
        image.width - 1 -> x - 1
        else -> x
    }

    val yForYGradient: Int = when (y) {
        0 -> y + 1
        image.height - 1 -> y - 1
        else -> y
    }

    val yForXGradient: Int = y

    val xForYGradient: Int = x

    val xDiffPrevPointColor = Color(image.getRGB(xForXGradient - 1, yForXGradient))
    val xDiffNextPointColor = Color(image.getRGB(xForXGradient + 1, yForXGradient))
    val yDiffPrevPointColor = Color(image.getRGB(xForYGradient, yForYGradient - 1))
    val yDiffNextPointColor = Color(image.getRGB(xForYGradient, yForYGradient + 1))
    val xDifferences =
        (xDiffPrevPointColor.red - xDiffNextPointColor.red).toDouble().pow(2) +
                (xDiffPrevPointColor.green - xDiffNextPointColor.green).toDouble().pow(2) +
                (xDiffPrevPointColor.blue - xDiffNextPointColor.blue).toDouble().pow(2)
    val yDifferences =
        (yDiffPrevPointColor.red - yDiffNextPointColor.red).toDouble().pow(2) +
                (yDiffPrevPointColor.green - yDiffNextPointColor.green).toDouble().pow(2) +
                (yDiffPrevPointColor.blue - yDiffNextPointColor.blue).toDouble().pow(2)

    return (xDifferences + yDifferences).pow(0.5)

}

private fun findSeam(args: Array<String>): Boolean {
    if (args.size != PARAMS_COUNT) {
        println("Not enough parameters")
        return true
    }

    val inFile = File(args[args.indexOf("-in") + 1])
    val outFile = File(args[args.indexOf("-out") + 1])
    val sourceImage = imIO.read(inFile)
    val width = sourceImage.width
    val height = sourceImage.height
    for (j in 0 until height) {
        var minEnergy: Double = Double.MAX_VALUE
        var minEnergyI: Int = -1
        for (i in 0 until width) {
            val energy: Double = getPixelEnergy(i, j, sourceImage)
            if (energy < minEnergy) {
                minEnergy = energy
                minEnergyI = i
            }
        }
        sourceImage.setRGB(minEnergyI, j, Color(255, 0, 0).rgb)
    }
    imIO.write(sourceImage, "PNG", outFile)
    return false
}

private fun calcPixelEnergy(args: Array<String>): Boolean {
    if (args.size != PARAMS_COUNT) {
        println("Not enough parameters")
        return true
    }

    val inFile = File(args[args.indexOf("-in") + 1])
    val outFile = File(args[args.indexOf("-out") + 1])
    val sourceImage = imIO.read(inFile)
    val width = sourceImage.width
    val height = sourceImage.height
    val outputImage = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
    var maxEnergy: Double = Double.MIN_VALUE
    val pixelEnergyMap = mutableMapOf<Pair<Int, Int>, Double>()
    for (i in 0 until width) {
        for (j in 0 until height) {
            val energy: Double = getPixelEnergy(i, j, sourceImage)
            pixelEnergyMap[Pair(i, j)] = energy
            maxEnergy = max(energy, maxEnergy)
        }
    }
    for (item in pixelEnergyMap) {
        val intensity = (255.0 * item.value / maxEnergy).toInt()
        val (x, y) = item.key
        outputImage.setRGB(x, y, Color(intensity, intensity, intensity).rgb)
    }
    imIO.write(outputImage, "PNG", outFile)
    return false
}

private fun negativeImage(args: Array<String>): Boolean {
    if (args.size != PARAMS_COUNT) {
        println("Not enough parameters")
        return true
    }

    val inFile = File(args[args.indexOf("-in") + 1])
    val outFile = File(args[args.indexOf("-out") + 1])
    val sourceImage = imIO.read(inFile)
    val width = sourceImage.width
    val height = sourceImage.height
    val outputImage = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
    for (i in 0 until width) {
        for (j in 0 until height) {
            val pixelColor = Color(sourceImage.getRGB(i, j))
            val negativeColor = Color(255 - pixelColor.red, 255 - pixelColor.green, 255 - pixelColor.blue)
            outputImage.setRGB(i, j, negativeColor.rgb)
        }
    }
    imIO.write(outputImage, "PNG", outFile)
    return false
}

private fun drawRectangle() {
    println("Enter rectangle width:")
    val width: Int = readLine()!!.toInt()
    println("Enter rectangle height:")
    val height: Int = readLine()!!.toInt()
    val image = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
    val square = image.createGraphics()
    square.background = Color.BLACK

    square.color = Color.RED
    square.drawLine(0, 0, width - 1, height - 1)
    square.drawLine(0, height - 1, width - 1, 0)
    println("Enter output image name:")
    val fileName = readLine()!!
    imIO.write(image, "PNG", File(fileName))
}
