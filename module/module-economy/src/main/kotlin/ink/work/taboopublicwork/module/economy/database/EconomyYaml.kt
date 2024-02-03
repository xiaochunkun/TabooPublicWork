package ink.work.taboopublicwork.module.economy.database

import ink.work.taboopublicwork.api.IMultipleFiles
import ink.work.taboopublicwork.module.economy.ModuleEconomy
import taboolib.common.io.newFile
import taboolib.module.configuration.Configuration
import java.io.File
import java.util.concurrent.ConcurrentHashMap

class EconomyYaml : IEconomyDatabase,IMultipleFiles {
    override val files = mutableListOf<Configuration>()

    private val data = ConcurrentHashMap<String, Double>()

    override fun load() {
        data.clear()
        files.clear()
        loadFile(File(ModuleEconomy.getFilePath(), "economy"))


        files.forEach {
            it.getKeys(false).forEach { key ->
                data[key] = it.getDouble(key, -1.0)
            }
        }
    }

    override fun addBalance(name: String, money: Double) {
        setAccount(name, getBalance(name) + money)
    }

    override fun insertAccount(name: String): Double {
        data[name] = -1.0
        val newFile = newFile(File(ModuleEconomy.getFilePath(), "economy"), "${name}.yml", create = true)
        val configuration = Configuration.loadFromFile(newFile)
        configuration["money"] = -1.0
        configuration.saveToFile()
        return -1.0
    }

    override fun getBalance(name: String): Double {
        return data[name] ?: run {
            files.forEach {
                it.getKeys(false).forEach { key ->
                    if (key == name) {
                        val money = it.getDouble(key, -1.0)
                        data[name] = money
                        return money
                    }
                }
            }
            insertAccount(name)
        }
    }

    override fun hasAccount(name: String): Boolean {
        return data.containsKey(name) || files.any { it.contains(name) }
    }

    override fun setAccount(name: String, money: Double) {
        data[name] = money
        files.filter { it.contains(name) }.forEach {
            it[name] = money
            it.saveToFile()
        }
    }
}