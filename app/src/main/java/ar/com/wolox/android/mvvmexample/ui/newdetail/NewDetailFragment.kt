package ar.com.wolox.android.mvvmexample.ui.newdetail

import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import ar.com.wolox.android.mvvmexample.R
import ar.com.wolox.android.mvvmexample.databinding.FragmentNewDetailBinding
import ar.com.wolox.android.mvvmexample.model.Status
import ar.com.wolox.android.mvvmexample.ui.base.BaseFragment
import ar.com.wolox.android.mvvmexample.ui.newdetail.fullscreen.FullScreenPictureDialog
import ar.com.wolox.android.mvvmexample.util.Extras.News.NEW_PICTURE
import ar.com.wolox.android.mvvmexample.util.Extras.News.TAG_FULLSCREEN_PICTURE
import ar.com.wolox.android.mvvmexample.util.observeLiveData
import org.greenrobot.eventbus.EventBus

class NewDetailFragment : BaseFragment<FragmentNewDetailBinding>() {

    private lateinit var viewModel: NewDetailViewModel

    override fun layout(): Int = R.layout.fragment_new_detail

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
        binding.vNewDetailPicture.setOnClickListener{
            val dialog = FullScreenPictureDialog()
            val bundle = Bundle()
            bundle.putString(NEW_PICTURE, viewModel.getImageUrl())
            dialog.arguments = bundle
            dialog.show(fragmentManager!!.beginTransaction(), TAG_FULLSCREEN_PICTURE)
        }
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