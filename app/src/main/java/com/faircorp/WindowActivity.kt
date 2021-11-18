package com.faircorp

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.faircorp.model.ApiServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
                    val iRoomName:String? = it.body()?.roomName
                    val iStatus: String = it.body()?.windowStatus.toString()
                    listArg.add(iname)
                    listArg.add(iRoomName)
                    listArg.add(iStatus)
                    withContext(context = Dispatchers.Main) { // (3)
                        Toast.makeText(
                            applicationContext,
                            "Success $iname, $iRoomName, $iStatus",
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
                findViewById<TextView>(R.id.txt_window_room_name).text = listArg.get(1)
                findViewById<TextView>(R.id.txt_window_status).text = listArg.get(2)
            }
        }
    }




}