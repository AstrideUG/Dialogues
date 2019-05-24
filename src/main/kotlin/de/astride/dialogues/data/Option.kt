package de.astride.dialogues.data

import de.astride.dialogues.functions.configService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.clip.placeholderapi.PlaceholderAPI
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.execute
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.sendTo
import net.darkdevelopers.darkbedrock.darkness.universal.builder.textcomponent.builder
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Bukkit
import org.bukkit.ChatColor.DARK_GRAY
import org.bukkit.ChatColor.GREEN
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 08.05.2019 00:43.
 * Current Version: 1.0 (08.05.2019 - 23.05.2019)
 */
interface Option {
    val id: String
    val text: List<String> //with Scheduler
    val actions: Map<String, String> //Name, NextOptionID; ends dialogue if actions is empty
    val command: String //if actions is empty run after text print
}

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 23.05.2019 18:45.
 * Current Version: 1.0 (23.05.2019 - 23.05.2019)
 */
data class DataOption(
    override val id: String,
    override val text: List<String> = emptyList(),
    override val actions: Map<String, String> = emptyMap(),
    override val command: String = ""
) : Option

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 23.05.2019 19:25.
 * Current Version: 1.0 (23.05.2019 - 23.05.2019)
 */
@Suppress("UNCHECKED_CAST")
fun Map<String, Any?>.toOption(): Option {
    val id = this["id"].toString()
    val text = this["text"] as? List<String> ?: listOf()
    val actions = this["actions"] as? Map<String, String> ?: mapOf()
    val command = this["command"] as? String ?: ""
    return DataOption(id, text, actions, command)
}

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 23.05.2019 22:07.
 * Current Version: 1.0 (23.05.2019 - 23.05.2019)
 */
fun Option.toMap(): Map<String, Any?> = mapOf(
    "id" to id,
    "text" to text,
    "actions" to actions,
    "command" to command
)

@InternalCoroutinesApi
fun Option.performAsync(player: Player, entityUUID: UUID) {
    GlobalScope.launch { perform(player, entityUUID) }.invokeOnCompletion(onCancelling = true) {
        if (it == null) if (player.options.isEmpty()) return@invokeOnCompletion
        player.isDialogueRunning = false
    }
}

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 23.05.2019 05:59.
 * Current Version: 1.0 (23.05.2019 - 24.05.2019)
 */
@InternalCoroutinesApi
suspend fun Option.perform(player: Player, entityUUID: UUID) {
    player.options = actions.values.mapNotNull { id -> options.find { it.id == id } }.toMutableSet()
    this.printText(player)

    if (player.options.isEmpty()) {
        Bukkit.getScheduler().runTask(JavaPlugin.getProvidingPlugin(Dialogue::class.java)) {
            Bukkit.getConsoleSender().execute(command.replaced(player))
        }
        states.getOrPut(player.uniqueId) { mutableMapOf() }[entityUUID] = id
    } else {
        val textComponents = mutableListOf<TextComponent>()
        actions.forEach { (key, value) ->
            textComponents += TextComponent().builder()
                .setText("$DARK_GRAY[$GREEN$key$DARK_GRAY] ")
                .setClickEvent(
                    ClickEvent(ClickEvent.Action.RUN_COMMAND, "/dialogue use option $entityUUID $value")
                )
                .setHoverEvent(HoverEvent(HoverEvent.Action.SHOW_TEXT, arrayOf(TextComponent("Select a option!"))))
                .build()
        }
        player.spigot().sendMessage(*textComponents.toTypedArray())
    }
}

private suspend fun Option.printText(sender: CommandSender): Unit = text.forEachIndexed { index, line ->
    line.sendTo(sender)
    if (index == text.size - 1 && actions.isEmpty()) return
    delay(configService.sleepOnTextPrint)
}

private fun String.replaced(player: Player): String =
    if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) this
    else PlaceholderAPI.setPlaceholders(player, this)
