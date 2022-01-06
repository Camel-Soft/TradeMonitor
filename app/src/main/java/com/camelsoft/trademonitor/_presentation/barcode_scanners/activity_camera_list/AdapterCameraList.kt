package com.camelsoft.trademonitor._presentation.barcode_scanners.activity_camera_list

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.camelsoft.trademonitor.R

class AdapterCameraList(
    private val listScanContinuous: List<MScanContinuous>,
    private val clickListener: (Int) -> Unit,
    private val clickListenerLong: (Int) -> Unit
): RecyclerView.Adapter<AdapterCameraList.ViewHolder>() {

    class ViewHolder(itemView: View, clickAtPosition: (Pair<String, Int>) -> Unit) : RecyclerView.ViewHolder(itemView) {
        var camListScancode: TextView = itemView.findViewById(R.id.camListScancode)
        var camListBitmap: ImageView = itemView.findViewById(R.id.camListBitmap)

        init {
            itemView.setOnClickListener {
                clickAtPosition(Pair("clickListener", adapterPosition))
            }

            itemView.setOnLongClickListener {
                clickAtPosition(Pair("clickListenerLong", adapterPosition))
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_camera_list_item, parent, false)
        return ViewHolder(itemView) {
            when (it.first) {
                "clickListener" -> { clickListener(it.second) }
                "clickListenerLong" -> { clickListenerLong(it.second) }
                else -> {}
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.camListScancode.text = listScanContinuous[position].scancode
        holder.camListBitmap.setImageBitmap(listScanContinuous[position].bitmap)
    }

    override fun getItemCount() = listScanContinuous.size

    data class MScanContinuous(
        var scancode: String,
        var bitmap: Bitmap
    )
}