/*
 * © Copyright - Lars Artmann aka. LartyHD 2019.
 */

package de.astride.dialogues.functions

import de.astride.dialogues.Dialogues
import de.astride.dialogues.services.ConfigService
import org.bukkit.Bukkit
import org.bukkit.plugin.ServicePriority
import org.bukkit.plugin.java.JavaPlugin

/*
 * @author Lars Artmann | LartyHD
 * Created on 23.05.2019 18:08.
 */

val javaPlugin: Dialogues = JavaPlugin.getPlugin(Dialogues::class.java)

var configService
    get() = Bukkit.getServicesManager()?.getRegistration(ConfigService::class.java)?.provider!!
    set(value) {
        Bukkit.getServicesManager()?.register(
            ConfigService::class.java,
            value,
            javaPlugin,
            ServicePriority.Normal
        )
    }
