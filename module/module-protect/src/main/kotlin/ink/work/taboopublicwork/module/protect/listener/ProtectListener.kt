package ink.work.taboopublicwork.module.protect.listener

import ink.work.taboopublicwork.TabooPublicWork
import ink.work.taboopublicwork.module.protect.ModuleProtect
import ink.work.taboopublicwork.module.protect.data.ProtectData
import ink.work.taboopublicwork.module.protect.utils.containsIgnoreCase
import ink.work.taboopublicwork.module.protect.utils.getFirstPluginVersion
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.block.*
import org.bukkit.event.entity.*
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.event.player.PlayerToggleFlightEvent
import org.bukkit.event.weather.WeatherChangeEvent
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.metadata.MetadataValue
import taboolib.common.platform.event.SubscribeEvent
import taboolib.library.xseries.XMaterial

object ProtectListener {

    // 禁止破坏
    @SubscribeEvent
    fun onBlockPlace(event: BlockPlaceEvent) {
        val player = event.player
        val block = event.block
        val world = player.world.name

        ModuleProtect.getProtectData(world).forEach { data ->
            if (accept(data, "build", player, block.type)) event.isCancelled = true
        }

    }

    // 禁止建造
    @SubscribeEvent
    fun onBlockBreak(event: BlockBreakEvent) {
        val player = event.player
        val block = event.block
        val world = player.world.name

        ModuleProtect.getProtectData(world).forEach { data ->
            if (accept(data, "break", player, block.type)) event.isCancelled = true
        }
    }

    // 禁止使用
    @SubscribeEvent
    fun onInteract(event: PlayerInteractEvent) {
        val action = event.action
        if (action != Action.RIGHT_CLICK_BLOCK && action != Action.RIGHT_CLICK_AIR) return
        val itemStack = event.item ?: return
        val player = event.player

        val world = player.world.name

        ModuleProtect.getProtectData(world).forEach { data ->
            if (accept(data, "use", player, itemStack.type)) event.isCancelled = true
        }
    }

    private fun accept(data: ProtectData, key: String, player: Player, type: Material): Boolean {
        if (!data.name.equals(key, true)) return false
        if (!data.enabled) return false
        if (data.op || !player.isOp) {
            if (data.typeALL) {
                return true
            } else {
                if (data.getTypeList().contains(XMaterial.matchXMaterial(type))) {
                    return true
                }
            }
        }
        return false
    }

    // 禁止爆炸
    @SubscribeEvent
    fun onExplode(event: EntityExplodeEvent) {

        val world = event.location.world?.name ?: return

        ModuleProtect.getProtectData(world).forEach { data ->
            if (!data.name.equals("explode", true)) return@forEach
            if (data.enabled) {
                event.isCancelled = true
            }
        }
    }

    // 天气保持晴天
    @SubscribeEvent
    fun onWeatherChange(event: WeatherChangeEvent) {
        ModuleProtect.allProtectDataList.forEach { data ->
            if (!data.name.equals("weather", true)) return@forEach
            if (!data.enabled) return@forEach
            event.isCancelled = true
        }

        val world = event.world

        ModuleProtect.worldProtectDataMap[world.name]?.forEach { data ->
            if (!data.name.equals("weather", true)) return@forEach
            if (data.enabled) {
                event.isCancelled = true
            }
        }
    }

    // 禁止玩家丢弃
    @SubscribeEvent
    fun onDropItem(event: PlayerDropItemEvent) {
        val player = event.player
        val world = player.world.name

        ModuleProtect.getProtectData(world).forEach { data ->
            if (!data.name.equals("itemDrop", true)) return@forEach
            if (data.enabled) {
                event.isCancelled = true
            }
        }
    }

    // 禁止玩家移动
    @SubscribeEvent
    fun onMove(event: PlayerMoveEvent) {
        val player = event.player

        val world = player.world.name

        ModuleProtect.getProtectData(world).forEach { data ->
            if (!data.name.equals("move", true)) return@forEach
            if (data.enabled && (data.op || !player.isOp)) {
                event.isCancelled = true
            }
        }
    }

    // 禁止玩家飞行
    @SubscribeEvent
    fun onFly(event: PlayerToggleFlightEvent) {
        val player = event.player
        val world = player.world.name

        ModuleProtect.getProtectData(world).forEach { data ->
            if (!data.name.equals("fly", true)) return@forEach
            if (data.enabled && (data.op || !player.isOp)) {
                event.isCancelled = true
            }
        }
    }

    // 保持物品不掉落
    @SubscribeEvent
    fun onKeepInventory(event: PlayerDeathEvent) {
        val player = event.entity
        val world = player.world.name

        ModuleProtect.getProtectData(world).forEach { data ->
            if (!data.name.equals("keepInventory", true)) return@forEach
            if (data.enabled && (data.op || !player.isOp)) {
                event.keepInventory = true
            }
        }
    }

    // 保持经验不掉落
    @SubscribeEvent
    fun onKeepExperience(event: PlayerDeathEvent) {
        val player = event.entity
        val world = player.world.name

        ModuleProtect.getProtectData(world).forEach { data ->
            if (!data.name.equals("keepLevel", true)) return@forEach
            if (data.enabled && (data.op || !player.isOp)) {
                event.keepLevel = true
            }
        }
    }

