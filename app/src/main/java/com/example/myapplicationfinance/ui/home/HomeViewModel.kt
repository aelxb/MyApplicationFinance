package com.example.myapplicationfinance.ui.home

import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*


class HomeViewModel : ViewModel() {
    var database = FirebaseDatabase.getInstance().reference
    fun addOrRemove(action: Boolean, summ:String){
        CoroutineScope(Dispatchers.IO).launch {
            var totalsumm:Double= 0.0
            database.child("Summary").addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    totalsumm = snapshot.getValue(Int::class.java)?.toDouble()!!
                }
                override fun onCancelled(error: DatabaseError) {

                }
            })
            val key = database.child("Payments").push().key
            val map = hashMapOf<String, Any>()
            map["Date"] = getCurrentDateTime().month
            map["Summ"] = summ
            map["Action"] = action
            val mapFinal = hashMapOf<String, Any>()
            mapFinal["Payments/$key"] = map
            database
                .updateChildren(mapFinal)
            val total = hashMapOf<String, Any>()
            total["Summary"] = if (action) totalsumm + summ.toDouble() else totalsumm - summ.toDouble()
            database
                .updateChildren(total)
        }
    }
    fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }
}