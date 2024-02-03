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
    constructor(key: String, section: ConfigurationSection) : this() {
        name = key
        enabled = section.getBoolean("$key.enabled", false)
        op = section.getBoolean("$key.op", false)
        typeALL = section.getBoolean("$key.typeALL", false)
        type = section.getStringList("$key.type")
    }

    fun getTypeList(): List<XMaterial> {
        return type.mapNotNull { XMaterial.matchXMaterial(it).orElse(null) }
    }
}
