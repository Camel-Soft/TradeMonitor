package com.camelsoft.trademonitor._presentation.activity_main.fragment_price

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.camelsoft.trademonitor.R
import com.camelsoft.trademonitor._domain.models.MPriceColl
import com.camelsoft.trademonitor._domain.models.MIntString
import com.camelsoft.trademonitor._presentation.utils.timeToString
import com.camelsoft.trademonitor.common.App.Companion.getAppContext
import com.google.android.material.card.MaterialCardView

class FragmentPriceAdapter(
    private val listPriceColl: List<MPriceColl>,
    private val clickHolder: (Int) -> Unit,
    private val clickBtnShare: (Int) -> Unit,
    private val clickBtnDelete: (Int) -> Unit,
    private val clickBtnUpdate: (MIntString) -> Unit
): RecyclerView.Adapter<FragmentPriceAdapter.ViewHolder>()  {

    class ViewHolder(
        itemView: View,
        private val click: (Triple<String, Int, String>) -> Unit
    ): RecyclerView.ViewHolder(itemView) {

        var textCreated: TextView = itemView.findViewById(R.id.textCreated)
        var textChanged: TextView = itemView.findViewById(R.id.textChanged)
        var textTotal: TextView = itemView.findViewById(R.id.textTotal)
        var textNote: TextView = itemView.findViewById(R.id.textNote)

        var btnEdit: ImageButton = itemView.findViewById(R.id.btnEdit)
        private var btnUpdate: ImageButton = itemView.findViewById(R.id.btnUpdate)

        var editNote: EditText = itemView.findViewById(R.id.editNote)
        var layoutExpand: ConstraintLayout = itemView.findViewById(R.id.layoutExpand)

        var cardGeneral: MaterialCardView = itemView.findViewById(R.id.cardGeneral)
        var layoutLongClick: ConstraintLayout = itemView.findViewById(R.id.layoutLongClick)
        var btnShare: ImageButton = itemView.findViewById(R.id.btnShare)
        var btnDelete: ImageButton = itemView.findViewById(R.id.btnDelete)

        init {
            cardGeneral.setOnClickListener {
                click(Triple("clickHolder", adapterPosition, ""))
            }

            cardGeneral.setOnLongClickListener {
                if (layoutLongClick.isVisible) layoutLongClick.visibility = View.GONE
                else layoutLongClick.visibility = View.VISIBLE
                true
            }

            btnShare.setOnClickListener {
                click(Triple("clickBtnShare", adapterPosition, ""))
            }

            btnDelete.setOnClickListener {
                click(Triple("clickBtnDelete", adapterPosition, ""))
            }

            btnEdit.setOnClickListener {
                if (layoutExpand.isVisible) {
                    layoutExpand.visibility = View.GONE
                    hideKeyboard()
                }
                else layoutExpand.visibility = View.VISIBLE
            }

            // Обновить Примечание у Сборки кнопкой РИСОВАННОЙ
            btnUpdate.setOnClickListener {
                updateNote()
                hideKeyboard()
            }

            // Обновить Примечание у Сборки кнопкой КЛАВИАТУРНОЙ
            editNote.setOnEditorActionListener { textView, actionId, keyEvent ->
                return@setOnEditorActionListener when (actionId) {
                    EditorInfo.IME_ACTION_DONE -> {
                        updateNote()
                        hideKeyboard()
                        true
                    }
                    else -> false
                }
            }
        }

        // Обновить примечание у Сборки
        private fun updateNote() {
            var note = editNote.text.toString()
            if (note == "") note = editNote.hint.toString()
            click(Triple("clickBtnUpdate", adapterPosition, note))
        }

        // Спрятать клавиатуру
        private fun hideKeyboard() {
            val imm = getAppContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(itemView.windowToken, 0)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.fragment_price_item, parent, false)
        return ViewHolder(itemView) {
            when (it.first) {
                "clickHolder" -> { clickHolder(it.second) }
                "clickBtnShare" -> { clickBtnShare(it.second) }
                "clickBtnDelete" -> { clickBtnDelete(it.second) }
                "clickBtnUpdate" -> { clickBtnUpdate(MIntString(
                    int = it.second,
                    string = it.third)) }
                else -> {}
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, pos: Int) {
        holder.textCreated.text = getAppContext().resources.getString(R.string.coll_created)+": "+timeToString(listPriceColl[pos].created)

        if (listPriceColl[pos].created != listPriceColl[pos].changed)
            holder.textChanged.text = getAppContext().resources.getString(R.string.coll_changed)+": "+timeToString(listPriceColl[pos].changed)
        else
            holder.textChanged.text = ""

        holder.textNote.text = listPriceColl[pos].note
        holder.textTotal.text = listPriceColl[pos].total.toString()
        holder.editNote.hint = listPriceColl[pos].note
    }

    override fun getItemCount(): Int {
        return listPriceColl.size
    }
}