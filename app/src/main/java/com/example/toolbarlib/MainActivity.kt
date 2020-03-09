package com.example.toolbarlib

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import com.example.toolbarlib.custom.component.ImageComponent
import com.example.toolbarlib.custom.component.MenuComponent
import com.example.toolbarlib.custom.component.PopupComponent
import com.example.toolbarlib.custom.component.TextComponent
import com.example.toolbarlib.custom.property.GravityPosition
import com.example.toolbarlib.custom.property.Margin
import com.example.toolbarlib.custom.property.consts.MarginSet

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
            addComponent(ImageComponent(R.drawable.ic_account_circle_black_24dp){}, GravityPosition.RIGHT)
            addComponent(ImageComponent(R.mipmap.ic_launcher){}, GravityPosition.LEFT)
            addComponent(
                TextComponent("Title"),
                Margin().apply {
                    marginEnd = MarginSet.EXTRA_BIG
                    marginStart = MarginSet.EXTRA_BIG
                })

            addComponent(
                MenuComponent(
                    arrayOf("Some item1", "Some item2", "Some item3"),
                    R.drawable.ic_more_vert_black_24dp
                ) { menuItem -> onMenuItemClick(menuItem) }, GravityPosition.RIGHT
            )
            addComponent(
                PopupComponent(
                    contentView = SearchView(this@MainActivity),
                    iconRes = R.drawable.ic_more_vert_black_24dp
                ), GravityPosition.RIGHT
            )
        }

    }

    private fun onMenuItemClick(item: MenuItem) {
        Snackbar.make(toolbar, "onMenuItemClick " + item.title, Snackbar.LENGTH_LONG).show()
    }
}
