package de.astride.dialogues

import de.astride.dialogues.commands.Dialogue
import de.astride.dialogues.events.Speech
import de.astride.dialogues.functions.configService
import de.astride.dialogues.services.ConfigService
import de.astride.dialogues.services.loadConfigs
import de.astride.dialogues.services.saveConfigs
import kotlinx.coroutines.InternalCoroutinesApi
import net.darkdevelopers.darkbedrock.darkness.spigot.plugin.DarkPlugin

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 22.05.2019 17:23.
 * Current Version: 1.0 (22.05.2019 - 22.05.2019)
 */
class Dialogues : DarkPlugin() {

    @InternalCoroutinesApi
    override fun onEnable(): Unit = onEnable {

        configService = ConfigService(dataFolder) //Important for ConfigService.instance
        loadConfigs()

        Dialogue(this)
        Speech.setup(this)

    }

    override fun onDisable(): Unit = onDisable {
        Speech.reset()
        saveConfigs()
    }


}