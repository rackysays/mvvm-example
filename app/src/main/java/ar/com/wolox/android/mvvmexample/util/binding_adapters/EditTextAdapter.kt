package ar.com.wolox.android.mvvmexample.util.binding_adapters

import android.widget.TextView
import androidx.databinding.BindingAdapter
import ar.com.wolox.android.mvvmexample.R
import ar.com.wolox.android.mvvmexample.util.formatDateToTime
import com.facebook.drawee.view.SimpleDraweeView

@BindingAdapter("formatDate")
fun setDateToTime(view: TextView, shouldFormat: Boolean) {
    if (shouldFormat) {
        view.text = formatDateToTime(view.context,view.text.toString())
    }
}

@BindingAdapter("setFrescoImage")
fun setFrescoImage(view: SimpleDraweeView, uri: String){
    view.setImageURI((uri).replace(
        view.context.getString(R.string.reg_pattern_protocol).toRegex(),
        view.context.getString(R.string.allow_protocol)))
}