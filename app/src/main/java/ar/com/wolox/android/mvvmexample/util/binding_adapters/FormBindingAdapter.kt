package ar.com.wolox.android.mvvmexample.util.binding_adapters

import android.widget.EditText
import androidx.core.util.PatternsCompat
import androidx.databinding.BindingAdapter
import ar.com.wolox.android.mvvmexample.R
import ar.com.wolox.android.mvvmexample.model.ErrorMessage
import ar.com.wolox.android.mvvmexample.util.clearOnTextChangedListener
import ar.com.wolox.android.mvvmexample.util.onChangeFocus

/**
 * This file is used to custom attributes, for helping business logic
 */

//Validate the correct format of email address at the moment is change focus
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

//Set error message in the field
@BindingAdapter("errorText")
fun setErrorMessage(view: EditText, errorMessage: ErrorMessage?) {
    val message = when(errorMessage) {
        ErrorMessage.WRONG_FORMAT ->  view.resources.getString(R.string.login_wrong_username_format)
        ErrorMessage.EMPTY_FIELD ->  view.resources.getString(R.string.login_required_field)
        else -> null
    }
    view.error = message
}