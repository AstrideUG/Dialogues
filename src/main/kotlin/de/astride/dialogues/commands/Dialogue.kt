/*
 * © Copyright - Lars Artmann aka. LartyHD 2019.
 */

package de.astride.dialogues.commands

import de.astride.dialogues.data.isDialogueRunning
import de.astride.dialogues.data.options
import de.astride.dialogues.data.perform
import kotlinx.coroutines.InternalCoroutinesApi
import net.darkdevelopers.darkbedrock.darkness.spigot.commands.Command
import net.darkdevelopers.darkbedrock.darkness.spigot.utils.isPlayer
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 23.05.2019 05:47.
 * Last edit 23.05.2019
 */
class Dialogue(javaPlugin: JavaPlugin) : Command(
    javaPlugin,
    "Dialogue",
    "dialogue.use",
    usage = "use option <entity-id> <id>",
    minLength = 4,
    maxLength = 4
) {

    @InternalCoroutinesApi
    override fun perform(sender: CommandSender, args: Array<String>) = sender.isPlayer { player ->
        if (player.isDialogueRunning) return@isPlayer
        if (args[0] notSame "use") return@isPlayer
        if (args[1] notSame "option") return@isPlayer

        val entityID = args[2].toIntOrNull() ?: return@isPlayer
        val option = options.find { it.id same args[3] } ?: return@isPlayer
        option.perform(player, entityID)
    }

    //TODO add to Darkness
    private infix fun String.notSame(other: String): Boolean = !this.equals(other, ignoreCase = true)

    //TODO add to Darkness
    private infix fun String.same(other: String): Boolean = this.equals(other, ignoreCase = true)

}