package com.example.smarthome

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.smarthome.databinding.ActivityBluetoothBinding
import java.io.OutputStream
import java.util.*

class BluetoothActivity : AppCompatActivity() {
    companion object{
        private lateinit var btAdapter : BluetoothAdapter
        private lateinit var btSocket: BluetoothSocket
        private lateinit var outputStream : OutputStream
        private const val REQUEST_CODE_BT = 1
        private const val MAC_ADDRESS_BT = "FC:A8:9A:00:16:F1"
        val mUUID: UUID? = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
        var BTstate = false
    }
    private lateinit var binding : ActivityBluetoothBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBluetoothBinding.inflate(layoutInflater)
        setContentView(binding.root)

        StartBT()

        binding.connectBtn.setOnClickListener {
            ConnectBT()
        }
        binding.OnBtn1.setOnClickListener {
            SendSignal(10)
        }
        binding.OffBtn1.setOnClickListener {
            SendSignal(11)
        }
        binding.OnBtn2.setOnClickListener {
            SendSignal(20)
        }
        binding.OffBtn2.setOnClickListener {
            SendSignal(21)
        }
        binding.OnBtn3.setOnClickListener {
            SendSignal(30)
        }
        binding.OffBtn3.setOnClickListener {
            SendSignal(31)
        }
        binding.OnBtn4.setOnClickListener {
            SendSignal(40)
        }
        binding.OffBtn4.setOnClickListener {
            SendSignal(41)
        }
        binding.internetBtn.setOnClickListener {
            SendSignal(2)
            val iotIntent = Intent(this,MainActivity::class.java)
            startActivity(iotIntent)
            finish()
        }
    }
    private fun StartBT(){
        btAdapter = BluetoothAdapter.getDefaultAdapter()
        if (!btAdapter.isEnabled) {
            ShowBtDisabledAlert()
        }
    }
    private fun ShowBtDisabledAlert(){
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setMessage("Bluetooth is disabled .\nWould you like to enable it?")
            .setCancelable(false)
            .setPositiveButton(
                "Yes"
            ) { dialog, id ->
                val enableBluetooth = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(enableBluetooth, REQUEST_CODE_BT)
            }
            .setNegativeButton(
                "No"
            ) { dialog, id -> dialog.cancel() }
            .create().show()
    }
    private fun ConnectBT(){
        if(btAdapter.isEnabled){
            val HomeBT = btAdapter.getRemoteDevice(MAC_ADDRESS_BT)
            var counter = 0
            btSocket = HomeBT.createRfcommSocketToServiceRecord(mUUID)
            while (!btSocket.isConnected){
                btSocket.connect()
                counter++
                if(counter == 4){
                    Toast.makeText(this,"Failed to connect",Toast.LENGTH_SHORT).show()
                    return
                }
            }
            if(btSocket.isConnected){
                BTstate = true
                SendSignal(1)
                Toast.makeText(this,"Successfully Connected",Toast.LENGTH_SHORT).show()
                binding.BtProgess.visibility = View.GONE
            }
        }else{
            Toast.makeText(this,"Turn on your Bluetooth first",Toast.LENGTH_SHORT).show()
            ShowBtDisabledAlert()
        }
    }
    private fun SendSignal(data:Int){
        val mediaPlayer = MediaPlayer.create(this,R.raw.click)
        mediaPlayer.start()
        if(BTstate && btAdapter.isEnabled){
            outputStream  = btSocket.outputStream
            outputStream.write(data)
        }else{
            Toast.makeText(this,"Turn on your Bluetooth first",Toast.LENGTH_SHORT).show()
        }
    }
}