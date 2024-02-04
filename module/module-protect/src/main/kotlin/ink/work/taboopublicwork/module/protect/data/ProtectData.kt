package ink.work.taboopublicwork.module.protect.data

import taboolib.library.configuration.ConfigurationSection
import taboolib.library.xseries.XMaterial

data class ProtectData(
    var name: String = "NONE",
    var enabled: Boolean = false,
    var op: Boolean = false,
    var typeALL: Boolean = false,
    var type: List<String> = listOf()
) {
    constructor(key: String, name: String, section: ConfigurationSection) : this() {
        this.name = name
        enabled = section.getBoolean("$key.$name.enable", false)
        op = section.getBoolean("$key.$name.op", false)
        typeALL = section.getBoolean("$key.$name.typeAll", false)
        type = section.getStringList("$key.$name.list")
    }

    fun getTypeList(): List<XMaterial> {
        return type.mapNotNull { XMaterial.matchXMaterial(it).orElse(null) }
    }
}
