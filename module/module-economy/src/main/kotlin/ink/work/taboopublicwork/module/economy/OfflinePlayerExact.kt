package ink.work.taboopublicwork.module.economy

import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer

fun String.getOfflinePlayerExact(): OfflinePlayer {
    return Bukkit.getOfflinePlayer(this)
}