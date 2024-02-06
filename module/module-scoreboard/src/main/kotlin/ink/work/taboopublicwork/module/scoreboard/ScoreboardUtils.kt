package ink.work.taboopublicwork.module.scoreboard

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.scoreboard.Objective
import taboolib.module.chat.colored
import taboolib.platform.compat.replacePlaceholder
import java.util.*

class ScoreboardUtils(private val player: Player, private val name: String) {

    val scores = mutableListOf<String>()

    private val keyToScore = mutableMapOf<String, String>()

    private val scoreboard = Bukkit.getScoreboardManager()!!.newScoreboard

    private val objective: Objective

    init {
        player.scoreboard = scoreboard
        objective = scoreboard.registerNewObjective(name, "dummy")
        objective.displaySlot = DisplaySlot.SIDEBAR
        for (i in 0 until 22) {
            val team = scoreboard.getTeam("line$i") ?: scoreboard.registerNewTeam("line$i")
            team.entries.forEach {
                team.removeEntry(it)
            }
            team.addEntry(genEntry(i))
        }
    }

    fun setTitle(title: String) {
        title.colored().let {
            objective.displayName = if (it.length > 32) it.substring(0, 32) else it
        }
    }

    private fun setSlot(slot: Int, text: String) {
        val team = scoreboard.getTeam("line$slot") ?: scoreboard.registerNewTeam("line$slot")
        val entry = genEntry(slot)
        if (!scoreboard.entries.contains(entry)) {
            objective.getScore(entry).score = slot
        }
        var key: String = text.colored()
        keyToScore.forEach { (t, u) ->
            key = text.replace("{${t}}", u)
        }
        key = key.replacePlaceholder(player = player)

        val prefix = getFirstSplit(key)
        val suffix = getFirstSplit(ChatColor.getLastColors(prefix) + getSecondSplit(key))
        team.prefix = prefix
        team.suffix = suffix
    }

    fun resetSlot(){
        val newScores = ArrayList(scores)
        newScores.reverse()
        for (i in 0 until newScores.size){
            setSlot(i, newScores[i])
        }
    }

    private fun genEntry(slot: Int): String {
        return ChatColor.values()[slot].toString()
    }

    private fun getFirstSplit(str: String): String {
        return if (str.length > 16) {
            str.substring(0, 16)
        } else {
            str
        }
    }

    private fun getSecondSplit(str: String): String {
        return if (str.length > 32) {
            str.substring(0, 32)
        } else {
            if (str.length > 16) {
                str.substring(16)
            } else {
                ""
            }
        }
    }
}