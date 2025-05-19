package com.cht.mybankapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.RecyclerView
import com.cht.mybankapp.data.model.Account
import com.example.hw.R

class AccountAdapter (
    // Функция для удаления
    val onDelete: (String) -> Unit,
    val onEdit: (Account) -> Unit,
    val onStatusToggle: (String, Boolean) -> Unit
) : RecyclerView.Adapter<AccountAdapter.AccountViewHolder>() {

    private val items = mutableListOf<Account>()

    // Метод для обновления списка данных в адаптере
    fun submitList(data: List<Account>) {
        items.clear() // Очищаем текущий список
        items.addAll(data) // Добавляем новые данные
        notifyDataSetChanged() // Уведомляем RecyclerView об изменениях
    }

    // Создание ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_account, parent, false)
        return AccountViewHolder(view)
    }

    // Привязка данных к элементу списка
    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
        holder.bind(items[position])
    }

    // Количество элементов в списке
    override fun getItemCount(): Int = items.size

    // ViewHolder для одного аккаунта
    inner class AccountViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Заполнение данных аккаунта
        fun bind(account: Account) = with(itemView) {
            findViewById<TextView>(R.id.tvName).text = account.name
            findViewById<TextView>(R.id.tvBalance).text = "${account.balance} ${account.currency}"
            // Обработка нажатия кнопки удаления
            val btnDelete = findViewById<Button>(R.id.btnDelete)
            btnDelete.setOnClickListener {
                account.id?.let { onDelete(it) }
            }

            // Обработка нажатия кнопки изменения
            val btnEdit = findViewById<Button>(R.id.btnEdit)
            btnEdit.setOnClickListener {
                onEdit(account)
            }

            // Обработка нажатия кнопки переключения статуса
            val switchActive = findViewById<SwitchCompat>(R.id.switchActive)
            switchActive.setOnCheckedChangeListener(null)
            switchActive.isChecked = account.isActive
            switchActive.setOnCheckedChangeListener { buttonView, isChecked ->
                account.id?.let { onStatusToggle(it, isChecked) }
            }


        }
    }
}