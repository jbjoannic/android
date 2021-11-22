package com.faircorp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.faircorp.model.ApiServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


const val BUILDING_NAME_PARAM2 = "com.faircorp.buildingname.attribute"
const val ROOM_LIST_CONFIG3 = "com.faircorp.roomconfig.attribute"

class BuildingActivity : BasicActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_building)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val id = intent.getLongExtra(BUILDING_NAME_PARAM2,0)
        val listArg: MutableList<String?> = mutableListOf()
        lifecycleScope.launch(Dispatchers.Default) { // (1)
            runCatching { ApiServices().buildingsApiService.findById(id).execute() } // (2)
                .onSuccess {
                    val iname:String? = it.body()?.name
                    val iOutsideTemperature:String? = it.body()?.outsideTemperature.toString()
                    listArg.add(iname)
                    listArg.add(iOutsideTemperature)
                    withContext(context = Dispatchers.Main) { // (3)
                        Toast.makeText(
                            applicationContext,
                            "Success $iname, $iOutsideTemperature",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                .onFailure {
                    withContext(context = Dispatchers.Main) { // (3)
                        Toast.makeText(
                            applicationContext,
                            "id $id Error on buildings loading $it",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            withContext(context = Dispatchers.Main) {
                findViewById<TextView>(R.id.txt_building_name).text = listArg.get(0)
                findViewById<TextView>(R.id.txt_building_outside_temp).text = listArg.get(1)
            }
        }
    }

    fun deleteBuilding(view: View){
        val id = intent.getLongExtra(BUILDING_NAME_PARAM2,0)
        lifecycleScope.launch(Dispatchers.Default) { // (1)
            runCatching { ApiServices().buildingsApiService.delete(id).execute() } // (2)
                .onSuccess {
                    withContext(context = Dispatchers.Main) { // (3)
                        Toast.makeText(
                            applicationContext,
                            "Success Delete",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                .onFailure {
                    withContext(context = Dispatchers.Main) { // (3)
                        Toast.makeText(
                            applicationContext,
                            "Failed Delete",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }
    }

    fun SelectRoomByBuilding(view: View){
        val id = intent.getLongExtra(BUILDING_NAME_PARAM2,0)
        val intent = Intent(this, RoomsActivity::class.java).putExtra(ROOM_LIST_CONFIG3, id)
        startActivity(intent)
    }




}