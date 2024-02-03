package ink.work.taboopublicwork.module.economy.database

import ink.work.taboopublicwork.module.economy.ModuleEconomy
import ink.work.taboopublicwork.utils.sql.SQLContext
import ink.work.taboopublicwork.utils.sql.impl.Database
import java.util.concurrent.ConcurrentHashMap

class EconomyDatabase : IEconomyDatabase,SQLContext {
    override lateinit var database: Database

    private val cache = ConcurrentHashMap<String, Double>()

    private val container by lazy {
        buildContainer(ModuleEconomy) {
        }
    }

    init {
        container.keys()
    }

    override fun load() {
        cache.clear()
        container.values().forEach { (t, u) ->
            cache[t] = u.toDoubleOrNull() ?: -1.0
        }
    }

    override fun addBalance(name: String, money: Double) {
        setAccount(name, getBalance(name) + money)
    }

    override fun insertAccount(name: String): Double {
        setAccount(name, 0.0)
        return 0.0
    }

    override fun getBalance(name: String): Double {
        return cache[name] ?: run {
            container[name]?.let {
                val money = it.toDoubleOrNull() ?: -1.0
                cache[name] = money
                return money
            } ?: run {
                insertAccount(name)
            }
        }
    }

    override fun hasAccount(name: String): Boolean {
        return cache.containsKey(name) || container[name] != null
    }

    override fun setAccount(name: String, money: Double) {
        cache[name] = money
        container[name] = money.toString()
    }
}