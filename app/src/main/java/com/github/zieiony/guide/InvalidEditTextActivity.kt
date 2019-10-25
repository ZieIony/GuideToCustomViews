package com.github.zieiony.guide

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import kotlinx.android.synthetic.main.activity_invalidedittext.*

@ActivityAnnotation(layout = R.layout.activity_invalidedittext, title = "InvalidEditText")
class InvalidEditTextActivity : SampleActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        invalidEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                invalidEditText.valid = s.length >= 6
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }
}
