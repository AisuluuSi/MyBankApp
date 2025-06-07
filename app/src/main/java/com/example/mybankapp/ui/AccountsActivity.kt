package com.cht.mybankapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.cht.mybankapp.databinding.ActivityAccountsBinding
import com.cht.mybankapp.ui.account_detail.AccountDetailsActivity
import com.example.hw.R
import com.example.mybankapp.ui.AccountViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountsActivity : AppCompatActivity(){

    private var _binding: ActivityAccountsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AccountViewModel by viewModels()

    private lateinit var adapter: AccountAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAccountsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        adapter = AccountAdapter(
            onStatusToggle = { id, isChecked ->
                viewModel.updateAccountStatus(id, isChecked)
            },
            onItemClick = { id ->
                val intent = Intent(this, AccountDetailsActivity::class.java)
                intent.putExtra("account_id", id)
                startActivity(intent)
            }
        )
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        binding.btnAdd.setOnClickListener {
            showAddDialog()
        }

        viewModel.loadAccounts()
        subscribeToLiveData()
    }

    private fun subscribeToLiveData(){
        viewModel.accounts.observe(this){
            adapter.submitList(it)
        }
        viewModel.successMessage.observe(this){
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
        viewModel.errorMessage.observe(this){
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun showAddDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_account, null)
        val nameInput = dialogView.findViewById<EditText>(R.id.etName)
        val balanceInput = dialogView.findViewById<EditText>(R.id.etBalance)
        val currencyInput = dialogView.findViewById<EditText>(R.id.etCurrency)

        AlertDialog.Builder(this)
            .setTitle("Добавить счёт")
            .setView(dialogView)
            .setPositiveButton("Добавить") { _, _ ->
                val name = nameInput.text.toString()
                val balance = balanceInput.text.toString()
                val currency = currencyInput.text.toString()

                viewModel.addAccount(name, balance, currency)
            }
            .setNegativeButton("Отмена", null)
            .show()
    }



}