
package com.example.milkchequedemo.data.service

import com.example.milkchequedemo.data.dto.AllOrdersResponse
import com.example.milkchequedemo.data.dto.CustomerOrderRequestDto
import com.example.milkchequedemo.data.dto.CustomerOrderResponseDto
import com.example.milkchequedemo.data.dto.MenuItemDto
import com.example.milkchequedemo.data.dto.PaymentRequestDto
import com.example.milkchequedemo.data.dto.SessionRequestDto
import com.example.milkchequedemo.data.dto.SessionResponseDto
import com.example.milkchequedemo.data.dto.StoreInfoResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface StoreApi {
    @GET("store/info")
    suspend fun getStoreInfo(
        @Query("storeId") storeId: Int,
        @Query("tableId") tableId: Int
    ): Response<StoreInfoResponseDto>

    // GET https://.../store/menu

    @GET("store/menu")
    suspend fun getMenu(
        @Query("storeId") storeId: Int,
        @Query("tableId") tableId: Int
    ): Response<List<MenuItemDto>>

    @POST("session/add")
    suspend fun initSession(
        @Body sessionRequestDto: SessionRequestDto
    ): Response<SessionResponseDto>


    @POST("session/order")
    suspend fun sessionOrder(
        @Body customerOrderRequestDto: CustomerOrderRequestDto
    ): Response<CustomerOrderResponseDto>

    @GET("session/allOrders")
    suspend fun getAllOrders(
        @Query("sessionId") storeId: String
    ): Response<List<AllOrdersResponse>>

    @POST("payments/pay")
    suspend fun pay(
        @Body body: PaymentRequestDto
    ): Response<String>
}
