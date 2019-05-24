package de.astride.dialogues.data

import net.darkdevelopers.darkbedrock.darkness.general.delegate.MapperCheckByCollection
import net.darkdevelopers.darkbedrock.darkness.general.delegate.MapperMapCheckByCollection
import org.bukkit.entity.Player
import java.util.*

typealias Dialogues = Map<UUID, List<Dialogue>> //(Entity)UUID, all dialogues
typealias MutableDialogues = MutableMap<UUID, MutableList<Dialogue>>
typealias States = Map<UUID, Map<UUID, String>> //(Player)UUID, <(Entity)UUID, DialogueID>
typealias MutableStates = MutableMap<UUID, MutableMap<UUID, String>>
typealias Options = Set<Option>
typealias MutableOptions = MutableSet<Option>

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 08.05.2019 00:49.
 * Current Version: 1.0 (08.05.2019 - 22.05.2019)
 */
var dialogues: MutableDialogues = mutableMapOf()

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 22.05.2019 17:13.
 * Current Version: 1.0 (22.05.2019 - 23.05.2019)
 */
var states: MutableStates = mutableMapOf()

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 23.05.2019 05:45.
 * Current Version: 1.0 (23.05.2019 - 23.05.2019)
 */
var options: MutableOptions = mutableSetOf()

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 22.05.2019 17:51.
 *
 * @returns `true` if uniqueId is in collection
 *
 * Current Version: 1.0 (22.05.2019 - 24.05.2019)
 */
var Player.isDialogueRunning: Boolean by MapperCheckByCollection(mutableSetOf<UUID>()) { uniqueId }

/**
 * @author Lars Artmann | LartyHD
 * Created by Lars Artmann | LartyHD on 24.05.2019 05:24.
 * Current Version: 1.0 (24.05.2019 - 24.05.2019)
 */
var Player.options: MutableOptions by MapperMapCheckByCollection<Player, UUID, MutableOptions>(
    mapped = { uniqueId },
    output = { mutableSetOf() }
)
