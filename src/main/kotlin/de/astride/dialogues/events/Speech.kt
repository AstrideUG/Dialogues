package de.astride.dialogues.events

import de.astride.dialogues.data.*
import kotlinx.coroutines.InternalCoroutinesApi
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.events.listen
import net.darkdevelopers.darkbedrock.darkness.spigot.manager.game.EventsTemplate
import org.bukkit.entity.Entity
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.plugin.Plugin
import java.util.*

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 19.05.2019 18:41.
 * Current Version: 1.0 (19.05.2019 - 23.05.2019)
 */
object Speech : EventsTemplate() {

    @InternalCoroutinesApi
    fun setup(plugin: Plugin) {
        listen<PlayerInteractEntityEvent>(plugin) { event ->

            val player = event.player ?: return@listen
            if (player.isDialogueRunning) return@listen

            val entity = event.rightClicked ?: return@listen
            val dialogue = entity.findDialogue(player.uniqueId) ?: return@listen
            val option = dialogue.start

            player.isDialogueRunning = true
            player.options = mutableSetOf(option)
            option.performAsync(player, entity.uniqueId)

        }.add()
    }

    private fun Entity.findDialogue(playerUUID: UUID): Dialogue? {

        val dialogues = dialogues[uniqueId] ?: return null
        if (dialogues.isEmpty()) return null //entity has no dialogues

        return try {
            val playerDialogues = states.getOrPut(playerUUID) { mutableMapOf() }
            val lastDialogueID = playerDialogues[uniqueId] ?: return dialogues.first()
            val lastDialogue = dialogues.find { it.id == lastDialogueID }
            if (lastDialogue == null) dialogues.first() else dialogues[dialogues.indexOf(lastDialogue) + 1]
        } catch (ex: IndexOutOfBoundsException) {
            System.err.println("[Dialogues] tried to use a not existing dialogue on entity with id: $entityId by player $playerUUID")
            dialogues.first()
        }

    }

}