    // 禁止火焰、菌丝蔓延
    @SubscribeEvent
    fun onSpread(event: BlockSpreadEvent) {
        val world = event.block.world.name

        ModuleProtect.getProtectData(world).forEach { data ->
            if (!data.name.equals("spread", true)) return@forEach
            if (data.enabled) {
                event.isCancelled = true
            }
        }
    }

    // 禁止方块点燃
    @SubscribeEvent
    fun onIgnite(event: BlockIgniteEvent) {
        val world = event.block.world.name

        ModuleProtect.getProtectData(world).forEach { data ->
            if (!data.name.equals("ignite", true)) return@forEach
            if (data.enabled) {
                event.isCancelled = true
            }
        }
    }

    // 禁止雪、冰的融化  方块的燃烧消失
    @SubscribeEvent
    fun onFade(event: BlockFadeEvent) {
        val world = event.block.world.name

        ModuleProtect.getProtectData(world).forEach { data ->
            if (!data.name.equals("fade", true)) return@forEach
            if (data.enabled) {
                event.isCancelled = true
            }
        }
    }

    // 禁止树叶凋落
    @SubscribeEvent
    fun onLeaves(event: LeavesDecayEvent) {
        val world = event.block.world.name

        ModuleProtect.getProtectData(world).forEach { data ->
            if (!data.name.equals("leaves", true)) return@forEach
            if (data.enabled) {
                event.isCancelled = true
            }
        }
    }

    // 禁止生物生成
    @SubscribeEvent
    fun onSpawn(event: CreatureSpawnEvent) {
        val world = event.entity.world.name

        ModuleProtect.getProtectData(world).forEach { data ->
            if (!data.name.equals("spawn", true)) return@forEach
            if (!data.enabled) return@forEach
            if (Bukkit.getPluginManager().isPluginEnabled("MythicMobs")) {
                val clazz = if (Bukkit.getPluginManager().getPlugin("MythicMobs")!!.getFirstPluginVersion() >= 5) {
                    Class.forName("io.lumine.mythic.core.mobs.MobExecutor")
                } else {
                    Class.forName("io.lumine.xikage.mythicmobs.mobs.MobManager")
                }

                val spawnFlag = clazz.getField("spawnflag")
                val result = spawnFlag.get(clazz) as Boolean
                if (result) return
            }
            event.isCancelled = true
        }
    }

    // 禁止耕地踩坏
    @SubscribeEvent
    fun onSoil(event: PlayerInteractEvent) {
        val player = event.player
        val block = event.clickedBlock ?: return
        val world = player.world.name

        ModuleProtect.getProtectData(world).forEach { data ->
            if (!data.name.equals("soil", true)) return@forEach
            if (data.enabled && (data.op || !player.isOp) && block.type == XMaterial.FARMLAND.parseMaterial()!!) {
                event.isCancelled = true
            }
        }
    }

    // 禁止打开容器
    @SubscribeEvent
    fun onContainer(event: PlayerInteractEvent) {
        val player = event.player
        if (event.action != Action.RIGHT_CLICK_BLOCK) return
        val block = event.clickedBlock ?: return
        val world = player.world.name

        ModuleProtect.getProtectData(world).forEach { data ->
            if (!data.name.equals("container", true)) return@forEach
            if (data.enabled && (data.op || !player.isOp) && block.type == XMaterial.FARMLAND.parseMaterial()!!) {
                event.isCancelled = true
            }
        }
    }

    // 禁止鸡生蛋
    @SubscribeEvent
    fun onEgg(event: ItemSpawnEvent) {
        val entity = event.entity
        val world = entity.world.name

        ModuleProtect.getProtectData(world).forEach { data ->
            if (!data.name.equals("egg", true)) return@forEach
            if (data.enabled && entity.type == EntityType.DROPPED_ITEM && entity.itemStack.type == XMaterial.EGG.parseMaterial()!!) {
                event.isCancelled = true
            }
        }
    }

    // 是否禁止掉饱食度
    @SubscribeEvent
    fun onFood(event: FoodLevelChangeEvent) {
        val player = event.entity
        val world = player.world.name

        ModuleProtect.getProtectData(world).forEach { data ->
            if (!data.name.equals("food", true)) return@forEach
            if (!data.enabled) return@forEach
            if (player.isOp) {
                if (data.type.containsIgnoreCase("op")) {
                    event.isCancelled = true
                }
            } else {
                if (data.type.containsIgnoreCase("player")) {
                    event.isCancelled = true
                }
            }
        }
    }

    // 禁止流体流动
    @SubscribeEvent
    fun onFluid(event: BlockFromToEvent) {
        val block = event.block
        val world = block.world.name

        ModuleProtect.getProtectData(world).forEach { data ->
            if (!data.name.equals("fluid", true)) return@forEach
            if (!data.enabled) return@forEach
            if (data.typeALL) {
                event.isCancelled = true
            } else {
                if (data.getTypeList().contains(XMaterial.matchXMaterial(block.type))) {
                    event.isCancelled = true
                }
            }
        }
    }

}