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


const val ROOM_NAME_PARAM2 = "com.faircorp.roomname.attribute"
const val WINDOW_LIST_CONFIG3 = "com.faircorp.windowconfig.attribute"
const val HEATER_LIST_CONFIG3 = "com.faircorp.heaterconfig.attribute"

class RoomActivity : BasicActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val id = intent.getLongExtra(ROOM_NAME_PARAM2, 0)
        val listArg: MutableList<String?> = mutableListOf()
        lifecycleScope.launch(Dispatchers.Default) { // (1)
            runCatching { ApiServices().roomsApiService.findById(id).execute() } // (2)
                .onSuccess {
                    val iname: String? = it.body()?.name
                    val iCurrentTemperature: String? = it.body()?.currentTemperature.toString()
                    val iTargetTemperature: String? = it.body()?.targetTemperature.toString()
                    val iFloor: String? = it.body()?.floor.toString()
                    val iBuildingName: String? = it.body()?.buildingName
                    listArg.add(iname)
                    listArg.add(iCurrentTemperature)
                    listArg.add(iTargetTemperature)
                    listArg.add(iFloor)
                    listArg.add(iBuildingName)
                    withContext(context = Dispatchers.Main) { // (3)
                        Toast.makeText(
                            applicationContext,
                            "Success $iname, $iCurrentTemperature, $iTargetTemperature, $iFloor, $iBuildingName",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                .onFailure {
                    withContext(context = Dispatchers.Main) { // (3)
                        Toast.makeText(
                            applicationContext,
                            "id $id Error on rooms loading $it",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            withContext(context = Dispatchers.Main) {
                findViewById<TextView>(R.id.txt_room_name).text = listArg.get(0)
                findViewById<TextView>(R.id.txt_room_current_temp).text = listArg.get(1)
                findViewById<TextView>(R.id.txt_room_target_temp).text = listArg.get(2)
                findViewById<TextView>(R.id.txt_room_floor).text = listArg.get(3)
                findViewById<TextView>(R.id.txt_room_building_name).text = listArg.get(4)
            }
        }
    }

    fun SelectByRoom(view: View) {
        val id = intent.getLongExtra(ROOM_NAME_PARAM2, 0)
        val intent = Intent(this, WindowsActivity::class.java).putExtra(WINDOW_LIST_CONFIG3, id)
        startActivity(intent)
    }

    fun SelectHeatersByRoom(view: View) {
        val id = intent.getLongExtra(ROOM_NAME_PARAM2, 0)
        val intent = Intent(this, HeatersActivity::class.java).putExtra(HEATER_LIST_CONFIG3, id)
        startActivity(intent)
    }

    fun deleteRoom(view: View) {
        val id = intent.getLongExtra(ROOM_NAME_PARAM2, 0)
        lifecycleScope.launch(Dispatchers.Default) { // (1)
            runCatching { ApiServices().roomsApiService.delete(id).execute() } // (2)
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


}