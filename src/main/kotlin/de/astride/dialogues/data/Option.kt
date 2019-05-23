package de.astride.dialogues.data

import de.astride.dialogues.functions.configService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.execute
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.sendTo
import net.darkdevelopers.darkbedrock.darkness.universal.builder.textcomponent.builder
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.HoverEvent
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Bukkit
import org.bukkit.entity.Player

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
 * Created by Lars Artmann | LartyHD on 23.05.2019 05:59.
 * Current Version: 1.0 (23.05.2019 - 23.05.2019)
 */
@InternalCoroutinesApi
fun Option.perform(player: Player, entityID: Int) {
    GlobalScope.launch {
        text.forEach {
            it.sendTo(player)
            delay(configService.sleepOnTextPrint)
        }
        if (actions.isEmpty()) {
            Bukkit.getConsoleSender().execute(command.replace("\${player}", player.name))
            states.getOrPut(player.uniqueId) { mutableMapOf() }[entityID] += id
        } else {
            val textComponent = TextComponent()
            actions.map { (key, value) ->
                textComponent.builder()
                    .setText(key)
                    .setClickEvent(
                        ClickEvent(ClickEvent.Action.RUN_COMMAND, "/dialogue use option $entityID $value")
                    )
                    .setHoverEvent(HoverEvent(HoverEvent.Action.SHOW_TEXT, arrayOf(TextComponent("Select a option!"))))
                    .build()
            }.forEach { textComponent.addExtra(it) }
            player.spigot().sendMessage(textComponent)
        }
    }.invokeOnCompletion(onCancelling = true) {
        player.isDialogueRunning = false
    }
}