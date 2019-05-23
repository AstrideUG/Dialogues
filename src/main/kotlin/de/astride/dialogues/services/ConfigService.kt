package de.astride.dialogues.services

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import de.astride.dialogues.data.*
import net.darkdevelopers.darkbedrock.darkness.general.configs.ConfigData
import net.darkdevelopers.darkbedrock.darkness.general.configs.gson.GsonService
import net.darkdevelopers.darkbedrock.darkness.general.configs.gson.GsonService.loadAs
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.toJsonArray
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.toJsonObject
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.toMap
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.toNotNull
import org.bukkit.Bukkit
import java.io.File
import java.util.*
import net.darkdevelopers.darkbedrock.darkness.spigot.functions.JsonObject as jsonObjectOf

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 23.05.2019 04:51.
 * Last edit 23.05.2019
 */
class ConfigService(private val directory: File) {

    private val configData: ConfigData = "config".toConfigData()
    private val config: JsonObject = loadAs(configData) ?: JsonObject()

    private val optionsData: ConfigData = "options".toConfigData(directory.resolve("data"))
    private val options: JsonArray = loadAs(optionsData) ?: JsonArray()

    private val dialoguesData: ConfigData = "options".toConfigData(directory.resolve("dialogues"))
    private val dialogues: JsonObject = loadAs(dialoguesData) ?: JsonObject()

    private val statesData: ConfigData = "options".toConfigData(directory.resolve("states"))
    private val states: JsonObject = loadAs(statesData) ?: JsonObject()

    val sleepOnTextPrint: Long = config["sleep-on-text-print-in-ms"]?.toString()?.toLongOrNull() ?: 1000L

    init {
        if (config["sleep-on-text-print-in-ms"] == null) GsonService.save(
            configData, jsonObjectOf(mapOf("sleep-on-text-print-in-ms" to JsonPrimitive(sleepOnTextPrint)))
        )
    }


    fun loadOptions(): List<Option> = options.toClearedJsonObjectList().map { it.toMap().toOption() }

    fun saveOptions(options: List<Option>, configData: ConfigData = optionsData): Unit =
        GsonService.save(configData, options.toJsonArray())

    @Suppress("UNCHECKED_CAST")
    fun loadDialogues(): Map<Int, List<Dialogue>> = dialogues.toMap().mapNotNull { (key, value) ->
        (key.toIntOrNull() to (value as? List<Map<String, Any?>>)?.mapNotNull { it.toDialogue() }).toNotNull()
    }.toMap()

    fun saveDialogues(
        dialogues: Map<Int, List<Dialogue>>,
        configData: ConfigData = optionsData
    ): Unit = GsonService.save(configData, dialogues.map { (key, value) ->
        key.toString() to value.map { it.toMap() }
    }.toMap().toJsonObject())

    @Suppress("UNCHECKED_CAST")
    fun loadStates(): Map<UUID, Map<Int, String>> = states.toMap().mapNotNull { (key, value) ->
        (UUID.fromString(key) to (value as? Map<String, Any?>)?.mapNotNull { (key, value) ->
            (key.toIntOrNull() to value?.toString()).toNotNull()
        }?.toMap()).toNotNull()
    }.toMap()

    fun saveStates(
        states: Map<UUID, Map<Int, String>>,
        configData: ConfigData = optionsData
    ): Unit = GsonService.save(
        configData, states.map { (key, value) ->
            key.toString() to value.map { it.toString() to value }.toMap()
        }.toMap().toJsonObject()
    )

    @Suppress("UNCHECKED_CAST")
    fun JsonArray.toClearedJsonObjectList(): List<JsonObject> = filter { it is JsonObject } as List<JsonObject>

    private fun String.toConfigData(
        directory: File = this@ConfigService.directory,
        prefix: String = "",
        suffix: String = ".json"
    ): ConfigData = ConfigData(directory, "$prefix$this$suffix")

    companion object {
        val instance: ConfigService get() = Bukkit.getServicesManager()?.getRegistration(ConfigService::class.java)?.provider!!
    }

}