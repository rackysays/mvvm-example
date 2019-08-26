package ar.com.wolox.android.mvvmexample.ui.home.news

import androidx.recyclerview.widget.DiffUtil
import ar.com.wolox.android.mvvmexample.model.New

class NewsDiffCallback : DiffUtil.ItemCallback<New>() {

    override fun areItemsTheSame(oldItem: New, newItem: New): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: New, newItem: New): Boolean {
        return oldItem == newItem
    }
}