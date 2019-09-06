package de.astride.dialogues.data

/**
 * @author Lars Artmann | LartyHD
 * Created on 08.05.2019 00:34.
 */
interface Dialogue {
    val id: String
    val start: Option
//    val active: () -> Boolean
}

/**
 * @author Lars Artmann | LartyHD
 * Created on 23.05.2019 19:38.
 */
class DataDialogue(
    override val id: String,
    override val start: Option
) : Dialogue

fun Map<String, Any?>.toDialogue(): Dialogue? {
    val id = this["id"].toString()
    val start = this["start"].toString()
    val option = options.find { it.id == start } ?: return null
    return DataDialogue(id, option)
}

fun Dialogue.toMap(): Map<String, Any?> = mapOf("id" to id, "start" to start.id)