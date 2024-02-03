package ink.work.taboopublicwork.module.economy.database

interface IEconomyDatabase {

    fun load()

    fun addBalance(name: String, money: Double)

    fun insertAccount(name: String): Double

    fun getBalance(name: String): Double

    fun hasAccount(name: String): Boolean

    fun setAccount(name: String, money: Double)

}