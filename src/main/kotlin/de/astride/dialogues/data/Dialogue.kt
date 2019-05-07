package de.astride.dialogues.data

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 08.05.2019 00:34.
 * Current Version: 1.0 (08.05.2019 - 08.05.2019)
 */
interface Dialogue {
    val id: String
    val entries: Set<Option>
    val endCommand: String
}