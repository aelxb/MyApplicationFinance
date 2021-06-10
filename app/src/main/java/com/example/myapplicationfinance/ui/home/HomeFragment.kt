package com.example.myapplicationfinance.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.TextView
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.myapplicationfinance.R

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val radioButtonAdd:RadioButton = root.findViewById(R.id.add)
        val radioButtonRemove:RadioButton = root.findViewById(R.id.remove)
        val buttonApply: Button = root.findViewById(R.id.apply)
        val etext : EditText = root.findViewById<EditText>(R.id.txt)
        buttonApply.setOnClickListener{
            homeViewModel.addOrRemove(radioButtonAdd.isChecked, etext.text.toString())
            etext.text = null
        }
        return root
    }
}