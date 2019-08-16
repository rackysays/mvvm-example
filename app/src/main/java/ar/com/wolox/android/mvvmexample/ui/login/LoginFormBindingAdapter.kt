package ar.com.wolox.android.mvvmexample.ui.login

import android.widget.EditText
import androidx.core.util.PatternsCompat
import androidx.databinding.BindingAdapter
import ar.com.wolox.android.mvvmexample.R
import ar.com.wolox.android.mvvmexample.util.clearOnTextChangedListener
import ar.com.wolox.android.mvvmexample.util.onChangeFocus

@BindingAdapter("emailValidation")
fun emailValidation(view: EditText, emailValidationEnable: Boolean) {

    if (!emailValidationEnable) {
        view.clearOnTextChangedListener()
        return
    }

    view.onChangeFocus {
        if (!it){
            if (view.text.isNotEmpty() && !PatternsCompat.EMAIL_ADDRESS.matcher(view.text).matches()) {
                view.error = view.resources.getString(R.string.login_wrong_username_format)
            } else {
                view.error = null
            }
        }
    }
}