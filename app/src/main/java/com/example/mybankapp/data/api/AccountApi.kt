package com.cht.mybankapp.data.api

import com.cht.mybankapp.data.model.Account
import com.cht.mybankapp.data.model.PatchAccountStatusDTO
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

// Интерфейс для работы с API счетов
interface AccountApi {

    // Метод для получения списка счетов (HTTP GET запрос)
    @GET("accounts")
    fun getAccounts(): Call<List<Account>>

    // Метод для создания нового счета (HTTP POST запрос)
    @POST("accounts")
    fun createAccount(@Body account: Account): Call<Account>

    // Метод для удаления счета (HTTP POST запрос)
    @DELETE("accounts/{id}")
    fun deleteAccount(@Path ("id") id: String): Call<Unit>

    // Метод для полной замены счета (HTTP POST запрос)
    @PUT("accounts/{id}")
    fun updateAccountFully(
        @Path ("id") id: String,
        @Body account: Account
    ): Call<Account>

    // Метод для внесения частичного изменения счета (HTTP POST запрос)
    @PATCH("accounts/{id}")
    fun patchAccountStatus(
        @Path ("id") id: String,
        @Body patchAccountStatusDTO: PatchAccountStatusDTO
    ): Call<Account>

}