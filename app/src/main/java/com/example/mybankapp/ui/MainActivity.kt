package com.cht.mybankapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.cht.mybankapp.data.model.Account
import com.example.mybankapp.ui.AccountViewModel
import com.example.hw.R
import com.example.hw.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    // Переменная для ViewBinding
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    // Презентер для управления логикой работы со счетами
    private val viewModel: AccountViewModel by viewModels()
    // Адаптер для отображения списка счетов
    private lateinit var adapter: AccountAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Привязываем макет к активности с помощью ViewBinding
        _binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Инициализация адаптера с обработчиками действий
        adapter = AccountAdapter(
            // Обработка удаления аккаунта
            onDelete = { id ->
                viewModel.deleteAccount(id)
            },
            // Обработка нажатия кнопки редактирования
            onEdit = { account ->
                //show edit dialog
                showEditDialog(account)
            },
            // Обработка переключения статуса
            onStatusToggle = { id, isChecked ->
                viewModel.updateAccountStatus(id, isChecked)
            }
        )
        // Настройка recyclerView (вертикальный список)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        // Установка адаптера
        binding.recyclerView.adapter = adapter

        // Обработка нажатия кнопки Добавить счет
        binding.btnAdd.setOnClickListener {
            showAddDialog()
        }

        // Загрузка списка счетов
        viewModel.loadAccounts()
        subscribeToLiveData()
    }

    private fun subscribeToLiveData(){
        // Подписываюясь через observe к Livedata переменной accounts из viewModel
        // и обновляю адаптер
        viewModel.accounts.observe(this){
            adapter.submitList(it)
        }
        // Подписываюсь к Livedata переменной SuccessMessage из viewModel
        // и показываю тост с ссобщением об успехе
        viewModel.successMessage.observe(this){
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
        // Подписываюсь к Livedata переменной ErrorMessage из viewModel
        // и показываю тост с ссобщением об ошибке
        viewModel.errorMessage.observe(this){
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
    }


    // Показ диалогового окна для добавления нового счета
    private fun showAddDialog() {
        // Создание и настройка пользовательского макета диалога
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_account, null)
        val nameInput = dialogView.findViewById<EditText>(R.id.etName)
        val balanceInput = dialogView.findViewById<EditText>(R.id.etBalance)
        val currencyInput = dialogView.findViewById<EditText>(R.id.etCurrency)

        // Создание и отображение диалогового окна
        AlertDialog.Builder(this)
            .setTitle("Добавить счёт") // Заголовок диалога
            .setView(dialogView) // Установка пользовательского макета
            .setPositiveButton("Добавить") { _, _ ->
                // Получение данных из полей ввода
                val name = nameInput.text.toString()
                val balance = balanceInput.text.toString()
                val currency = currencyInput.text.toString()

                // Вызов метода добавления счета в презентере
                viewModel.addAccount(name, balance, currency)
            }
            .setNegativeButton("Отмена", null) // Кнопка отмены
            .show()
    }

    // Метод отображения диалога для редактирования счета
    private fun showEditDialog(account: Account) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_account, null)
        val nameInput = dialogView.findViewById<EditText>(R.id.etName)
        val balanceInput = dialogView.findViewById<EditText>(R.id.etBalance)
        val currencyInput = dialogView.findViewById<EditText>(R.id.etCurrency)

        // Заполняем текущими данными
        nameInput.setText(account.name)
        balanceInput.setText(account.balance)
        currencyInput.setText(account.currency)

        // Отображение диалога редактирования
        AlertDialog.Builder(this)
            .setTitle("Редактировать счёт")
            .setView(dialogView)
            .setPositiveButton("Обновить") { _, _ ->
                // Получаем обновленные данные
                val name = nameInput.text.toString()
                val balance = balanceInput.text.toString()
                val currency = currencyInput.text.toString()

                // Создаем копию объекта с обновленными полями
                val updated = account.copy(
                    name = name,
                    balance = balance,
                    currency = currency
                )

                // Обновляем счет через viewModel
                viewModel.updateAccountFully(updated.id!!, updated)
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

}