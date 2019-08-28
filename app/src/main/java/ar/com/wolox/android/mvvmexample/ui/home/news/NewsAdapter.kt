package ar.com.wolox.android.mvvmexample.ui.home.news

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ar.com.wolox.android.mvvmexample.databinding.ItemNewBinding
import ar.com.wolox.android.mvvmexample.model.New
import ar.com.wolox.android.mvvmexample.BR

class NewsAdapter(private val newListener: NewListener) : ListAdapter<New, NewsAdapter.NewsViewHolder>(NewsDiffCallback()) {

    private lateinit var userId: String

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return NewsViewHolder(ItemNewBinding.inflate(layoutInflater, parent, false), newListener)
    }

    fun setUserId(userId: String) {
        this.userId = userId
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bindTo(getItem(position), userId)
    }

    inner class NewsViewHolder(itemView: ItemNewBinding, private val newListener: NewListener) :
        RecyclerView.ViewHolder(itemView.root) {

        private val binding = itemView

        fun bindTo(new: New, userId: String) {
            binding.apply {
                setVariable(BR.userId, userId.toInt())
                setVariable(BR.newB, new)
                executePendingBindings()
            }
            itemView.setOnClickListener { newListener.onNewSelected(new) }
        }
    }

    interface NewListener {
        fun onNewSelected(new: New)
    }
}