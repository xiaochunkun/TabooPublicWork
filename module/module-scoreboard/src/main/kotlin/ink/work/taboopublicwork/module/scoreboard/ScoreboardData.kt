package ink.work.taboopublicwork.module.scoreboard

import taboolib.library.configuration.ConfigurationSection

data class ScoreboardData(var title: String = "", var lines: List<String> = listOf()) {
    constructor(section: ConfigurationSection, key: String) : this() {
        this.title = section.getString("$key.title") ?: ""
        this.lines = section.getStringList("$key.lines")
    }

}
