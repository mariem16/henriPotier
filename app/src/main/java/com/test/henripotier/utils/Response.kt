package com.test.henripotier.utils


/**
 * Response : this class defines data's states using sub classes
 * in order to manage the UI
 */
sealed class Response<out T> {
    /**
     * Success : This class will be called when the data has been received successfully
     * @param result received
     */
    data class Success<T> (val result : T? = null) : Response<T>()

    /**
     * Error : This class will be called when retrieving data failed
     * @param message error message
     */
    data class Error (val message : String) : Response<Nothing>()

    /**
     * Loading : This class will be called when retrieving data is in progress
     */
    object Loading : Response<Nothing>()
}