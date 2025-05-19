package com.cht.mybankapp.domain.contract

import com.cht.mybankapp.data.model.Account

interface AccountContract {

    // Интерфейс для представления (View) - отвечает за отображение данных и сообщений
    interface View {

        // Отображение списка счетов
        fun showAccounts(accounts: List<Account>)

        // Отображение сообщения об ошибке
        fun showError(message: String)

        // Отображение сообщения об успешной операции
        fun showSuccess(message: String)
    }

    // Интерфейс для презентера (Presenter) - отвечает за обработку данных и взаимодействие с View
    interface Presenter {

        // Загрузка списка счетов
        fun loadAccounts()

        // Добавление нового счета
        fun addAccount(name: String, balance: String, currency: String)

        // Удаление счета
        fun deleteAccount(accountId: String)

        // Полное обновление данных счета
        fun updateAccountFully(accountId: String, account: Account)

        // Изменение статуса активности счета
        fun updateAccountStatus(accountId: String, isActive: Boolean)

    }

}