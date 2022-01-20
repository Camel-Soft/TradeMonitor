package com.camelsoft.trademonitor._presentation.activity_main.fragment_price

import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.camelsoft.trademonitor.R
import com.camelsoft.trademonitor._domain.models.MPriceColl

class FragmentPriceAdapter(
    private val listPriceColl: List<MPriceColl>,
    private val clickListener: (MPriceColl) -> Unit,
    private val clickListenerLong: (MPriceColl) -> Unit
): RecyclerView.Adapter<FragmentPriceAdapter.ViewHolder>()  {






    class ViewHolder(itemView: View, click: (Triple<String, Int, String>) -> Unit) : RecyclerView.ViewHolder(itemView) {
        var textCreated: TextView = itemView.findViewById(R.id.textCreated)
        var textChanged: TextView = itemView.findViewById(R.id.textChanged)
        var textTotal: TextView = itemView.findViewById(R.id.textTotal)
        var textNote: TextView = itemView.findViewById(R.id.textNote)

        var btnEdit: ImageButton = itemView.findViewById(R.id.btnEdit)
        var btnSave: ImageButton = itemView.findViewById(R.id.btnSave)

        var editNote: EditText = itemView.findViewById(R.id.editNote)
        var expandLayout: ConstraintLayout = itemView.findViewById(R.id.expandLayout)

        init {
            itemView.setOnClickListener {
                click(Triple("click", adapterPosition, ""))
            }

            itemView.setOnLongClickListener {
                click(Triple("clickLong", adapterPosition, ""))
                true
            }

            btnSave.setOnClickListener {
                click(Triple("clickBtnSave", adapterPosition, editNote.text.toString()))

            }



        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        return listPriceColl.size
    }


}