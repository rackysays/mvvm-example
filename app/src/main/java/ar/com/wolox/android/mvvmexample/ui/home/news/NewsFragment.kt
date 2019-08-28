package ar.com.wolox.android.mvvmexample.ui.home.news

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ar.com.wolox.android.mvvmexample.R
import ar.com.wolox.android.mvvmexample.databinding.FragmentNewsBinding
import ar.com.wolox.android.mvvmexample.model.New
import ar.com.wolox.android.mvvmexample.model.Status
import ar.com.wolox.android.mvvmexample.ui.base.BaseFragment
import ar.com.wolox.android.mvvmexample.ui.newdetail.NewDetailActivity
import ar.com.wolox.android.mvvmexample.util.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class NewsFragment : BaseFragment<FragmentNewsBinding>(), NewsAdapter.NewListener{

    private lateinit var viewModel: NewsViewModel
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var recyclerPagination: PaginationUtils

    override fun layout(): Int = R.layout.fragment_news

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
    }

    override fun init() {
        super.init()
        newsAdapter = NewsAdapter(this)
        binding.vNewsRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = newsAdapter
        }

        recyclerPagination = PaginationUtils(
            recyclerView = binding.vNewsRecycler,
            limit = LIMIT,
            isLoading = {viewModel.observeNewsListStatusValue()?.status == Status.LOADING},
            loadMore = { viewModel.onNextPage(it) },
            onLast = { viewModel.observeNewsListStatusValue()?.status == (Status.ERROR)  }
        )
    }

    override fun observeLiveData() {
        super.observeLiveData()
        viewModel =  ViewModelProviders.of(this, viewModelFactory).get(NewsViewModel::class.java)

        observeLiveData(viewModel.observeUserStored()){
            newsAdapter.setUserId(it)
        }

        observeLiveData(viewModel.observeNewsListChanges()) {
            newsAdapter.submitList(it)
            newsAdapter.notifyDataSetChanged()
        }

        observeLiveData(viewModel.observeNewsList()){
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
            viewModel.onRefreshData()
        }
    }

    override fun onNewSelected(new: New) {
        val intent = Intent(requireActivity(),NewDetailActivity::class.java)
        intent.putExtra(Extras.News.NEW,new)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun getLike(eventLikes: New) {
        viewModel.onReceivedLikeEvent(eventLikes)
    }

    companion object {
        const val ERROR_NETWORK = R.string.network_error_message
        const val LIMIT = 10
    }
}