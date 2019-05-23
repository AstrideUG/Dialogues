package de.astride.dialogues

import de.astride.dialogues.commands.Dialogue
import de.astride.dialogues.data.dialogues
import de.astride.dialogues.data.options
import de.astride.dialogues.data.states
import de.astride.dialogues.events.Speech
import de.astride.dialogues.functions.configService
import de.astride.dialogues.services.ConfigService
import kotlinx.coroutines.InternalCoroutinesApi
import net.darkdevelopers.darkbedrock.darkness.spigot.plugin.DarkPlugin
import org.bukkit.plugin.ServicePriority

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 22.05.2019 17:23.
 * Current Version: 1.0 (22.05.2019 - 22.05.2019)
 */
class Dialogues : DarkPlugin() {

    @InternalCoroutinesApi
    override fun onEnable(): Unit = onEnable {
        server.servicesManager.register(
            ConfigService::class.java,
            ConfigService(dataFolder),
            this,
            ServicePriority.Normal
        ) //Important for ConfigService.instance

        options = configService.loadOptions().toSet().toMutableSet()
        dialogues = configService.loadDialogues().map { it.key to it.value.toMutableList() }.toMap().toMutableMap()
        states = configService.loadStates().map { it.key to it.value.toMutableMap() }.toMap().toMutableMap()


        Dialogue(this)
        Speech.setup(this)
    }

    override fun onDisable(): Unit = onDisable {
        Speech.reset()
    }

}