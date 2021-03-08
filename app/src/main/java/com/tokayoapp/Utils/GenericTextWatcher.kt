package com.tokayoapp.Utils

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import com.tokayoapp.R

class GenericTextWatcher internal constructor(val editText: Array<EditText>, var view: View) :
        TextWatcher {
    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    override fun afterTextChanged(editable: Editable) {
        // TODO Auto-generated method stub
        val text = editable.toString()
        when (view.id) {
            R.id.editOne -> if (text.length == 1) editText[1].requestFocus()
            R.id.editTwo -> if (text.length == 1) editText[2].requestFocus() else if (text.isEmpty()) editText[0].requestFocus()
            R.id.editThree -> if (text.length == 1) editText[3].requestFocus() else if (text.isEmpty()) editText[1].requestFocus()
            R.id.editFour -> if (text.isEmpty()) editText[2].requestFocus()
        }
    }
}