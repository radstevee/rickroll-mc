package net.radstevee.rickroll

import io.papermc.paper.command.brigadier.Commands
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents
import net.radstevee.radlib.text.TextUtil.buildText
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class RickRoll : JavaPlugin() {
    override fun onEnable() {
        ResourcePack

        lifecycleManager.registerEventHandler(LifecycleEvents.COMMANDS) { event ->
            val commands = event.registrar()
            commands.register(
                    Commands.literal("rickroll")
                            .executes { ctx ->
                                var currentFrame = 1
                                println(ResourcePack.characters)

                                Bukkit.getScheduler()
                                        .runTaskTimer(
                                                this,
                                                Runnable {
                                                    var str = ""
                                                    val component = buildText {
                                                        repeat(16) { appendNewline() }

                                                        println(
                                                                "Frame: $currentFrame; chars: ${ResourcePack.characters[currentFrame]?.size}"
                                                        )
                                                        font(
                                                                ResourcePack.pack.fonts[
                                                                                currentFrame]
                                                                        .key
                                                                        .toString()
                                                        )
                                                        ResourcePack.characters[currentFrame]!!
                                                                .forEachIndexed { i, it ->
                                                                    append(it)
                                                                    str += it
                                                                    if ((i + 1) % 9 == 0) {
                                                                        str += "\n"
                                                                        appendNewline()
                                                                    } /* else {
                                                                          str += " "
                                                                          append(buildText {
                                                                              appendSpace()
                                                                              font("minecraft:default")
                                                                          })
                                                                      } */
                                                                }
                                                    }
                                                    currentFrame++
                                                    ctx.source.sender.sendMessage(component)
                                                    if (currentFrame == 1000)
                                                            Bukkit.getScheduler().cancelTasks(this)
                                                },
                                                1,
                                                0
                                        )
                                0
                            }
                            .build()
            )
        }
    }

    override fun onDisable() {}
}
