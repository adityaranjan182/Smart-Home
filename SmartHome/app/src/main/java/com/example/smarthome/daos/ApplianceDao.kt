package com.example.smarthome.daos

import android.media.MediaPlayer
import com.example.smarthome.databinding.ActivityMainBinding
import androidx.appcompat.app.AppCompatActivity
import com.example.smarthome.R
import com.example.smarthome.models.Appliance
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ApplianceDao : AppCompatActivity(){

    private lateinit var database : DatabaseReference

    fun updateStatus(home:String, appliance:String, status:Int){
        GlobalScope.launch {
            database = FirebaseDatabase.getInstance().getReference(home)
            database.child(appliance).setValue(status).await()
        }
    }
}