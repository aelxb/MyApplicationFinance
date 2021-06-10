package com.example.myapplicationfinance.ui.notifications

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.myapplicationfinance.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.BarGraphSeries
import com.jjoe64.graphview.series.DataPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class NotificationsFragment : Fragment() {

    private lateinit var notificationsViewModel: NotificationsViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        notificationsViewModel =
                ViewModelProvider(this).get(NotificationsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_notifications, container, false)
        CoroutineScope(Dispatchers.IO).launch {
            var database = FirebaseDatabase.getInstance().reference
            val a = Array(2){9.0;8.0}
            val graph = root.findViewById(R.id.graph) as GraphView
            database.child("Payments").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.map {
                        if(it.child("Action").getValue(Boolean::class.java)==true){
                            a[0]+= it.child("Summ").getValue(String::class.java)!!.toDouble()
                        }
                        else
                            a[1]+= it.child("Summ").getValue(String::class.java)!!.toDouble()
                    }
                    val series = BarGraphSeries(arrayOf<DataPoint>(
                            DataPoint(0.0, a[0]*1),
                            DataPoint(1.0, a[1]*1)
                    ))
                    /*val text = root.findViewById(R.id.tx) as TextView
                    text.text = "${a[0]}${a[1]}"*/
                    graph.addSeries(series)
                    series.setValueDependentColor { data -> Color.rgb(data.x.toInt() * 255 / 4, Math.abs(data.y * 255 / 6).toInt(), 100) }
                    series.isDrawValuesOnTop = true
                    series.valuesOnTopColor = Color.RED
                }
                override fun onCancelled(error: DatabaseError) {

                }
            })
        }
        return root
    }
}