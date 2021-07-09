package com.pekyurek.emircan.presentation.core.extensions

import androidx.core.widget.addTextChangedListener
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout


@BindingAdapter("setLinearLayoutManager")
fun setLinearLayoutManager(recyclerView: RecyclerView, vertical: Boolean) {
    recyclerView.layoutManager = LinearLayoutManager(
        recyclerView.context,
        if (vertical) LinearLayoutManager.VERTICAL else LinearLayoutManager.HORIZONTAL,
        false
    )
}

@BindingAdapter("errorText")
fun errorText(textInputLayout: TextInputLayout, text: String?) {
    textInputLayout.error = text
    textInputLayout.editText?.addTextChangedListener { textInputLayout.error = null }
    textInputLayout.editText?.setOnFocusChangeListener { v, hasFocus ->
        if (hasFocus) textInputLayout.error = null
    }
}