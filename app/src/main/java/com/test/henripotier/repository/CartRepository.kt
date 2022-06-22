package com.test.henripotier.repository

import com.test.henripotier.api.ApiService
import com.test.henripotier.data.model.CommercialOffers
import com.test.henripotier.utils.Response
import java.net.UnknownHostException

class CartRepository(private val apiService: ApiService) {

    suspend fun getOffers(isbns: List<String>?): Response<CommercialOffers> {
        return try {
            val result = apiService.getOffers(isbns?.joinToString(","))
            if (result.isSuccessful) {
                val books = result.body() as CommercialOffers
                Response.Success(books)
            } else {
                Response.Error(result.errorBody().toString())
            }
        } catch (exception : UnknownHostException) {
            Response.Error("No internet connection!")
        } catch (exception: Exception) {
            Response.Error("error occurs when trying to fetch data!")
        }

    }

}