package com.example.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.toolbarlib.custom.component.ImageComponent
import com.example.toolbarlib.custom.component.TextComponent
import com.example.toolbarlib.custom.property.GravityPosition
import com.example.toolbarlib.custom.property.Margin
import com.example.toolbarlib.custom.property.consts.MarginSet
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        initToolbar()
    }

    private fun initToolbar() {

        toolbar.createToolbar {
            addComponent(
                ImageComponent(com.example.toolbarlib.R.drawable.ic_account_circle_black_24dp),
                GravityPosition.RIGHT
            )
            addComponent(
                ImageComponent(com.example.toolbarlib.R.drawable.ic_account_circle_black_24dp),
                GravityPosition.RIGHT
            )
            addComponent(
                ImageComponent(com.example.toolbarlib.R.drawable.ic_account_circle_black_24dp),
                GravityPosition.RIGHT
            )
            addComponent(
                TextComponent("asdasdasd"),
                GravityPosition.CENTER
            )
            addComponent(
                TextComponent("asdasdasd")
            )
            addComponent(ImageComponent(com.example.toolbarlib.R.drawable.ic_account_circle_black_24dp), Margin().apply {
                marginBottom = MarginSet.BIG
                marginTop = MarginSet.BIG
            })
            addComponent(ImageComponent(com.example.toolbarlib.R.drawable.ic_account_circle_black_24dp))
            addComponent(ImageComponent(com.example.toolbarlib.R.drawable.ic_account_circle_black_24dp))
            addComponent(ImageComponent(com.example.toolbarlib.R.drawable.ic_account_circle_black_24dp))
        }

        toolbar.setHomeButtonEnabled(true)
        toolbar.setOnHomeButtonClick {
            Snackbar.make(toolbar, "OnHomeButtonClick", Snackbar.LENGTH_LONG).show()

        }

    }
}
