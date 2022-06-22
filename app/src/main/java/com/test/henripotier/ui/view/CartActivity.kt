package com.test.henripotier.ui.view

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.test.henripotier.R
import com.test.henripotier.data.model.Book
import com.test.henripotier.databinding.ActivityCartBinding
import com.test.henripotier.ui.adapter.CartBookAdapter
import com.test.henripotier.ui.viewmodel.CartViewModel
import com.test.henripotier.utils.Response
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartActivity : AppCompatActivity() {

    private val mViewModel: CartViewModel by viewModels()
    private lateinit var mBinding: ActivityCartBinding
    lateinit var mCartBookAdapter: CartBookAdapter

    companion object {
        const val CART_DETAIL = "cart_detail"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //init binding
        mBinding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        initRecycleView()
        initObservers()
    }


    /**
     * Call this method to initialize the view and setup recycle view.
     */
    private fun initRecycleView() {
        mCartBookAdapter = CartBookAdapter(
            iconRsc = R.drawable.delete_cart,
            onIconClicked = { book -> removeFromCart(book) })

        mBinding.recyclerView.layoutManager = LinearLayoutManager(this)
        mBinding.recyclerView.adapter = mCartBookAdapter
    }


    private fun initObservers() {
        mViewModel.cartBookLiveData.observe(this) { books ->

            mCartBookAdapter.submitList(books?.toMutableList())
            if (books.isNullOrEmpty()) {
                showSnackBar("Add new books in the cart")
                mBinding.calculateOfferTextview.visibility = View.GONE
            } else {
                //setTotalPrice
                setTotalPrice()
                //calculate offer
                mViewModel.getOffers(books.map { it.isbn }) {
                    updateOffer(it)
                }

            }
        }
    }

    private fun setTotalPrice() {
        mBinding.calculatedPriceTextview.text =
            getString(R.string.price, mViewModel.calculateTotalPrice()?.toString() ?: "")

    }

    private fun updateOffer(offerResponse: Response<String>) {
        when (offerResponse) {
            is Response.Success -> {
                mBinding.progressBar.visibility = View.GONE
                mBinding.calculateOfferTextview.visibility = View.VISIBLE
                mBinding.calculateOfferTextview.text = getString(R.string.price, offerResponse.result)
            }
            is Response.Error -> {
                mBinding.progressBar.visibility = View.GONE
                mBinding.calculateOfferTextview.visibility = View.GONE
                showSnackBar(offerResponse.message)
            }
            is Response.Loading -> {
                mBinding.progressBar.visibility = View.VISIBLE
                mBinding.calculateOfferTextview.visibility = View.GONE
            }
        }
    }


    private fun removeFromCart(book: Book) {
        mViewModel.removeFromCart(book)
    }


    private fun showSnackBar(message: String) {
        Snackbar.make(mBinding.root, message, Snackbar.LENGTH_LONG).show()

    }


}