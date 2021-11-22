package com.faircorp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.faircorp.model.ApiServices
import com.faircorp.model.HeaterAdapter
import com.faircorp.model.OnHeaterSelectedListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

const val HEATER_NAME_PARAM = "com.faircorp.heatername.attribute"
const val HEATER_LIST_CONFIG2 = "com.faircorp.heaterconfig.attribute"

class HeatersActivity : BasicActivity(), OnHeaterSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_heaters)
        val recyclerView = findViewById<RecyclerView>(R.id.list_heaters) // (2)
        val adapter = HeaterAdapter(this) // (3)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter

        val config = intent.getLongExtra(HEATER_LIST_CONFIG2, -11)
        val compar: Long = -11

        if (config.equals(compar)) {
            lifecycleScope.launch(context = Dispatchers.IO) { // (1)
                runCatching { ApiServices().heatersApiService.findAll().execute() } // (2)
                    .onSuccess {
                        withContext(context = Dispatchers.Main) { // (3)
                            adapter.update(it.body() ?: emptyList())
                        }
                    }
                    .onFailure {
                        withContext(context = Dispatchers.Main) { // (3)
                            Toast.makeText(
                                applicationContext,
                                "Error on heaters loading $it",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
            }
        } else {
            lifecycleScope.launch(context = Dispatchers.IO) { // (1)
                runCatching {
                    ApiServices().roomsApiService.findHeatersByRoom(config).execute()
                } // (2)
                    .onSuccess {
                        withContext(context = Dispatchers.Main) { // (3)
                            adapter.update(it.body() ?: emptyList())
                        }
                    }
                    .onFailure {
                        withContext(context = Dispatchers.Main) { // (3)
                            Toast.makeText(
                                applicationContext,
                                "Error on heaters loading $it",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
            }
        }
    }

    override fun onHeaterSelected(id: Long?) {
        val intent = Intent(this, HeaterActivity::class.java).putExtra(HEATER_NAME_PARAM, id)
        startActivity(intent)
    }

    fun createHeaters(view: View) {
        val intent = Intent(this, HeatersCreateActivity::class.java)
        startActivity(intent)
    }
}