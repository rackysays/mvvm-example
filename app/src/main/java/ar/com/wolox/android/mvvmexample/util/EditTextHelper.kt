package ar.com.wolox.android.mvvmexample.util

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

fun EditText.afterTextChanged(action: (CharSequence) -> Unit) {
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(string: Editable?) = Unit
        override fun beforeTextChanged(string: CharSequence?, start: Int, count: Int, after: Int) = Unit
        override fun onTextChanged(string: CharSequence?, start: Int, before: Int, count: Int) {
            action(string ?: "")
        }

    })
}

fun EditText.onChangeFocus(action: (Boolean) -> Unit){
    setOnFocusChangeListener { _, b -> action(b)  }
}

fun EditText.clearOnTextChangedListener() {
    afterTextChanged {}
    onChangeFocus {}
}