package ar.com.wolox.android.mvvmexample.ui.home.news

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ar.com.wolox.android.mvvmexample.R
import ar.com.wolox.android.mvvmexample.databinding.FragmentNewsBinding
import ar.com.wolox.android.mvvmexample.model.Status
import ar.com.wolox.android.mvvmexample.ui.base.BaseFragment
import ar.com.wolox.android.mvvmexample.util.NetworkSimpleBoundResource
import ar.com.wolox.android.mvvmexample.util.PaginationUtils
import ar.com.wolox.android.mvvmexample.util.observeLiveData
import kotlinx.android.synthetic.main.fragment_news.*
import javax.inject.Inject

class NewsFragment : BaseFragment(){

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewmodel: NewsViewModel

    private lateinit var binding: FragmentNewsBinding
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var recyclerPagination: PaginationUtils

    override fun layout(): Int = R.layout.fragment_news

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, layout(),container,false)
        return binding.root
    }

    override fun init() {
        super.init()
        val defaultColor = ContextCompat.getColor(requireActivity() as Context, DEFAULT_PROGRESS_COLOR)
        vNewsSwipeContainer.setColorSchemeColors(defaultColor, defaultColor, defaultColor)
        newsAdapter = NewsAdapter()
        binding.vNewsRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = newsAdapter
        }

        recyclerPagination = PaginationUtils(
            recyclerView = binding.vNewsRecycler,
            limit = LIMIT,
            isLoading = {viewmodel.observeNewsListStatusValue()?.status == Status.LOADING},
            loadMore = { viewmodel.nextPage(it) },
            onLast = { viewmodel.observeNewsListStatusValue()?.status == (Status.ERROR)  }
        )
    }

    override fun observeLiveData() {
        super.observeLiveData()
        viewmodel =  ViewModelProviders.of(this, viewModelFactory).get(NewsViewModel::class.java)

        observeLiveData(viewmodel.observeUserStored()){
            newsAdapter.setUserId(it)
        }

        observeLiveData(viewmodel.observeNewsList()){
            when (it.status) {
                Status.SUCCESS -> {
                    binding.vNewsNoContentImage.visibility = View.GONE
                    binding.vNewsSwipeContainer.isRefreshing = false
                    newsAdapter.submitList(it.data)
                    newsAdapter.notifyDataSetChanged()
                }
                Status.LOADING -> binding.vNewsSwipeContainer.isRefreshing = true
                Status.ERROR -> {
                    binding.vNewsSwipeContainer.isRefreshing = false
                    when(it.message) {
                        NetworkSimpleBoundResource.NULL_DATA -> {
                            if (newsAdapter.itemCount == 0){
                                binding.vNewsNoContentImage.visibility = View.VISIBLE
                            }
                        }
                        else -> Toast.makeText(requireContext(),ERROR_NETWORK,Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    override fun setListener() {
        super.setListener()
        binding.vNewsSwipeContainer.setOnRefreshListener {
            recyclerPagination.restorePagination()
            viewmodel.refreshData()
        }
    }

    companion object {
        private const val DEFAULT_PROGRESS_COLOR = R.color.colorAccent
        const val ERROR_NETWORK = R.string.network_error_message
        const val LIMIT = 10
    }
}