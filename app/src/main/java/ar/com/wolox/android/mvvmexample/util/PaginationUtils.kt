package ar.com.wolox.android.mvvmexample.util

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * This class is used for pagination purpose, when the data is loaded from a service
 */
class PaginationUtils(
    recyclerView: RecyclerView,
    private val limit: Int,
    private val isLoading: () -> Boolean,
    private var loadMore : (Int) -> Unit,
    private val onLast: () -> Boolean = { true }
) : RecyclerView.OnScrollListener(){

    private var threshold = 10
    private var currentPage: Int = 0

    private var endWithAuto = false

    init {
        recyclerView.addOnScrollListener(this)
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val layoutManager = recyclerView.layoutManager
        layoutManager?.let {
            val visibleItemCount = it.childCount
            val totalItemCount = it.itemCount
            val firstVisibleItemPosition = when (layoutManager) {
                is LinearLayoutManager -> layoutManager.findLastVisibleItemPosition()
                is GridLayoutManager -> layoutManager.findLastVisibleItemPosition()
                else -> return
            }

            if (onLast() || isLoading()) return

            if (endWithAuto) {
                if (visibleItemCount + firstVisibleItemPosition == totalItemCount) return
            }

            if ((visibleItemCount + firstVisibleItemPosition + threshold) >= totalItemCount) {
                loadMore(++currentPage * limit)
            }
        }
    }

    fun restorePagination(){
        currentPage = 0
    }

}