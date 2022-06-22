package com.test.henripotier.repository.builder

import com.test.henripotier.data.model.Book
import com.test.henripotier.data.model.CommercialOffers
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Api service for retrofit
 * Interface that groups the different endpoints
 */
interface  ApiService {

    /**
     * Get a http response
     * GET : retrofit annotation
     * @return Response with body contains result with books if exist
     */
    @GET("books")
    suspend fun getBooks(): Response<List<Book>>


    @GET("books/{isbns}/commercialOffers")
    suspend fun getOffers(@Path("isbns") isbns: String?): Response<CommercialOffers>




}