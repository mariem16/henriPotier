package com.test.henripotier.ui.viewmodel

import androidx.lifecycle.*
import com.test.henripotier.data.model.Book
import com.test.henripotier.data.model.CommercialOffers
import com.test.henripotier.repository.BookRepository
import com.test.henripotier.repository.CartRepository
import com.test.henripotier.ui.view.CartActivity
import com.test.henripotier.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val bookRepository: BookRepository,
    private var cartRepository: CartRepository,
    savedStateHandle: SavedStateHandle

) : ViewModel() {

    private var _bookList: MutableList<Book>? = null
    private var _cartBookLiveData = MutableLiveData<MutableList<Book>?>()
    var cartBookLiveData: LiveData<MutableList<Book>?> = _cartBookLiveData

    init {
        val isbns = savedStateHandle.get<Array<String>>(CartActivity.CART_DETAIL)?.toList()
        getBooksByIsbns(isbns)
    }

    private fun getBooksByIsbns(isbns: List<String>?) {

        isbns?.let {
            viewModelScope.launch(Dispatchers.IO) {
                _bookList = bookRepository.getBooksByIsbn(isbns)?.toMutableList()
                _cartBookLiveData.postValue(_bookList)
            }
        }
    }

    fun getOffers(isbns: List<String>?, offerResponse: (Response<String>) -> Unit) {
        offerResponse.invoke(Response.Loading)
        viewModelScope.launch {

            val response = withContext(Dispatchers.IO) {
                cartRepository.getOffers(isbns)
            }

            if (response is Response.Success) {
            val offer = calculateOffer(response.result)
            offerResponse.invoke(Response.Success(offer.toString()))
        } else if (response is Response.Error) {
            offerResponse.invoke(response)
        }
        }
    }

    fun calculateTotalPrice() : Int? {
        val prices = _cartBookLiveData.value?.map { it.price }
        return prices?.sum()

    }


    private fun calculateOffer(commercialOffer: CommercialOffers?): Int {

        val totalPrice = calculateTotalPrice()

        totalPrice?.let {
            val offers = mutableListOf<Int>()

            commercialOffer?.offers?.forEach { offer ->

                offers.add(
                    when (offer.type) {
                        "percentage" -> totalPrice * offer.value / 100
                        "minus" -> offer.value
                        "slice" -> offer.value * (totalPrice / offer.sliceValue)
                        else -> {
                            0
                        }
                    }.toInt()
                )
            }

            val bestOffer = offers.toList().maxOrNull() ?: 0
            return it - bestOffer
        }

        return 0
    }


    fun removeFromCart(book: Book) {
        _bookList?.remove(book)
        _cartBookLiveData.postValue(_bookList)
    }

}