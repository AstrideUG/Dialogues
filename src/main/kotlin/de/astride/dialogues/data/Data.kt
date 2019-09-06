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

var dialogues: MutableDialogues = mutableMapOf()
var states: MutableStates = mutableMapOf()
var options: MutableOptions = mutableSetOf()

/**
 * @returns `true` if uniqueId is in collection
 */
var Player.isDialogueRunning: Boolean by MapperCheckByCollection(mutableSetOf<UUID>()) { uniqueId }

var Player.options: MutableOptions by MapperMapCheckByCollection<Player, UUID, MutableOptions>(
    mapped = { uniqueId },
    output = { mutableSetOf() }
)
