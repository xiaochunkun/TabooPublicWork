package ink.work.taboopublicwork.module.protect

import ink.work.taboopublicwork.api.IModule
import ink.work.taboopublicwork.module.protect.data.ProtectData
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.function.info
import taboolib.common.platform.function.submit
import taboolib.module.configuration.Configuration
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList

object ModuleProtect : IModule {

    // 标准 IModule 接口实现
    override val name: String = "保护"
    override val id: String = "protect"
    override val author: String = "小坤"
    override val description = "世界保护模块"

    override lateinit var config: Configuration
    override lateinit var langFile: Configuration

    // 总保护数据载体
    var allProtectDataList: CopyOnWriteArrayList<ProtectData> = CopyOnWriteArrayList()

    // 世界保护数据载体
    var worldProtectDataMap: ConcurrentHashMap<String, List<ProtectData>> = ConcurrentHashMap()

    @Awake(LifeCycle.ENABLE)
    fun init() {
        initModule {
            info("Module - Protect 已启用")
            loadValues()
        }
        reloadModule {
            loadValues()
        }
    }

    private fun loadValues() {
        config.reload()
        allProtectDataList = CopyOnWriteArrayList()
        worldProtectDataMap = ConcurrentHashMap()
        config.getConfigurationSection("all")?.getKeys(false)?.forEach {
            val protectData = ProtectData("all", it, config)
            allProtectDataList.add(protectData)
        }
        config.getConfigurationSection("world")?.getKeys(false)?.forEach {
            val section = config.getConfigurationSection("world.$it") ?: return@forEach
            val list = section.getKeys(false).map { key ->
                ProtectData("world.$it", key, config)
            }
            worldProtectDataMap[it] = list
        }
    }

    fun getProtectData(world: String?): List<ProtectData> {
        val list = mutableListOf<ProtectData>()
        list.addAll(allProtectDataList)
        world?.let {
            list.addAll(worldProtectDataMap[world] ?: emptyList())
        } ?: run {
            val protectDataList = mutableListOf<ProtectData>()
            worldProtectDataMap.forEach { (_, u) -> protectDataList.addAll(u) }
            list.addAll(protectDataList)
        }
        return list
    }
}