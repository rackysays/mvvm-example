package ar.com.wolox.android.mvvmexample.ui.newdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import ar.com.wolox.android.mvvmexample.R
import ar.com.wolox.android.mvvmexample.databinding.FragmentNewDetailBinding
import ar.com.wolox.android.mvvmexample.model.Status
import ar.com.wolox.android.mvvmexample.ui.base.BaseFragment
import ar.com.wolox.android.mvvmexample.util.observeLiveData
import org.greenrobot.eventbus.EventBus
import javax.inject.Inject

class NewDetailFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: FragmentNewDetailBinding
    private lateinit var viewModel: NewDetailViewModel
    override fun layout(): Int = R.layout.fragment_new_detail

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, layout(),container,false)
        return binding.root
    }

    override fun observeLiveData() {
        super.observeLiveData()
        viewModel =  ViewModelProviders.of(this, viewModelFactory).get(NewDetailViewModel::class.java)
        viewModel.loadArguments(arguments)

        observeLiveData(viewModel.observeUserStored()){ binding.userId = it.toInt() }

        observeLiveData(viewModel.observeNew()){ binding.newDetail = it }

        observeLiveData(viewModel.observeLikeStatus()){
            when (it.status) {
                Status.LOADING -> binding.vNewDetailLikeIcon.isClickable = false
                Status.SUCCESS -> {
                    binding.vNewDetailLikeIcon.isClickable = true
                    EventBus.getDefault().post(it.data)
                }
                Status.ERROR -> binding.vNewDetailLikeIcon.isClickable = true
            }
        }

        observeLiveData(viewModel.observeRefreshStatus()){
            when (it.status) {
                Status.LOADING -> binding.vNewDetailSwipe.isRefreshing = true
                Status.SUCCESS -> binding.vNewDetailSwipe.isRefreshing = false
                Status.ERROR -> {
                    binding.vNewDetailSwipe.isRefreshing = false
                    Toast.makeText(requireContext(), ERROR_NETWORK, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun setListener() {
        super.setListener()
        binding.vNewDetailBackButton.setOnClickListener { requireActivity().onBackPressed() }
        binding.vNewDetailSwipe.setOnRefreshListener { viewModel.onRefreshNewDetail() }
        binding.vNewDetailLikeIcon.setOnClickListener { viewModel.onLikeClicked() }
    }

    override fun changeConnectionStatus(connectionOk: Boolean) {
        super.changeConnectionStatus(connectionOk)
        if (!connectionOk)
            Toast.makeText(requireContext(), ERROR_NETWORK, Toast.LENGTH_LONG).show()
    }

    companion object {
        const val ERROR_NETWORK = R.string.network_error_message
    }
}