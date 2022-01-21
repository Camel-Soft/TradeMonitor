package com.camelsoft.trademonitor._presentation.activity_main.fragment_price

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.camelsoft.trademonitor.R
import com.camelsoft.trademonitor._domain.models.MPriceColl
import com.camelsoft.trademonitor._domain.models.MPriceCollUpdate
import com.camelsoft.trademonitor._presentation.utils.timeToString
import com.camelsoft.trademonitor.common.App.Companion.getAppContext

class FragmentPriceAdapter(
    private val listPriceColl: List<MPriceColl>,
    private val clickHolder: (MPriceColl) -> Unit,
    private val clickHolderLong: (MPriceColl) -> Unit,
    private val clickBtnUpdate: (MPriceCollUpdate) -> Unit
): RecyclerView.Adapter<FragmentPriceAdapter.ViewHolder>()  {

    class ViewHolder(itemView: View, click: (Triple<String, Int, String>) -> Unit) : RecyclerView.ViewHolder(itemView) {
        var textCreated: TextView = itemView.findViewById(R.id.textCreated)
        var textChanged: TextView = itemView.findViewById(R.id.textChanged)
        var textTotal: TextView = itemView.findViewById(R.id.textTotal)
        var textNote: TextView = itemView.findViewById(R.id.textNote)

        var btnEdit: ImageButton = itemView.findViewById(R.id.btnEdit)
        private var btnUpdate: ImageButton = itemView.findViewById(R.id.btnUpdate)

        var editNote: EditText = itemView.findViewById(R.id.editNote)
        var expandLayout: ConstraintLayout = itemView.findViewById(R.id.expandLayout)

        init {
            itemView.setOnClickListener {
                click(Triple("clickHolder", adapterPosition, ""))
            }

            itemView.setOnLongClickListener {
                click(Triple("clickHolderLong", adapterPosition, ""))
                true
            }

            btnUpdate.setOnClickListener {
                click(Triple("clickBtnUpdate", adapterPosition, editNote.text.toString()))

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.fragment_price_item, parent, false)
        return ViewHolder(itemView) {
            when (it.first) {
                "clickHolder" -> { clickHolder(listPriceColl[it.second]) }
                "clickHolderLong" -> { clickHolderLong(listPriceColl[it.second]) }
                "clickBtnUpdate" -> { clickBtnUpdate(MPriceCollUpdate(
                    priceColl = listPriceColl[it.second],
                    newNote = it.third)) }
                else -> {}
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, pos: Int) {
        holder.textCreated.text = getAppContext().resources.getString(R.string.created)+": "+timeToString(listPriceColl[pos].created)

        if (listPriceColl[pos].created != listPriceColl[pos].changed)
            holder.textChanged.text = getAppContext().resources.getString(R.string.changed)+": "+timeToString(listPriceColl[pos].changed)
        else
            holder.textChanged.text = ""

        holder.textNote.text = listPriceColl[pos].note
        holder.textTotal.text = listPriceColl[pos].total.toString()
        holder.editNote.hint = listPriceColl[pos].note

        holder.btnEdit.setOnClickListener {
            if (holder.expandLayout.isVisible) holder.expandLayout.visibility = View.GONE
            else holder.expandLayout.visibility = View.VISIBLE
        }
    }

    override fun getItemCount(): Int {
        return listPriceColl.size
    }
}