package com.camelsoft.trademonitor._presentation.activity_main.fragment_object

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.camelsoft.trademonitor._presentation.models.MAddress
import com.camelsoft.trademonitor.databinding.FragmentObjectItemBinding

class FragmentObjectAdapter : RecyclerView.Adapter<FragmentObjectAdapter.ViewHolder>(), Filterable {
    private var list: List<MAddress> = ArrayList()
    private var listFull: List<MAddress> = ArrayList()
    var setOnItemClickListener: ((Int) -> Unit)? = null
    var setOnItemLongClickListener: ((Int) -> Unit)? = null

    fun getList() = list

    fun submitList(newList: List<MAddress>) {
        val oldList = list
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(
            ListItemsDiffCallback (oldList = oldList, newList = newList)
        )
        list = newList
        listFull = newList
        diffResult.dispatchUpdatesTo(this)
    }

    private class ListItemsDiffCallback (
        var oldList: List<MAddress>,
        var newList: List<MAddress>
    ): DiffUtil.Callback() {

        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].address == newList[newItemPosition].address

        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }

    inner class ViewHolder(private var binding : FragmentObjectItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind (mAddress: MAddress) {
            binding.apply {
                textBig.text = mAddress.name
                textSmall.text = mAddress.address
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
        return ViewHolder(FragmentObjectItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mAddress = list[position])
    }

    override fun getItemCount() = list.size

    override fun getFilter(): Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filterResults = FilterResults()
            val suggestions = ArrayList<MAddress>()

            if (constraint.isNullOrBlank()) suggestions.addAll(listFull)
            else {
                val filterPattern = constraint.toString().lowercase().trim()
                listFull.forEach {
                    if (it.name.lowercase().contains(filterPattern)
                        || it.address.lowercase().contains(filterPattern))
                        suggestions.add(it)
                }
            }

            filterResults.values = suggestions
            filterResults.count = suggestions.size
            return filterResults
        }

        @Suppress("UNCHECKED_CAST")
        override fun publishResults(constraint: CharSequence?, filterResults: FilterResults?) {
            list = filterResults?.values as List<MAddress>
            notifyDataSetChanged()
        }
    }
}
