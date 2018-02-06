package com.gg.vertivaldragview

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val strings = (0..100).map { "御坂${9800 + it}" }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = MyAdapter(strings)
    }


    private class MyAdapter(val data: List<String>) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MyViewHolder {
            val view = LayoutInflater.from(parent?.context).inflate(R.layout.item_string, parent, false)
            return MyViewHolder(view)
        }

        override fun getItemCount(): Int = data.size

        override fun onBindViewHolder(holder: MyViewHolder?, position: Int) {
            val viewHolder = holder as MyAdapter.MyViewHolder
            viewHolder.textView.text = data[position]
        }


        private inner class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
            val textView: TextView = view.findViewById(R.id.text)
        }
    }
}


