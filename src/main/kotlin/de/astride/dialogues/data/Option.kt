package de.astride.dialogues.data

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 08.05.2019 00:43.
 * Current Version: 1.0 (08.05.2019 - 08.05.2019)
 */
interface Option {
    val name: String
    val text: List<String>
    val actions: Map<String, Option> //Name, NextOption; ends dialogue if actions is empty
    val command: String
}