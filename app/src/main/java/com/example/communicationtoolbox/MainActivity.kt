package com.example.communicationtoolbox

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.example.communicationtoolbox.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    //private lateinit var getResult: ActivityResultLauncher<Intent>
    private lateinit var binding: ActivityMainBinding
    private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()

    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            showToastMessage("Bluetooth enable success")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initBluetooth()

        binding.buttonScan.setOnClickListener {
            scanBluetooth()
        }

        // Register for broadcasts when a device is discovered.
        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        registerReceiver(receiver, filter)
    }

    // Toast for button execution result
    private fun showToastMessage(_message: String) {
        var toast = Toast.makeText(this, _message, Toast.LENGTH_LONG)

        toast?.cancel()
        toast = Toast.makeText(this, _message, Toast.LENGTH_LONG)
        toast.show()
    }

    private fun initBluetooth() {
        // Check Bluetooth support
//        val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
        if (bluetoothAdapter == null) {
            // Device doesn't support Bluetooth
            showToastMessage("Device doesn't support Bluetooth")
        }

        // If not enabled 'Bluetooth', request enable
        if (bluetoothAdapter?.isEnabled == false) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startForResult.launch(enableBtIntent)
        }
    }


    private fun scanBluetooth() {
        if (bluetoothAdapter?.isEnabled == true) {
            bluetoothAdapter.startDiscovery()
        }
        else {
            showToastMessage("Bluetooth is not Enable. Please check 'Bluetooth'")
        }
    }

    // Create a BroadcastReceiver for ACTION_FOUND.
    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            showToastMessage("onReceive")
            when(intent.action) {
                BluetoothDevice.ACTION_FOUND -> {
                    // Discovery has found a device. Get the BluetoothDevice
                    // object and its info from the Intent.
                    val device: BluetoothDevice? =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    val deviceName = device?.name
                    val deviceHardwareAddress = device?.address // MAC address

                    showToastMessage("device name : $deviceName")
                    showToastMessage("device adder : $deviceHardwareAddress")
                }
            }
        }
    }

}