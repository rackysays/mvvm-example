package ar.com.wolox.android.mvvmexample.ui.newdetail.fullscreen

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ar.com.wolox.android.mvvmexample.util.Extras.News.NEW_PICTURE
import javax.inject.Inject

class FullScreenPictureViewModel @Inject constructor(): ViewModel(){

    private val imageUrl = MutableLiveData<String>()

    fun loadArguments(arguments: Bundle?) {
        arguments?.let {
            imageUrl.postValue(it.getString(NEW_PICTURE))
        }
    }

    fun observeImageUrl() = imageUrl
}