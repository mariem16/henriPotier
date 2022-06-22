package com.test.henripotier.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.test.henripotier.R
import com.test.henripotier.data.model.Book
import com.test.henripotier.ui.viewHolder.BookViewHolder

class BookAdapter constructor(
    private val onIconClicked: ((Book) -> Unit)? = null,
    private val iconRsc : Int?
    ) : PagingDataAdapter<Book, BookViewHolder>(BOOK_COMPARATOR) {

    companion object {
        private val BOOK_COMPARATOR =
            object : DiffUtil.ItemCallback<Book>() {
                override fun areItemsTheSame(
                    oldItem: Book,
                    newItem: Book
                ): Boolean {
                    return oldItem.isbn == newItem.isbn
                }

                override fun areContentsTheSame(
                    oldItem: Book,
                    newItem: Book
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder =
        BookViewHolder(
            DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.view_item_book,
            parent,
            false
        ))

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = getItem(position)
        book?.let {
            holder.bind(it, onIconClicked, iconRsc)
         }
    }

}