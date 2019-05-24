package de.astride.dialogues.services

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import de.astride.dialogues.data.*
import net.darkdevelopers.darkbedrock.darkness.general.configs.ConfigData
import net.darkdevelopers.darkbedrock.darkness.general.configs.gson.GsonService
import net.darkdevelopers.darkbedrock.darkness.general.configs.gson.GsonService.loadAs
import net.darkdevelopers.darkbedrock.darkness.general.functions.toUUID
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.*
import org.bukkit.Bukkit
import java.io.File
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.JsonObject as jsonObjectOf


/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 23.05.2019 04:51.
 * Last edit 24.05.2019
 */
class ConfigService(private val directory: File) {

    private val dataDirectory = directory.resolve("data")

    private val configData: ConfigData = "config".toConfigData()
    private val config: JsonObject = loadAs(configData) ?: JsonObject()

    private val optionsData: ConfigData = "options".toConfigData(dataDirectory)
    private val options: JsonArray = loadAs(optionsData) ?: JsonArray()

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


    fun loadOptions(): MutableOptions = options.toMap().map { it.toOption() }.toMutableSet()

    fun saveOptions(options: Options, configData: ConfigData = optionsData): Unit =
        GsonService.save(configData, JsonArray(options.map { it.toMap().toJsonObject() }))

    @Suppress("UNCHECKED_CAST")
    fun loadDialogues(): Dialogues = dialogues.toMap().mapNotNull { (key, value) ->
        (key.toUUID() to (value as? List<Map<String, Any?>>)?.mapNotNull { it.toDialogue() }).toSecondNotNull()
    }.toMap()

    fun saveDialogues(
        dialogues: Dialogues,
        configData: ConfigData = dialoguesData
    ): Unit = GsonService.save(configData, dialogues.map { (key, value) ->
        key.toString() to value.map { it.toMap() }
    }.toMap().toJsonObject())

    @Suppress("UNCHECKED_CAST")
    fun loadStates(): States = states.toMap().mapNotNull { (key, value) ->
        (key.toUUID() to (value as? Map<String, Any?>)?.mapNotNull { (key, value) ->
            (key.toUUID() to value?.toString()).toSecondNotNull()
        }?.toMap()).toNotNull()
    }.toMap()

    fun saveStates(
        states: States,
        configData: ConfigData = statesData
    ): Unit = GsonService.save(
        configData, states.map { (key, value) ->
            key.toString() to value.map { it.toString() to value }.toMap()
        }.toMap().toJsonObject()
    )

    private fun String.toConfigData(
        directory: File = this@ConfigService.directory,
        prefix: String = "",
        suffix: String = ".json"
    ): ConfigData = ConfigData(directory, "$prefix$this$suffix")

    companion object {
        val instance: ConfigService get() = Bukkit.getServicesManager()?.getRegistration(ConfigService::class.java)?.provider!!
    }

}