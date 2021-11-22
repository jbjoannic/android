package com.faircorp
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.lifecycle.lifecycleScope
import com.faircorp.databinding.ActivityRoomsCreateBinding
import com.faircorp.model.ApiServices
import com.faircorp.model.RoomDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
class RoomsCreateActivity : BasicActivity() {
    var realId : Long? = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rooms_create)

        val binding = ActivityRoomsCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val ids = arrayListOf<Long?>()
        val names = arrayListOf<String?>()

        lifecycleScope.launch(context = Dispatchers.IO) { // (1)
            runCatching { ApiServices().buildingsApiService.findAll().execute() } // (2)
                .onSuccess {
                    withContext(context = Dispatchers.Main) { // (3)
                        for (i in 0..(it.body()?.size!!-1)) {
                            val provId:Long? = it.body()?.get(i)?.id
                            val provName:String? = it.body()?.get(i)?.name
                            ids.add(provId)
                            names.add(provName)
                        }
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
            withContext(context = Dispatchers.Main) {
                val arrayAdapterSpinner=ArrayAdapter(applicationContext,R.layout.activity_rooms_create_item, names)
                binding.spinnerRoomsBuildings.adapter=arrayAdapterSpinner

                binding.spinnerRoomsBuildings.onItemSelectedListener=object:AdapterView.OnItemSelectedListener{
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        realId = ids.get(id.toInt())
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }

                }
            }
        }
    }

    fun createRoom(view: View){
        val roomName = findViewById<EditText>(R.id.edit_room_name).text.toString()
        val roomCurrentTemp = findViewById<EditText>(R.id.edit_room_current_temp).text.toString().toDouble()
        val roomTargetTemp = findViewById<EditText>(R.id.edit_room_target_temp).text.toString().toDouble()
        val roomFloor = findViewById<EditText>(R.id.edit_room_floor).text.toString().toLong()
        val roomBuildingId = realId
        val roomDto = RoomDto(null, roomName, roomCurrentTemp, roomTargetTemp, roomFloor, null, roomBuildingId)
        lifecycleScope.launch(context = Dispatchers.IO) { // (1)
            runCatching { ApiServices().roomsApiService.create(roomDto).execute() } // (2)
                .onSuccess {
                    withContext(context = Dispatchers.Main) { // (3)
                        Toast.makeText(
                            applicationContext,
                            "Success Create",
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