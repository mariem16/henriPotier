package com.test.henripotier.ui.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.test.henripotier.R
import com.test.henripotier.data.model.Book
import com.test.henripotier.databinding.ActivityBookListBinding
import com.test.henripotier.ui.adapter.BookAdapter
import com.test.henripotier.ui.viewmodel.BookListViewModel
import com.test.henripotier.utils.Response
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BookListActivity : AppCompatActivity() {

    private val mViewModel: BookListViewModel by viewModels()
    private lateinit var mBinding: ActivityBookListBinding
    lateinit var mBookAdapter: BookAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        //init binding
        mBinding = ActivityBookListBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        initRecycleView()
        initData()
        initEvent()
    }

    private fun initEvent() {
        mBinding.goToCartBtn.setOnClickListener {
            if (mViewModel.mCart.isNullOrEmpty()) {
                showSnackBar("No Book found in the cart")
            } else {
                val intent = Intent(this, CartActivity::class.java)
                intent.putExtra(CartActivity.CART_DETAIL, mViewModel.mCart.toTypedArray())
                startActivity(intent)
                mViewModel.clearCart()

            }

        }
    }

    /**
     * Call this method to initialize the view and setup recycle view.
     */
    private fun initRecycleView() {
        mBookAdapter = BookAdapter(
            iconRsc = R.drawable.shopping_cart,
            onIconClicked = { book -> addToCartClicked(book) })

        mBinding.recyclerView.layoutManager = GridLayoutManager(this, 2)
        mBinding.recyclerView.adapter = mBookAdapter
    }

    private fun initData() {

        lifecycleScope.launch {
            mViewModel.mAllBooksFlow?.collectLatest { books ->
                mBookAdapter.submitData(books)
            }
        }


        mViewModel.booksFromRemoteLiveData.observe(this) { reponse ->
            when (reponse) {
                is Response.Success -> {
                    mBinding.progressBar.visibility = View.GONE
                    showSnackBar("book retrieved remotely successfully")
                }

                is Response.Error -> {
                    mBinding.progressBar.visibility = View.GONE
                    showSnackBar(reponse.message)
                }

                is Response.Loading -> {
                    mBinding.progressBar.visibility = View.VISIBLE
                    showSnackBar("retrieving Books is in progress")
                }
                else -> {
                    //Nothing to do
                }
            }

        }
    }


    private fun addToCartClicked(book: Book) {
        val isAddedToCart = mViewModel.addToCart(book.isbn)
        if (isAddedToCart)
            showSnackBar("${book.title} is added to cart")
        else {
            showSnackBar("${book.title} already added to cart")

        }
    }


    private fun showSnackBar(message: String) {
        Snackbar.make(mBinding.root, message, Snackbar.LENGTH_LONG).show()

    }


}