package com.camelsoft.trademonitor._presentation.activity_main.fragment_checker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.camelsoft.trademonitor._presentation.models.secondary.MStringString
import com.camelsoft.trademonitor.databinding.FragmentCheckerItemBinding

class FragmentCheckerAdapter : RecyclerView.Adapter<FragmentCheckerAdapter.ViewHolder>() {

    private var list: List<MStringString> = ArrayList()
    var setOnItemClickListener: ((Int) -> Unit)? = null
    var setOnItemLongClickListener: ((Int) -> Unit)? = null

    fun getList() = list

    fun submitList(newList: List<MStringString>) {
        val oldList = list
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(
            ListItemsDiffCallback (oldList = oldList, newList = newList)
        )
        list = newList
        diffResult.dispatchUpdatesTo(this)
    }

    private class ListItemsDiffCallback (
        var oldList: List<MStringString>,
        var newList: List<MStringString>
    ): DiffUtil.Callback() {

        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].string1 == newList[newItemPosition].string1

        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }

    inner class ViewHolder(private var binding: FragmentCheckerItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind (mStringString: MStringString) {
            binding.apply {
                textHeader.text = mStringString.string1
                textMessage.text = mStringString.string2
            }
            itemView.apply {
                setOnClickListener {
                    setOnItemClickListener?.invoke(adapterPosition)
                }
                setOnLongClickListener {
                    setOnItemLongClickListener?.invoke(adapterPosition)
                    true
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(FragmentCheckerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mStringString = list[position])
    }

    override fun getItemCount() = list.size
}
