package com.faircorp

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.faircorp.model.ApiServices
import com.faircorp.model.BuildingDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BuildingsCreateActivity : BasicActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buildings_create)

    }

    fun createBuilding(view: View) {
        val buildingName = findViewById<EditText>(R.id.edit_building_name).text.toString()
        val buildingOutsideTemperature =
            findViewById<EditText>(R.id.edit_building_outside_temp).text.toString().toDoubleOrNull()
        val buildingDto = BuildingDto(null, buildingName, buildingOutsideTemperature)
        lifecycleScope.launch(context = Dispatchers.IO) { // (1)
            runCatching { ApiServices().buildingsApiService.create(buildingDto).execute() } // (2)
                .onSuccess {
                    withContext(context = Dispatchers.Main) { // (3)
                        Toast.makeText(
                            applicationContext,
                            "Success Create $buildingName",
                            Toast.LENGTH_LONG
                        ).show()

                    }
                }
                .onFailure {
                    withContext(context = Dispatchers.Main) { // (3)
                        Toast.makeText(
                            applicationContext,
                            "Error Create ${it.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }
    }
}