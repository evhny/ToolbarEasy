package com.example.toolbarlib.custom

import android.content.Context
import android.view.View

interface Component {

    fun getView(context: Context): View
}