package ink.work.taboopublicwork.module.economy

import ink.work.taboopublicwork.api.IModule
import ink.work.taboopublicwork.module.economy.database.EconomyDatabase
import ink.work.taboopublicwork.module.economy.database.EconomyYaml
import ink.work.taboopublicwork.module.economy.database.IEconomyDatabase
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.function.info
import taboolib.module.configuration.Configuration

object ModuleEconomy : IModule {
    // 标准 IModule 接口实现
    override val name: String = "经济"
    override val id: String = "economy"
    override val author: String = "小坤"
    override val description = "经济模块"
    override lateinit var config: Configuration
    override lateinit var langFile: Configuration

    // Database接口载体
    lateinit var database: IEconomyDatabase

    // VaultHandler接口载体
    private lateinit var vaultManager: VaultManager

    var moneyFormat = "###,##0.00$"

    @Awake(LifeCycle.ENABLE)
    fun init() {
        initModule {
            info("Module - Economy 已启用")
            moneyFormat = config.getString("money-format") ?: "###,##0.00$"
            vaultManager = VaultManager()
            val databaseType = config.getString("database.type", "yaml")!!
            database = when (databaseType) {
                "yaml","YAML" -> EconomyYaml()
                "sql","SQL" -> EconomyDatabase()
                else -> EconomyYaml()
            }
            database.load()
            vaultManager.hook()
            info("Module - Economy 数据库类型: $databaseType")
        }
        reloadModule {
            config.reload()
            moneyFormat =  config.getString("money-format") ?: "###,##0.00$"
            database.load()
        }
    }

    @Awake(LifeCycle.DISABLE)
    fun disable() {
        if (this::vaultManager.isInitialized) {
            vaultManager.unHook()
        }
    }


}