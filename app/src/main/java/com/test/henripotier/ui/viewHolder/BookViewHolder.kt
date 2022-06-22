package com.test.henripotier.ui.viewHolder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.test.henripotier.data.model.Book
import com.test.henripotier.databinding.ViewItemBookBinding

class BookViewHolder(private val mBinding: ViewItemBookBinding) : RecyclerView.ViewHolder(mBinding.root) {
    fun bind(
        book: Book,
        onIconClicked: ((Book) -> Unit)?,
        iconRsc : Int?
    ): View {
        with(mBinding) {
            this.book = book
            if(iconRsc != null) {
                iconImg.setBackgroundResource(iconRsc)
                iconImg.setOnClickListener {
                    onIconClicked?.invoke(book) }
            } else {
                iconImg.visibility = View.GONE
            }
        }


        return mBinding.root
    }
}
