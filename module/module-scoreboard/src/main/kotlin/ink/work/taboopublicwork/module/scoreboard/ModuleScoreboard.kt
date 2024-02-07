package ink.work.taboopublicwork.module.scoreboard

import ink.work.taboopublicwork.api.IModule
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerKickEvent
import org.bukkit.event.player.PlayerQuitEvent
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.event.SubscribeEvent
import taboolib.common.platform.function.info
import taboolib.common.platform.function.submit
import taboolib.common.platform.service.PlatformExecutor
import taboolib.common.util.unsafeLazy
import taboolib.library.reflex.Reflex.Companion.invokeMethod
import taboolib.library.reflex.Reflex.Companion.setProperty
import taboolib.library.reflex.Reflex.Companion.unsafeInstance
import taboolib.module.chat.colored
import taboolib.module.configuration.Configuration
import taboolib.module.nms.nmsClass
import taboolib.module.nms.sendPacket
import taboolib.platform.compat.replacePlaceholder
import java.util.*

object ModuleScoreboard : IModule {

    // 标准 IModule 接口实现
    override val name: String = "计分板"
    override val id: String = "scoreboard"
    override val author: String = "小坤"
    override val description = "玩家计分板"

    override lateinit var config: Configuration
    override lateinit var langFile: Configuration


    private val scoreboards = mutableMapOf<UUID, ScoreboardUtils>()

    private lateinit var task: PlatformExecutor.PlatformTask

    private var updateTime = 20L

    private lateinit var scoreboardData: ScoreboardData

    private lateinit var headerTabList: List<String>

    private lateinit var footerTabList: List<String>

    @Awake(LifeCycle.ENABLE)
    fun init() {
        initModule {
            info("Module - Scoreboard 已启用")
            loadValues()
            scoreboards.clear()
            task = submit(period = updateTime) {
                scoreboards.forEach { (_, u) ->
                    u.resetSlot()
                }
            }

        }
        reloadModule {
            task.cancel()
            scoreboards.clear()
            loadValues()
            task = submit(period = updateTime) {
                scoreboards.forEach { (_, u) ->
                    u.resetSlot()
                }
            }
        }
    }

    private fun loadValues() {
        config.reload()
        updateTime = config.getLong("update-time", 20L)
        scoreboardData = ScoreboardData(config, "boards")
        headerTabList = config.getStringList("tab-list.header")
        footerTabList = config.getStringList("tab-list.footer")
    }

    @SubscribeEvent
    fun onJoin(event: PlayerJoinEvent) {
        if (ModuleScoreboard.isEnable()) {
            val player = event.player
            sendTabList(player)
            val scoreboard = ScoreboardUtils(player, "TabooPublicWork")
            scoreboards[player.uniqueId] = scoreboard
            scoreboard.setTitle(scoreboardData.title)
            scoreboard.scores.addAll(scoreboardData.lines)
        }
    }

    @SubscribeEvent
    fun onQuit(event: PlayerQuitEvent) {
        if (ModuleScoreboard.isEnable()) {
            val player = event.player
            scoreboards.remove(player.uniqueId)
        }
    }

    @SubscribeEvent
    fun onQuit(event: PlayerKickEvent) {
        if (ModuleScoreboard.isEnable()) {
            val player = event.player
            scoreboards.remove(player.uniqueId)
        }
    }

    private fun sendTabList(player: Player) {
        val a = classChatSerializer.invokeMethod<Any>(
            "a",
            "{\"text\":\"" + headerTabList.colored().joinToString("\n") { it.replacePlaceholder(player) } + "\"}",
            isStatic = true
        )!!
        val b = classChatSerializer.invokeMethod<Any>(
            "b",
            "{\"text\":\"" + footerTabList.colored().joinToString("\n") { it.replacePlaceholder(player) } + "\"}",
            isStatic = true
        )!!
        player.sendPacket(nmsClass("PacketPlayOutPlayerListHeaderFooter").unsafeInstance().also {
            it.setProperty("a", a)
            it.setProperty("b", b)
        })
    }

    private val classChatSerializer by unsafeLazy {
        nmsClass("IChatBaseComponent\$ChatSerializer")
    }
}