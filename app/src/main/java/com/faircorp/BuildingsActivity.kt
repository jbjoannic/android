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
import com.faircorp.model.BuildingAdapter
import com.faircorp.model.OnBuildingSelectedListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

const val BUILDING_NAME_PARAM = "com.faircorp.buildingname.attribute"

class BuildingsActivity : BasicActivity(), OnBuildingSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buildings)
        val recyclerView = findViewById<RecyclerView>(R.id.list_buildings) // (2)
        val adapter = BuildingAdapter(this) // (3)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter

        lifecycleScope.launch(context = Dispatchers.IO) { // (1)
            runCatching { ApiServices().buildingsApiService.findAll().execute() } // (2)
                .onSuccess {
                    withContext(context = Dispatchers.Main) { // (3)
                        adapter.update(it.body() ?: emptyList())
                    }
                }
                .onFailure {
                    withContext(context = Dispatchers.Main) { // (3)
                        Toast.makeText(
                            applicationContext,
                            "Error on buildings loading $it",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }
    }

    override fun onBuildingSelected(id: Long?) {
        val intent = Intent(this, BuildingActivity::class.java).putExtra(BUILDING_NAME_PARAM, id)
        startActivity(intent)
    }

    fun createBuildings(view: View) {
        val intent = Intent(this, BuildingsCreateActivity::class.java)
        startActivity(intent)
    }
}