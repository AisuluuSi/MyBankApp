package com.example.mybankapp.ui
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cht.mybankapp.data.api.AccountApi
import com.cht.mybankapp.data.api.ApiClient
import com.cht.mybankapp.data.model.Account
import com.cht.mybankapp.data.model.PatchAccountStatusDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val accountApi: AccountApi
): ViewModel() {

    // Приватная переменная LiveData для хранения списка аккаунтов
    // доступна только внутри ViewModel
    private val _accounts = MutableLiveData<List<Account>>()
    // Публичная неизменяемая LiveData предоставляет доступ к списку аккаунтов
    val accounts: LiveData<List<Account>> = _accounts

    // Приватная переменная LiveData для хранения сообщений об успешных действиях
    private val _successMessage = MutableLiveData<String>()
    // Публичная неизменяемая LiveData предоставляет доступ к сообщениям об успехе
    val successMessage: LiveData<String> = _successMessage

    // Приватная переменная LiveData для хранения сообщений об ошибках
    private val _errorMessage = MutableLiveData<String>()
    // Публичная неизменяемая LiveData предоставляет доступ к сообщениям об ошибках
    val errorMessage: LiveData<String> = _errorMessage

    // Загрузка списка счетов
    fun loadAccounts() {
        // Выполняем асинхронный запрос к API для получения списка счетов
        accountApi.getAccounts().enqueue(object : Callback<List<Account>> {
            // Обработка успешного ответа
            override fun onResponse(call: Call<List<Account>>, response: Response<List<Account>>) {
                if (response.isSuccessful) {
                    _accounts.value = response.body() ?: emptyList()
                } else {
                    // Показываем сообщение об ошибке, если ответ не успешен
                    _errorMessage.value = "Ошибка загрузки"
                }
            }

            // Обработка ошибки сети или других исключений
            override fun onFailure(call: Call<List<Account>>, t: Throwable) {
                // Показываем сообщение об ошибке сети
                _errorMessage.value = "Ошибка сети: ${t.message}"
            }
        })
    }

    // Добавление нового счета
    fun addAccount(name: String, balance: String, currency: String) {
        // Создаем объект Account с переданными данными
        val account = Account(name = name, balance = balance, currency = currency, isActive = true)

        // Выполняем асинхронный запрос к API для создания нового счета
        accountApi.createAccount(account).enqueue(object : Callback<Account> {
            // Обработка успешного ответа
            override fun onResponse(call: Call<Account>, response: Response<Account>) {
                if (response.isSuccessful) {
                    // Показываем сообщение об успешном добавлении счета
                    _successMessage.value = "Аккаунт добавлен"
                    // Обновляем список счетов после добавления
                    loadAccounts()
                } else {
                    // Показываем сообщение об ошибке, если ответ не успешен
                    _errorMessage.value = "Ошибка добавления"
                }
            }

            // Обработка ошибки сети или других исключений
            override fun onFailure(call: Call<Account>, t: Throwable) {
                // Показываем сообщение об ошибке сети
                _errorMessage.value = "Ошибка сети: ${t.message}"
            }
        })
    }

    // Удаление счета
    fun deleteAccount(accountId: String) {
        accountApi.deleteAccount(accountId).enqueue(object: Callback<Unit> {
            // Обработка успешного ответа
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                // Показываем сообщение об успешном удалении счета
                if (response.isSuccessful) {
                    _successMessage.value = "Удалено"
                    // Обновляем список счетов после удаления
                    loadAccounts()
                } else {
                    // Показываем сообщение об ошибке, если ответ не успешен
                    _errorMessage.value = "Ошибка удаления"
                }
            }

            // Обработка ошибки сети или других исключений
            override fun onFailure(call: Call<Unit>, t: Throwable) {
                // Показываем сообщение об ошибке сети
                _errorMessage.value = "Ошибка сети: ${t.message}"
            }

        } )
    }

    // Обновление счета
    fun updateAccountFully(accountId: String, account: Account) {
        accountApi.updateAccountFully(accountId, account).enqueue(object: Callback<Account>{
            // Обработка успешного ответа
            override fun onResponse(call: Call<Account>, response: Response<Account>) {
                // Показываем сообщение об успешном обновлении счета
                if (response.isSuccessful) {
                    _successMessage.value = "Успешно обновлен счет"
                    // Обновляем список счетов после обновления
                    loadAccounts()
                } else {
                    // Показываем сообщение об ошибке, если ответ не успешен
                    _errorMessage.value = "Ошибка обновления счета"
                }
            }

            // Обработка ошибки сети или других исключений
            override fun onFailure(call: Call<Account>, t: Throwable) {
                // Показываем сообщение об ошибке сети
                _errorMessage.value = "Ошибка сети: ${t.message}"
            }

        })
    }

    // Обновление статуса аккаунта
    fun updateAccountStatus(accountId: String, isActive: Boolean) {
        accountApi.patchAccountStatus(accountId, PatchAccountStatusDTO(isActive)).enqueue(object: Callback<Account>{
            // Обработка успешного ответа
            override fun onResponse(call: Call<Account>, response: Response<Account>) {
                // Показываем сообщение об успешном обновлении статуса счета
                if (response.isSuccessful) {
                    _successMessage.value = "Успешно обновлен cтатус счета"
                    // Обновляем список счетов после обновления
                    loadAccounts()
                } else {
                    // Показываем сообщение об ошибке, если ответ не успешен
                    _errorMessage.value = "Ошибка обновления статуса счета"
                }
            }

            // Обработка ошибки сети или других исключений
            override fun onFailure(call: Call<Account>, t: Throwable) {
                // Показываем сообщение об ошибке сети
                _errorMessage.value = "Ошибка сети: ${t.message}"
            }

        })
    }
}