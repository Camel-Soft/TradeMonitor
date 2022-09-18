package com.camelsoft.trademonitor._presentation.dialogs.specify_ch_zn

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import com.camelsoft.trademonitor.databinding.ItemPairStringStringBinding
import java.util.ArrayList

class AutocomplitePairSSAdapter(context: Context, pairs: ArrayList<Pair<String, String>>):
    ArrayAdapter<Pair<String, String>>(context, 0, pairs) {
    private val pairsFull = ArrayList(pairs)

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = ItemPairStringStringBinding.inflate(LayoutInflater.from(context))
        val item = getItem(position)
        item?.let {
            binding.string.text = "${it.first} - ${it.second}"
        }
        return binding.root
    }

    override fun getFilter() = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filterResults = FilterResults()
            val suggestions = ArrayList<Pair<String, String>>()

            if (constraint == null || constraint.isEmpty()) {
                suggestions.addAll(pairsFull)
            }
            else {
                val filterPattern = constraint.toString().lowercase().trim()
                pairsFull.forEach {
                    if (it.first.lowercase().contains(filterPattern)
                        || it.second.lowercase().contains(filterPattern))
                        suggestions.add(it)
                }
            }

            filterResults.values = suggestions
            filterResults.count = suggestions.size

            return filterResults
        }

        @Suppress("UNCHECKED_CAST")
        override fun publishResults(constraint: CharSequence?, filterResults: FilterResults?) {
            clear()
            addAll(filterResults?.values as ArrayList<Pair<String, String>>)
            notifyDataSetChanged()
        }

        @Suppress("UNCHECKED_CAST")
        override fun convertResultToString(resultValue: Any?): CharSequence {
            return (resultValue as Pair<String, String>).first
        }
    }
}