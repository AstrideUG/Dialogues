package de.astride.dialogues.data

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 08.05.2019 00:34.
 * Current Version: 1.0 (08.05.2019 - 23.05.2019)
 */
interface Dialogue {
    val id: String
    val start: Option
//    val active: () -> Boolean
}

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 23.05.2019 19:38.
 * Current Version: 1.0 (23.05.2019 - 23.05.2019)
 */
class DataDialogue(
    override val id: String,
    override val start: Option
) : Dialogue

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 23.05.2019 19:38.
 * Current Version: 1.0 (23.05.2019 - 23.05.2019)
 */
fun Map<String, Any?>.toDialogue(): Dialogue? {
    val id = this["id"].toString()
    val start = this["start"].toString()
    val option = options.find { it.id == start } ?: return null
    return DataDialogue(id, option)
}

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 23.05.2019 19:44.
 * Current Version: 1.0 (23.05.2019 - 23.05.2019)
 */
fun Dialogue.toMap(): Map<String, Any?> = mapOf("id" to id, "start" to start.id)