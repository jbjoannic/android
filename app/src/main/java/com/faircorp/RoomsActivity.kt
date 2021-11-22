package com.faircorp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.faircorp.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
const val ROOM_NAME_PARAM = "com.faircorp.roomname.attribute"
const val ROOM_LIST_CONFIG2 = "com.faircorp.roomconfig.attribute"
class RoomsActivity : BasicActivity(), OnRoomSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rooms)
        val recyclerView = findViewById<RecyclerView>(R.id.list_rooms) // (2)
        val adapter = RoomAdapter(this) // (3)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter

        val config = intent.getLongExtra(ROOM_LIST_CONFIG2,-11)
        val compar : Long = -11

        if (config.equals(compar)) {
            lifecycleScope.launch(context = Dispatchers.IO) { // (1)
                runCatching { ApiServices().roomsApiService.findAll().execute() } // (2)
                    .onSuccess {
                        withContext(context = Dispatchers.Main) { // (3)
                            adapter.update(it.body() ?: emptyList())
                        }
                    }
                    .onFailure {
                        withContext(context = Dispatchers.Main) { // (3)
                            Toast.makeText(
                                applicationContext,
                                "Error on rooms loading $it",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
            }
        } else {
            lifecycleScope.launch(context = Dispatchers.IO) { // (1)
                runCatching { ApiServices().buildingsApiService.findRoomsByBuilding(config).execute() } // (2)
                    .onSuccess {
                        withContext(context = Dispatchers.Main) { // (3)
                            adapter.update(it.body() ?: emptyList())
                        }
                    }
                    .onFailure {
                        withContext(context = Dispatchers.Main) { // (3)
                            Toast.makeText(
                                applicationContext,
                                "Error on rooms loading $it",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
            }
        }
    }

    override fun onRoomSelected(id: Long?) {
        val intent = Intent(this, RoomActivity::class.java).putExtra(ROOM_NAME_PARAM, id)
        startActivity(intent)
    }

    fun createRooms(view: View){
        val intent = Intent(this, RoomsCreateActivity::class.java)
        startActivity(intent)
    }
}