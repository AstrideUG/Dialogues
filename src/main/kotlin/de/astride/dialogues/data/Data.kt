package de.astride.dialogues.data

import org.bukkit.entity.Player
import java.util.*

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 08.05.2019 00:49.
 * Current Version: 1.0 (08.05.2019 - 22.05.2019)
 */
val dialogues: MutableMap<Int, MutableList<Dialogue>> = mutableMapOf() //EntityID, all dialogues

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 22.05.2019 17:13.
 * Current Version: 1.0 (22.05.2019 - 23.05.2019)
 */
val states: MutableMap<UUID, MutableMap<Int, String>> = mutableMapOf() //(Player)UUID, <EntityID, DialogueID>

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 23.05.2019 05:45.
 * Current Version: 1.0 (23.05.2019 - 23.05.2019)
 */
val options: MutableSet<Option> = mutableSetOf()

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 22.05.2019 17:50.
 *
 * This is a runtime variable.
 *
 * Current Version: 1.0 (22.05.2019 - 22.05.2019)
 */
val runsDialogue: MutableSet<UUID> = mutableSetOf()

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 22.05.2019 17:51.
 *
 * @returns `true` if uniqueId is in runsDialogue
 *
 * Current Version: 1.0 (22.05.2019 - 22.05.2019)
 */
var Player.isDialogueRunning: Boolean
    get() = uniqueId in runsDialogue
    set(value) = if (value) runsDialogue += uniqueId else runsDialogue -= uniqueId
