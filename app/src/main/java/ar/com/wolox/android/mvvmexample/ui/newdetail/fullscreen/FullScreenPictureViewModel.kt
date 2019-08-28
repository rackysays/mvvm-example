package ar.com.wolox.android.mvvmexample.ui.newdetail.fullscreen

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import ar.com.wolox.android.mvvmexample.ui.base.BaseViewModel
import ar.com.wolox.android.mvvmexample.util.Extras.News.NEW_PICTURE
import javax.inject.Inject

class FullScreenPictureViewModel @Inject constructor(): BaseViewModel(){

    private val imageUrl = MutableLiveData<String>()

    fun loadArguments(arguments: Bundle?) {
        arguments?.let {
            imageUrl.postValue(it.getString(NEW_PICTURE))
        }
    }

    fun observeImageUrl() = imageUrl
}