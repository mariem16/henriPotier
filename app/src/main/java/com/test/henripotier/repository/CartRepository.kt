package com.test.henripotier.repository

import com.test.henripotier.data.model.CommercialOffers
import com.test.henripotier.api.ApiService
import com.test.henripotier.utils.Response

class CartRepository(private val apiService: ApiService) {

    suspend fun getOffers(isbns : List<String>?) : Response<CommercialOffers> {
        val result = apiService.getOffers(isbns?.joinToString(","))
        return if(result.isSuccessful){
            val books = result.body() as CommercialOffers
             Response.Success(books)
        }else {
            Response.Error(result.errorBody().toString())
        }
    }

}