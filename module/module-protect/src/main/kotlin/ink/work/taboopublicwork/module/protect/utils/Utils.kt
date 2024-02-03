package ink.work.taboopublicwork.module.protect.utils

import org.bukkit.plugin.Plugin

fun List<String>.containsIgnoreCase(str: String): Boolean {
    return this.any { it.equals(str, true) }
}

fun Plugin.getFirstPluginVersion(): Int{
    val version = this.description.version
    return version.split(".")[0].toInt()
}