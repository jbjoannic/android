package com.faircorp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.faircorp.model.ApiServices
import com.faircorp.model.WindowApiService
import com.faircorp.model.WindowDto
import com.faircorp.model.WindowService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.withContext


const val WINDOW_NAME_PARAM2 = "com.faircorp.windowname.attribute"

class WindowActivity : BasicActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_window)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val id = intent.getLongExtra(WINDOW_NAME_PARAM2,0)
        val listArg: MutableList<String?> = mutableListOf()

        lifecycleScope.launch(Dispatchers.Default) { // (1)
            runCatching { ApiServices().windowsApiService.findById(id).execute() } // (2)
                .onSuccess {
                    val iname:String? = it.body()?.name
                    val iRoomName:String? = it.body()?.room?.name
                    val iCurrentTemperature:String? = it.body()?.room?.currentTemperature?.toString()
                    val iTargetTemperature: String? = it.body()?.room?.targetTemperature?.toString()
                    val iStatus: String = it.body()?.status.toString()
                    listArg.add(iname)
                    listArg.add(iRoomName)
                    listArg.add(iTargetTemperature)
                    listArg.add(iCurrentTemperature)
                    listArg.add(iStatus)
                    withContext(context = Dispatchers.Main) { // (3)
                        Toast.makeText(
                            applicationContext,
                            "Success $iname, $iRoomName, $iCurrentTemperature, $iTargetTemperature, $iStatus",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                .onFailure {
                    withContext(context = Dispatchers.Main) { // (3)
                        Toast.makeText(
                            applicationContext,
                            "id $id Error on windows loading $it",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            withContext(context = Dispatchers.Main) {
                findViewById<TextView>(R.id.text_name_window2).text = listArg.get(0)
                findViewById<TextView>(R.id.txt_room_name).text = listArg.get(1)
                findViewById<TextView>(R.id.txt_window_target_temperature).text = listArg.get(2)
                findViewById<TextView>(R.id.txt_window_current_temperature).text= listArg.get(3)
                findViewById<TextView>(R.id.txt_window_status).text = listArg.get(4)
            }
        }
    }




}