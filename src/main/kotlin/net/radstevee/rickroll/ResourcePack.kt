package net.radstevee.rickroll

import java.io.File
import javax.imageio.ImageIO
import net.radstevee.packed.ResourcePackBuilder.Companion.resourcePack
import net.radstevee.packed.font.Font.Companion.font
import net.radstevee.packed.key.Key
import net.radstevee.packed.pack.PackFormat
import org.apache.commons.lang.StringEscapeUtils

object ResourcePack {
    val characters = mutableMapOf<Int, MutableList<String>>()
    val pack = resourcePack {
        meta {
            description = "Rick roll !!"
            format = PackFormat.LATEST
            outputDir = File("/home/radsteve/Minecraft/MCCI 1/.minecraft/resourcepacks/rickroll")
        }
    }
    val frames = (1..999).toList()

    private fun splitImage(frame: Int): List<Key> {
        val inputStream =
                RickRoll::class
                        .java
                        .getResource(
                                String.format("/assets/rickroll/textures/frame%04d.png", frame)
                        )
                        ?.openStream()
                        ?: error("Frame $frame not found!")
        val outputDir = File(pack.outputDir, "assets/rickroll/textures/frame$frame")
        val image = ImageIO.read(inputStream)
        val tileWidth = 128
        val tileHeight = 128
        val rows = (image.height + tileHeight - 1) / tileHeight
        val columns = (image.width + tileWidth - 1) / tileWidth

        outputDir.mkdirs()
        var partNumber = 0
        val images = mutableListOf<Key>()

        for (y in 0 until rows) {
            for (x in 0 until columns) {
                val startX = x * tileWidth
                val startY = y * tileHeight
                val width =
                        if (startX + tileWidth > image.width) image.width - startX else tileWidth
                val height =
                        if (startY + tileHeight > image.height) image.height - startY
                        else tileHeight
                val subImage = image.getSubimage(startX, startY, width, height)
                val outputFile = File(outputDir, "tile_$partNumber.png")
                outputFile.mkdirs()
                ImageIO.write(subImage, "png", outputFile)
                images.add(Key("rickroll", "frame$frame/tile_$partNumber.png"))

                if (characters[frame] == null) characters[frame] = mutableListOf()
                characters[frame]!!.add(
                        StringEscapeUtils.unescapeJava(String.format("\\uE%03d", partNumber))
                )

                partNumber++
            }
        }

        return images
    }

    init {
        println("Generating fonts...")
        frames.forEach { f ->
            println("Processing frame $f")
            pack.fonts.add(
                    font {
                        key = Key("rickroll", "frame$f")

                        val images = splitImage(f)
                        images.forEachIndexed { i, img ->
                            bitmap {
                                key = img
                                height = 8
                                ascent = 0
                                chars = listOf(characters[f]!![i])
                            }
                        }
                    }
            )
        }
        pack.save()
    }
}
