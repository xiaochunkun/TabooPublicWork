package ink.work.taboopublicwork.module.protect

import taboolib.library.configuration.ConfigurationSection
import taboolib.library.xseries.XMaterial

data class ProtectData(
    var name: String = "NONE",
    var enabled: Boolean = false,
    var op: Boolean = false,
    var typeALL: Boolean = false,
    var white: List<String> = listOf(),
    var blacklist: List<String> = listOf(),
) {
    constructor(key: String, name: String, section: ConfigurationSection) : this() {
        this.name = name
        enabled = section.getBoolean("$key.$name.enable", false)
        op = section.getBoolean("$key.$name.op", false)
        typeALL = section.getBoolean("$key.$name.typeAll", false)
        white = section.getStringList("$key.$name.white")
        blacklist = section.getStringList("$key.$name.black")
    }

    fun getBlackList(): List<XMaterial> {
        return blacklist.mapNotNull { XMaterial.matchXMaterial(it).orElse(null) }
    }

    fun getWhiteList(): List<XMaterial> {
        return white.mapNotNull { XMaterial.matchXMaterial(it).orElse(null) }
    }
}
