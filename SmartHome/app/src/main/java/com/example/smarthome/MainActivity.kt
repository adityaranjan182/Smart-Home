package com.example.smarthome

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.smarthome.databinding.ActivityMainBinding
import com.example.smarthome.daos.ApplianceDao
import com.example.smarthome.models.Appliance
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import android.view.View
import android.widget.Button
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class MainActivity : AppCompatActivity() {

    companion object{
        const val home = "home-1"
        var status = -1
    }
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val applianceDao = ApplianceDao()
        val applianceStatus = Appliance()

        getCurrentStatus(applianceStatus)
        updateUI(applianceStatus)

        binding.onBtn1.setOnClickListener{
            status = 1
            applianceDao.updateStatus(home,"appliance-1",status)
            setVisibility(binding.offBtn1,binding.onBtn1) }
        binding.offBtn1.setOnClickListener{
            status = 0
            applianceDao.updateStatus(home,"appliance-1",status)
            setVisibility(binding.onBtn1,binding.offBtn1)
        }
        binding.onBtn2.setOnClickListener{
            status = 1
            applianceDao.updateStatus(home,"appliance-2",status)
            setVisibility(binding.offBtn2,binding.onBtn2)
        }
        binding.offBtn2.setOnClickListener{
            status = 0
            applianceDao.updateStatus(home,"appliance-2",status)
            setVisibility(binding.onBtn2,binding.offBtn2)
        }
        binding.onBtn3.setOnClickListener{
            status = 1
            applianceDao.updateStatus(home,"appliance-3",status)
            setVisibility(binding.offBtn3,binding.onBtn3)
        }
        binding.offBtn3.setOnClickListener{
            status = 0
            applianceDao.updateStatus(home,"appliance-3",status)
            setVisibility(binding.onBtn3,binding.offBtn3)
        }
        binding.onBtn4.setOnClickListener{
            status = 1
            applianceDao.updateStatus(home,"appliance-4",status)
            setVisibility(binding.offBtn4,binding.onBtn4)
        }
        binding.offBtn4.setOnClickListener{
            status = 0
            applianceDao.updateStatus(home,"appliance-4",status)
            setVisibility(binding.onBtn4,binding.offBtn4)
        }
        binding.bluetoothBtn.setOnClickListener {
            val bluetoothIntent = Intent(this, BluetoothActivity::class.java)
            startActivity(bluetoothIntent)
            finish()
        }
    }
    private fun setVisibility(VisibleBtn: Button, InvisibleBtn:Button){
        VisibleBtn.visibility = View.VISIBLE
        InvisibleBtn.visibility = View.GONE
        clickSound()
    }
    private fun clickSound(){
        val mediaPlayer = MediaPlayer.create(this,R.raw.click)
        mediaPlayer.start()
    }
    private fun updateUI(appliance: Appliance){
        when(appliance.appliance1){
            1 -> binding.offBtn1.visibility = View.VISIBLE
            0 -> binding.onBtn1.visibility = View.VISIBLE
        }
        when(appliance.appliance2){
            1 -> binding.offBtn2.visibility = View.VISIBLE
            0 -> binding.onBtn2.visibility = View.VISIBLE
        }
        when(appliance.appliance3){
            1 -> binding.offBtn3.visibility = View.VISIBLE
            0 -> binding.onBtn3.visibility = View.VISIBLE
        }
        when(appliance.appliance4){
            1 -> binding.offBtn4.visibility = View.VISIBLE
            0 -> binding.onBtn4.visibility = View.VISIBLE
        }
        binding.progressBar.visibility = View.GONE
    }
    private fun getCurrentStatus(appliance : Appliance){
        var database : DatabaseReference
        GlobalScope.launch {
            database = FirebaseDatabase.getInstance().getReference(home)
            database.get().addOnSuccessListener {
                if(it.exists()){
                    appliance.appliance1 = it.child("appliance-1").value.toString().toInt()
                    appliance.appliance2 = it.child("appliance-2").value.toString().toInt()
                    appliance.appliance3 = it.child("appliance-3").value.toString().toInt()
                    appliance.appliance4 = it.child("appliance-4").value.toString().toInt()

                    updateUI(appliance)
                }
            }.await()
        }
    }
}