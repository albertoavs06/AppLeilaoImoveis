package br.edu.ifrn.imoveis.extensions

import android.widget.TextView

var TextView.string: String
    get() = text.toString()
    set(value) {
        text = value
    }

fun TextView.isEmpty() = text.trim().isEmpty()