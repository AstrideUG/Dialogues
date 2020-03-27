/*
 * © Copyright - Lars Artmann aka. LartyHD 2019.
 */

package de.astride.dialogues.commands

import de.astride.dialogues.data.isDialogueRunning
import de.astride.dialogues.data.options
import de.astride.dialogues.data.performAsync
import de.astride.dialogues.functions.configService
import de.astride.dialogues.services.ConfigService
import de.astride.dialogues.services.loadConfigs
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
 * Created on 23.05.2019 05:47.
 */
class Dialogue(javaPlugin: JavaPlugin) : Command(
    javaPlugin,
    "Dialogue",
    usage = "reload:dialogue.reload|use option <entity-id> <id...>:dialogue.use",
    minLength = 1,
    maxLength = Int.MAX_VALUE
) {

    @InternalCoroutinesApi
    override fun perform(sender: CommandSender, args: Array<String>) {
        if (args[0] same "reload") hasPermission(sender, "dialogue.reload") {
            configService = ConfigService(javaPlugin.dataFolder)
            javaPlugin.loadConfigs()
        } else hasPermission(sender, "dialogue.use") {
            if (args.size <= 3) {
                sendUseMessage(sender)
                return@hasPermission
            }
            sender.isPlayer { player ->
                if (player.isDialogueRunning) return@isPlayer
                if (args[0] notSame "use") return@isPlayer
                if (args[1] notSame "option") return@isPlayer

                val entityUUID = args[2].toUUIDOrNull() ?: return@isPlayer
                val option = options.find { it.id same args.drop(3).joinToString(" ") } ?: return@isPlayer
                if (option !in player.options) return@isPlayer

                option.performAsync(player, entityUUID)
            }
        }
    }

}
