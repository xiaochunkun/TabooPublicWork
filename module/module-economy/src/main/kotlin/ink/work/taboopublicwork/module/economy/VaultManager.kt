package ink.work.taboopublicwork.module.economy

import ink.work.taboopublicwork.TabooPublicWork
import net.milkbowl.vault.economy.Economy
import org.bukkit.Bukkit
import org.bukkit.plugin.ServicePriority


class VaultManager {

    lateinit var economy: VaultHandler

    fun hook(){
        if (!this::economy.isInitialized) {
            economy = VaultHandler()
            Bukkit.getServicesManager()
                .register(
                    Economy::class.java,
                    economy,
                    TabooPublicWork.bukkitPlugin,
                    ServicePriority.Highest
                )
        }
    }

    fun unHook(){
        if (this::economy.isInitialized) {
            Bukkit.getServicesManager().unregister(Economy::class.java, economy)
        }
    }
}