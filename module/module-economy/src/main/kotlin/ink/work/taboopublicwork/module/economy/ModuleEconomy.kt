package ink.work.taboopublicwork.module.economy

import ink.work.taboopublicwork.api.IModule
import org.bukkit.event.player.PlayerJoinEvent
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.event.SubscribeEvent
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

    // VaultHandler接口载体
    lateinit var vaultManager: VaultManager

    var moneyFormat = "###,##0.00$"

    @Awake(LifeCycle.ENABLE)
    fun init() {
        initModule {
            info("Module - Economy 已启用")
            EconomyPlayerData.module = this
            moneyFormat = config.getString("money-format") ?: "###,##0.00$"
            vaultManager = VaultManager()
            vaultManager.hook()
        }
        reloadModule {
            config.reload()
            moneyFormat =  config.getString("money-format") ?: "###,##0.00$"
        }
    }

    @Awake(LifeCycle.DISABLE)
    fun disable() {
        if (this::vaultManager.isInitialized) {
            vaultManager.unHook()
        }
    }
}