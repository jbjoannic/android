package com.faircorp
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.lifecycle.lifecycleScope
import com.faircorp.databinding.ActivityHeatersCreateBinding
import com.faircorp.model.ApiServices
import com.faircorp.model.HeaterDto
import com.faircorp.model.HeaterStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
class HeatersCreateActivity : BasicActivity() {

    var realId : Long? = 0
    var status : HeaterStatus = HeaterStatus.OFF
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_heaters_create)

        val binding = ActivityHeatersCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val ids = arrayListOf<Long?>()
        val names = arrayListOf<String?>()

        lifecycleScope.launch(context = Dispatchers.IO) { // (1)
            runCatching { ApiServices().roomsApiService.findAll().execute() } // (2)
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
                val arrayAdapterSpinner=ArrayAdapter(applicationContext,R.layout.activity_heaters_create_item, names)
                val dataStatus = arrayListOf<String>("ON","OFF")
                val arrayAdapterSpinnerStatus=ArrayAdapter(applicationContext,R.layout.activity_heaters_create_item,dataStatus)
                binding.spinnerHeatersRooms.adapter=arrayAdapterSpinner
                binding.spinnerHeaterStatus.adapter=arrayAdapterSpinnerStatus

                binding.spinnerHeatersRooms.onItemSelectedListener=object:AdapterView.OnItemSelectedListener{
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        realId = ids.get(id.toInt())
                        Toast.makeText(
                            applicationContext,
                            "Success $realId",
                            Toast.LENGTH_LONG
                        ).show()
                        println("SUCESS $realId")
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }

                }
                binding.spinnerHeaterStatus.onItemSelectedListener=object:AdapterView.OnItemSelectedListener{
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        if (id.toInt() == 0){
                            status= HeaterStatus.ON

                        } else {
                            status= HeaterStatus.OFF
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }

                }
            }

        }
    }

    fun createHeater(view: View){
        val heaterName = findViewById<EditText>(R.id.edit_heater_name).text.toString()
        val heaterPower = findViewById<EditText>(R.id.edit_heater_power).text.toString().toDouble()
        println(heaterPower)
        /*val heaterStatus = findViewById<EditText>(R.id.edit_heater_status).text.toString()
        val heaterRoomId = findViewById<EditText>(R.id.edit_heater_room_id).text.toString().toLong()*/
        val heaterDto = HeaterDto(null, heaterName, heaterPower, null, realId, status)
        lifecycleScope.launch(context = Dispatchers.IO) { // (1)
            runCatching { ApiServices().heatersApiService.create(heaterDto).execute() } // (2)
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