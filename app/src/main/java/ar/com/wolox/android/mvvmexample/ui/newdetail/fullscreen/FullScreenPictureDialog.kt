package ar.com.wolox.android.mvvmexample.ui.newdetail.fullscreen

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import ar.com.wolox.android.mvvmexample.databinding.DialogFullScreenPictureBinding
import ar.com.wolox.android.mvvmexample.ui.base.BaseDialogFragment
import ar.com.wolox.android.mvvmexample.util.observeLiveData

class FullScreenPictureDialog : BaseDialogFragment<DialogFullScreenPictureBinding>(){

    private lateinit var viewModel: FullScreenPictureViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeLiveData()
    }

    private fun observeLiveData() {
        viewModel =  ViewModelProviders.of(this, viewModelFactory).get(FullScreenPictureViewModel::class.java)
        viewModel.loadArguments(arguments)

        observeLiveData(viewModel.observeImageUrl()){
            binding.imageUrl = it
        }
    }
}