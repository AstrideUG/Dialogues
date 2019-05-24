/*
 * © Copyright - Lars Artmann aka. LartyHD 2019.
 */

package de.astride.dialogues.commands

import de.astride.dialogues.data.isDialogueRunning
import de.astride.dialogues.data.options
import de.astride.dialogues.data.performAsync
import kotlinx.coroutines.InternalCoroutinesApi
import net.darkdevelopers.darkbedrock.darkness.general.functions.notSame
import net.darkdevelopers.darkbedrock.darkness.general.functions.same
import net.darkdevelopers.darkbedrock.darkness.general.functions.toUUIDOrNull
import net.darkdevelopers.darkbedrock.darkness.spigot.commands.Command
import net.darkdevelopers.darkbedrock.darkness.spigot.utils.isPlayer
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 23.05.2019 05:47.
 * Last edit 24.05.2019
 */
class Dialogue(javaPlugin: JavaPlugin) : Command(
    javaPlugin,
    "Dialogue",
    "dialogue.use",
    usage = "use option <entity-id> <id...>",
    minLength = 4,
    maxLength = Int.MAX_VALUE
) {

    @InternalCoroutinesApi
    override fun perform(sender: CommandSender, args: Array<String>): Unit = sender.isPlayer { player ->
        if (player.isDialogueRunning) return@isPlayer
        if (args[0] notSame "use") return@isPlayer
        if (args[1] notSame "option") return@isPlayer

        val entityUUID = args[2].toUUIDOrNull() ?: return@isPlayer
        val option = options.find { it.id same args.drop(3).joinToString(" ") } ?: return@isPlayer
        if (option !in player.options) return@isPlayer

        option.performAsync(player, entityUUID)
    }

}