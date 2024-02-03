package ink.work.taboopublicwork.module.economy

import net.milkbowl.vault.economy.AbstractEconomy
import net.milkbowl.vault.economy.EconomyResponse
import java.text.DecimalFormat

class VaultHandler : AbstractEconomy() {
    override fun isEnabled(): Boolean = true

    override fun getName(): String = "TabooPublicWorkEconomy"

    override fun hasBankSupport(): Boolean {
        return false
    }

    override fun fractionalDigits(): Int {
        return -1
    }

    override fun format(amount: Double): String {
        val decimalFormat = DecimalFormat(ModuleEconomy.moneyFormat)
        return decimalFormat.format(amount)
    }

    override fun currencyNamePlural(): String {
        return ""
    }

    override fun currencyNameSingular(): String {
        return ""
    }

    override fun hasAccount(playerName: String): Boolean {
        return ModuleEconomy.database.hasAccount(playerName)
    }

    override fun hasAccount(playerName: String, worldName: String): Boolean {
        return hasAccount(playerName)
    }

    override fun getBalance(playerName: String): Double {
        return ModuleEconomy.database.getBalance(playerName)
    }

    override fun getBalance(playerName: String, worldName: String): Double {
        return getBalance(playerName)
    }

    override fun has(playerName: String, amount: Double): Boolean {
        return getBalance(playerName) >= amount
    }

    override fun has(playerName: String, worldName: String, amount: Double): Boolean {
        return has(playerName, amount)
    }

    override fun withdrawPlayer(playerName: String, amount: Double): EconomyResponse {
        if (amount < 0) {
            return EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE, "Cannot deposit negative funds")
        }
        if (getBalance(playerName) < amount) {
            return EconomyResponse(0.0, getBalance(playerName), EconomyResponse.ResponseType.FAILURE,
                "Could not withdraw $amount from $playerName because they don't have enough funds"
            )
        }
        ModuleEconomy.database.addBalance(playerName, -amount)
        return EconomyResponse(amount, getBalance(playerName), EconomyResponse.ResponseType.SUCCESS, "")
    }

    override fun withdrawPlayer(playerName: String, worldName: String, amount: Double): EconomyResponse {
        return withdrawPlayer(playerName, amount)
    }

    override fun depositPlayer(playerName: String, amount: Double): EconomyResponse {
        if (amount < 0) {
            return EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.FAILURE, "Cannot deposit negative funds")
        }
        ModuleEconomy.database.addBalance(playerName, amount)
        return EconomyResponse(amount, getBalance(playerName), EconomyResponse.ResponseType.SUCCESS, "")
    }

    override fun createPlayerAccount(playerName: String): Boolean {
        return false
    }

    override fun createPlayerAccount(playerName: String, worldName: String): Boolean {
        return createPlayerAccount(playerName)
    }

    override fun depositPlayer(playerName: String, worldName: String, amount: Double): EconomyResponse {
        return depositPlayer(playerName, amount)
    }

    override fun createBank(playerName: String, worldName: String): EconomyResponse {
        return EconomyResponse(
            0.0,
            0.0,
            EconomyResponse.ResponseType.NOT_IMPLEMENTED,
            "TabooPublicWorkEconomy does not support bank accounts!"
        )
    }

    override fun deleteBank(playerName: String): EconomyResponse {
        return EconomyResponse(
            0.0,
            0.0,
            EconomyResponse.ResponseType.NOT_IMPLEMENTED,
            "TabooPublicWorkEconomy does not support bank accounts!"
        )
    }

    override fun bankBalance(playerName: String): EconomyResponse {
        return EconomyResponse(
            0.0,
            0.0,
            EconomyResponse.ResponseType.NOT_IMPLEMENTED,
            "TabooPublicWorkEconomy does not support bank accounts!"
        )
    }

    override fun bankHas(playerName: String, amount: Double): EconomyResponse {
        return EconomyResponse(
            0.0,
            0.0,
            EconomyResponse.ResponseType.NOT_IMPLEMENTED,
            "TabooPublicWorkEconomy does not support bank accounts!"
        )
    }

    override fun bankWithdraw(playerName: String, amount: Double): EconomyResponse {
        return EconomyResponse(
            0.0,
            0.0,
            EconomyResponse.ResponseType.NOT_IMPLEMENTED,
            "TabooPublicWorkEconomy does not support bank accounts!"
        )
    }

    override fun bankDeposit(playerName: String, amount: Double): EconomyResponse {
        return EconomyResponse(
            0.0,
            0.0,
            EconomyResponse.ResponseType.NOT_IMPLEMENTED,
            "TabooPublicWorkEconomy does not support bank accounts!"
        )
    }

    override fun isBankOwner(playerName: String, worldName: String): EconomyResponse {
        return EconomyResponse(
            0.0,
            0.0,
            EconomyResponse.ResponseType.NOT_IMPLEMENTED,
            "TabooPublicWorkEconomy does not support bank accounts!"
        )
    }

    override fun isBankMember(playerName: String, worldName: String): EconomyResponse {
        return EconomyResponse(
            0.0,
            0.0,
            EconomyResponse.ResponseType.NOT_IMPLEMENTED,
            "TabooPublicWorkEconomy does not support bank accounts!"
        )
    }

    override fun getBanks(): List<String> {
        return listOf()
    }

}