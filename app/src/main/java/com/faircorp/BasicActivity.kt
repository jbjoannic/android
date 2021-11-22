package com.faircorp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import com.faircorp.databinding.ActivityBasicBinding

const val WINDOW_LIST_CONFIG = "com.faircorp.windowconfig.attribute"
const val HEATER_LIST_CONFIG = "com.faircorp.heaterconfig.attribute"
const val ROOM_LIST_CONFIG = "com.faircorp.roomconfig.attribute"

open class BasicActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityBasicBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_windows -> startActivity(
                Intent(this, WindowsActivity::class.java).putExtra(WINDOW_LIST_CONFIG, -11)
            )
            R.id.menu_website -> startActivity(
                Intent(Intent.ACTION_VIEW, Uri.parse("https://dev-mind.fr"))
            )
            R.id.menu_email -> startActivity(
                Intent(Intent.ACTION_SENDTO, Uri.parse("mailto://jb.joannic.jbj@gmail.com"))
            )
            R.id.menu_heaters -> startActivity(
                Intent(this, HeatersActivity::class.java).putExtra(HEATER_LIST_CONFIG, -11)
            )
            R.id.menu_rooms -> startActivity(
                Intent(this, RoomsActivity::class.java).putExtra(ROOM_LIST_CONFIG, -11)
            )
            R.id.menu_buildings -> startActivity(
                Intent(this, BuildingsActivity::class.java)
            )

        }
        return super.onContextItemSelected(item)
    }

}