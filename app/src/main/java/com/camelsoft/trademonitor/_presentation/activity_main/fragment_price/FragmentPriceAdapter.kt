package com.camelsoft.trademonitor._presentation.activity_main.fragment_price

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.camelsoft.trademonitor.R
import com.camelsoft.trademonitor._presentation.models.price.MPriceColl
import com.camelsoft.trademonitor._presentation.models.secondary.MIntString
import com.camelsoft.trademonitor._presentation.utils.timeToString
import com.camelsoft.trademonitor.common.App.Companion.getAppContext
import com.camelsoft.trademonitor.databinding.FragmentPriceItemBinding

class FragmentPriceAdapter : RecyclerView.Adapter<FragmentPriceAdapter.ViewHolder>()  {

    private var list: List<MPriceColl> = ArrayList()
    var clickHolder: ((Int) -> Unit)? = null
    var clickBtnShare: ((Int) -> Unit)? = null
    var clickBtnDelete: ((Int) -> Unit)? = null
    var clickBtnUpdate: ((MIntString) -> Unit)? = null

    fun getList() = list

    fun submitList(newList: List<MPriceColl>) {
        val oldList = list
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(
            ListItemsDiffCallback (oldList = oldList, newList = newList)
        )
        list = newList
        diffResult.dispatchUpdatesTo(this)
    }

    private class ListItemsDiffCallback (
        var oldList: List<MPriceColl>,
        var newList: List<MPriceColl>
    ): DiffUtil.Callback() {

        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id_coll == newList[newItemPosition].id_coll

        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }

    inner class ViewHolder(private var binding : FragmentPriceItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind (priceColl: MPriceColl) {
            binding.apply {
                textCreated.text = getAppContext().resources.getString(R.string.coll_created)+": "+timeToString(priceColl.created)

                if (priceColl.created != priceColl.changed)
                    textChanged.text = getAppContext().resources.getString(R.string.coll_changed)+": "+timeToString(priceColl.changed)
                else
                    textChanged.text = ""

                textNote.text = priceColl.note
                textTotal.text = priceColl.total.toString()
                editNote.hint = priceColl.note

                cardGeneral.setOnClickListener {
                    clickHolder?.invoke(adapterPosition)
                }

                layoutLongClick.visibility = View.GONE
                cardGeneral.setOnLongClickListener {
                    if (layoutLongClick.isVisible) layoutLongClick.visibility = View.GONE
                    else layoutLongClick.visibility = View.VISIBLE
                    true
                }

                btnShare.setOnClickListener {
                    clickBtnShare?.invoke(adapterPosition)
                }

                btnDelete.setOnClickListener {
                    clickBtnDelete?.invoke(adapterPosition)
                }

                layoutExpand.visibility = View.GONE
                btnEdit.setOnClickListener {
                    if (layoutExpand.isVisible) {
                        layoutExpand.visibility = View.GONE
                        hideKeyboard()
                    }
                    else {
                        editNote.text.clear()
                        layoutExpand.visibility = View.VISIBLE
                    }
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
        }

        // Обновить примечание у Сборки
        private fun updateNote() {
            var note = binding.editNote.text.toString()
            if (note == "") note = binding.editNote.hint.toString()
            clickBtnUpdate?.invoke(MIntString(int = adapterPosition, string = note))
        }

        // Спрятать клавиатуру
        private fun hideKeyboard() {
            val imm = getAppContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(itemView.windowToken, 0)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FragmentPriceAdapter.ViewHolder {
        return ViewHolder(FragmentPriceItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: FragmentPriceAdapter.ViewHolder, position: Int) {
        holder.bind(priceColl = list[position])
    }

    override fun getItemCount() = list.size
}