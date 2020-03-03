package com.example.toolbarlib

import android.graphics.Color
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.example.toolbarlib.custom.MenuComponent
import com.example.toolbarlib.custom.TextComponent
import com.example.toolbarlib.custom.ToolbarEasy

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        initToolbar()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initToolbar() {

        toolbar.createToolbar {
            addComponent(TextComponent("Title"))
            addComponent(TextComponent("Title"))
            addComponent(TextComponent("Title"))
            addComponent(TextComponent("Title"))
            addComponent(TextComponent("Title"))
            addComponent(TextComponent("Title"))
            addComponent(TextComponent("Title"))
            addComponent(TextComponent("Title"))
            addComponent(TextComponent("Title"))
            addComponent(TextComponent("Title"))
            addComponent(TextComponent("Title"))
            addComponent(MenuComponent(arrayOf("item1", "item2", "item3"), R.drawable.ic_more_vert_black_24dp))
        }
//
//        toolbar.build()
//                .titleText("Title")
//                .titleTextColor(Color.RED)
//                .subTitleText("subtitle")
//                .subtitleTextColor(Color.YELLOW)
//                .homeButtonId(R.drawable.ic_account_circle_black_24dp)
//                .navigationIconColor(Color.WHITE)
//                .build()
    }
}
