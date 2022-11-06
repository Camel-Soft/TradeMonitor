package com.camelsoft.trademonitor._presentation.activity_main.fragment_alko

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
import com.camelsoft.trademonitor._presentation.models.alko.MAlkoColl
import com.camelsoft.trademonitor._presentation.models.secondary.MIntString
import com.camelsoft.trademonitor._presentation.utils.timeToString
import com.camelsoft.trademonitor.common.App
import com.camelsoft.trademonitor.databinding.FragmentAlkoCollItemBinding

class FragmentAlkoCollAdapter : RecyclerView.Adapter<FragmentAlkoCollAdapter.ViewHolder>() {

    private var list: List<MAlkoColl> = ArrayList()
    var clickHolder: ((Int) -> Unit)? = null
    var clickBtnShare: ((Int) -> Unit)? = null
    var clickBtnDelete: ((Int) -> Unit)? = null
    var clickBtnUpdate: ((MIntString) -> Unit)? = null

    fun getList() = list

    fun submitList(newList: List<MAlkoColl>) {
        val oldList = list
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(
            ListItemsDiffCallback (oldList = oldList, newList = newList)
        )
        list = newList
        diffResult.dispatchUpdatesTo(this)
    }

    private class ListItemsDiffCallback (
        var oldList: List<MAlkoColl>,
        var newList: List<MAlkoColl>
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

    inner class ViewHolder(private var binding : FragmentAlkoCollItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind (alkoColl: MAlkoColl) {
            binding.apply {
                textCreated.text = App.getAppContext().resources.getString(R.string.coll_created)+": "+ timeToString(alkoColl.created)

                if (alkoColl.created != alkoColl.changed)
                    textChanged.text = App.getAppContext().resources.getString(R.string.coll_changed)+": "+ timeToString(alkoColl.changed)
                else
                    textChanged.text = ""

                textNote.text = alkoColl.note
                textTotal.text = alkoColl.total.toString()
                editNote.hint = alkoColl.note

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
            val imm = App.getAppContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(itemView.windowToken, 0)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FragmentAlkoCollAdapter.ViewHolder {
        return ViewHolder(FragmentAlkoCollItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: FragmentAlkoCollAdapter.ViewHolder, position: Int) {
        holder.bind(alkoColl = list[position])
    }

    override fun getItemCount() = list.size
}