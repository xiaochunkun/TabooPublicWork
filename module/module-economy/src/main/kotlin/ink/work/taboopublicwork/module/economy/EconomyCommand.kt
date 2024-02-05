package ink.work.taboopublicwork.module.economy

import ink.work.taboopublicwork.api.ICommand
import ink.work.taboopublicwork.module.economy.ModuleEconomy.sendLang
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.ProxyPlayer
import taboolib.common.platform.command.*
import taboolib.expansion.createHelper


@CommandHeader(name = "eco", permission = "taboopublicwork.command.eco")
object EconomyCommand : ICommand {

    override val command: String = "eco"

    @Awake(LifeCycle.LOAD)
    fun init() {
        register(ModuleEconomy)
    }

    @CommandBody
    val main = mainCommand {
//        dynamic("name") {
//            suggestion<CommandSender> { _, context ->
//                ModuleWarp.database.getDataList().map { it.name }
//            }
//            execute<Player> { sender, context, argument ->
//                val name = context["name"]
//                val warpData = ModuleWarp.database.getData(name)
//                if (warpData == null) {
//                    sender.sendLang("module-warp-warp-not-exists", name)
//                    return@execute
//                }
//                val location = warpData.toLocation()
//                sender.teleport(location)
//                sender.sendLang("module-warp-warp-success", name)
//            }
//        }
        createHelper()
    }

    @CommandBody
    val set = subCommand {
        dynamic("玩家名") {
            suggestion<CommandSender> { _, _ ->
                Bukkit.getOfflinePlayers().mapNotNull { it.name }
            }
            decimal("数量") {
                execute<CommandSender> { sender, context, _ ->
                    val amount = context.double("数量")
                    val name = context["玩家名"]
                    ModuleEconomy.vaultManager.economy.setBalance(name, amount)
                    sender.sendLang("new-balance", name, amount)
                }
            }
        }
    }

    @CommandBody
    val give = subCommand {
        dynamic("玩家名") {
            suggestion<CommandSender> { _, _ ->
                Bukkit.getOfflinePlayers().mapNotNull { it.name }
            }
            decimal("数量") {
                execute<CommandSender> { sender, context, _ ->
                    val amount = context.double("数量")
                    val name = context["玩家名"]
                    ModuleEconomy.vaultManager.economy.depositPlayer(name, amount)
                    sender.sendLang("give-balance", name, amount)
                }
            }
        }
    }

    @CommandBody
    val take = subCommand {
        dynamic("玩家名") {
            suggestion<CommandSender> { _, _ ->
                Bukkit.getOfflinePlayers().mapNotNull { it.name }
            }
            decimal("数量") {
                execute<CommandSender> { sender, context, _ ->
                    val amount = context.double("数量")
                    val name = context["玩家名"]
                    ModuleEconomy.vaultManager.economy.withdrawPlayer(name, amount)
                    sender.sendLang("take-balance", name, amount)
                }
            }
        }
    }

    @Awake(LifeCycle.ENABLE)
    fun money() {
        simpleCommand(
            "money",
            permission = "taboopublicwork.command.money",
            permissionDefault = PermissionDefault.TRUE
        ) { sender, _ ->
            if (sender is ProxyPlayer) {
                val player = sender.cast<Player>()
                val economy = ModuleEconomy.vaultManager.economy
                player.sendLang("money", economy.format(economy.getBalance(player.name)))
            }
        }
    }

}