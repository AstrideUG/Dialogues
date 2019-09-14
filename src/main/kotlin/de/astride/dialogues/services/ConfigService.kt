package de.astride.dialogues.services

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import de.astride.dialogues.data.*
import de.astride.dialogues.functions.configService
import net.darkdevelopers.darkbedrock.darkness.general.configs.ConfigData
import net.darkdevelopers.darkbedrock.darkness.general.configs.gson.GsonService
import net.darkdevelopers.darkbedrock.darkness.general.configs.gson.GsonService.loadAs
import net.darkdevelopers.darkbedrock.darkness.general.functions.toUUID
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.*
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.JsonObject as jsonObjectOf


/**
 * @author Lars Artmann | LartyHD
 * Created on 23.05.2019 04:51.
 */
class ConfigService(private val directory: File) {

    private val dataDirectory = directory.resolve("data")

    private val configData: ConfigData = "config".toConfigData()
    private val config: JsonObject = loadAs(configData) ?: JsonObject()

    private val optionsData: ConfigData = "options".toConfigData(dataDirectory)
    private val options: Array<File> = (dataDirectory.resolve("options").listFiles() ?: emptyArray()) + optionsData.file

    private val dialoguesData: ConfigData = "dialogues".toConfigData(dataDirectory)
    private val dialogues: JsonObject = loadAs(dialoguesData) ?: JsonObject()

    private val statesData: ConfigData = "states".toConfigData(dataDirectory)
    private val states: JsonObject = loadAs(statesData) ?: JsonObject()


    val sleepOnTextPrint: Long = config["sleep-on-text-print-in-ms"]?.toString()?.toLongOrNull() ?: 1000L

    init {
        if (config["sleep-on-text-print-in-ms"] == null) GsonService.save(
            configData, jsonObjectOf(mapOf("sleep-on-text-print-in-ms" to JsonPrimitive(sleepOnTextPrint)))
        )
    }

    fun loadOptions(): MutableOptions = options.filter {
        it.isFile && it.endsWith(".json")
    }.map { loadAs(it) ?: JsonArray() }.toMap().map { it.toOption() }.toMutableSet()

    fun saveOptions(options: Options, configData: ConfigData = optionsData): Unit =
        GsonService.save(configData, JsonArray(options.map { it.toMap().toJsonObject() }))

    @Suppress("UNCHECKED_CAST")
    fun loadDialogues(jsonObject: JsonObject = dialogues): Dialogues = jsonObject.toMap().mapNotNull { (key, value) ->
        (key.toUUID() to (value as? List<Map<String, Any?>>)?.mapNotNull { it.toDialogue() }).toSecondNotNull()
    }.toMap()

    fun saveDialogues(
        dialogues: Dialogues,
        configData: ConfigData = dialoguesData
    ): Unit = GsonService.save(configData, dialogues.map { (key, value) ->
        key.toString() to value.map { it.toMap() }
    }.toMap().toJsonObject())

    @Suppress("UNCHECKED_CAST")
    fun loadStates(jsonObject: JsonObject = states): States = jsonObject.toMap().mapNotNull { (key, value) ->
        (key.toUUID() to (value as? Map<String, Any?>)?.mapNotNull { (key, value) ->
            (key.toUUID() to value?.toString()).toSecondNotNull()
        }?.toMap()).toNotNull()
    }.toMap()

    fun saveStates(
        states: States,
        configData: ConfigData = statesData
    ): Unit = GsonService.save(
        configData, states.map { (key, value) ->
            val pair = key.toString() to value.map { it.key.toString() to it.value }.toMap()
            pair
        }.toMap().apply {

        }.toJsonObject()
    )

    private fun String.toConfigData(
        directory: File = this@ConfigService.directory,
        prefix: String = "",
        suffix: String = ".json"
    ): ConfigData = ConfigData(directory, "$prefix$this$suffix")

}

fun JavaPlugin.loadConfigs() {
    options = configService.loadOptions()
    logger.info("Loaded options: $options")
    dialogues = configService.loadDialogues().map { it.key to it.value.toMutableList() }.toMap().toMutableMap()
    logger.info("Loaded dialogues: $dialogues")
    states = configService.loadStates().map { it.key to it.value.toMutableMap() }.toMap().toMutableMap()
    logger.info("Loaded states: $states")
}

fun JavaPlugin.saveConfigs() {
    configService.saveOptions(options)
    logger.info("Saves options: $options")
    configService.saveDialogues(dialogues)
    logger.info("Saves dialogues: $dialogues")
    configService.saveStates(states)
    logger.info("Saves states: $states")
}