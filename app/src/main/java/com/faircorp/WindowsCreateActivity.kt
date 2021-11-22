package com.faircorp
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.lifecycle.lifecycleScope
import com.faircorp.databinding.ActivityWindowsCreateBinding
import com.faircorp.model.ApiServices
import com.faircorp.model.WindowDto
import com.faircorp.model.WindowStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WindowsCreateActivity : BasicActivity() {

    var realId : Long? = 0
    var status : WindowStatus? = WindowStatus.OPEN
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_windows_create)

        val binding = ActivityWindowsCreateBinding.inflate(layoutInflater)
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
                val arrayAdapterSpinner=ArrayAdapter(applicationContext,R.layout.activity_windows_create_item, names)
                val dataStatus = arrayListOf<String>("OPENED","CLOSED")
                val arrayAdapterSpinnerStatus=ArrayAdapter(applicationContext,R.layout.activity_windows_create_item, dataStatus)
                binding.spinnerWindowsRooms.adapter=arrayAdapterSpinner
                binding.spinnerWindowsStatus.adapter=arrayAdapterSpinnerStatus

                binding.spinnerWindowsRooms.onItemSelectedListener=object:AdapterView.OnItemSelectedListener{
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        /*val realId : Long? = ids.get(id.toInt())*/
                        realId = ids.get(id.toInt())
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }

                }
                binding.spinnerWindowsStatus.onItemSelectedListener=object:AdapterView.OnItemSelectedListener{
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        println(id)
                        if (id.toInt() == 0){
                            status=WindowStatus.OPEN

                        } else {
                            status=WindowStatus.CLOSED
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        TODO("Not yet implemented")
                    }

                }
            }
        }
    }

    fun createWindow(view: View){
        val windowName = findViewById<EditText>(R.id.edit_window_name).text.toString()
        val windowRoomId = realId
        val windowDto = WindowDto(null,windowName,null,windowRoomId,status)
        println(windowName)
        println(windowRoomId)
        lifecycleScope.launch(context = Dispatchers.IO) { // (1)
            runCatching { ApiServices().windowsApiService.create(windowDto).execute() } // (2)
                .onSuccess {
                    withContext(context = Dispatchers.Main) { // (3)
                        Toast.makeText(
                            applicationContext,
                            "Success Create $realId",
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