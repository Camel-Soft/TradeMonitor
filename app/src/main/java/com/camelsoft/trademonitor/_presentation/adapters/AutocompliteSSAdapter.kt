package com.camelsoft.trademonitor._presentation.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import com.camelsoft.trademonitor._presentation.models.secondary.MStringString
import com.camelsoft.trademonitor.databinding.ItemStringBinding
import java.util.ArrayList

class AutocompliteSSAdapter(context: Context, listMStringString: ArrayList<MStringString>):
    ArrayAdapter<MStringString>(context, 0, listMStringString) {
    private val listFull = ArrayList(listMStringString)

    @SuppressLint("ViewHolder", "SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = ItemStringBinding.inflate(LayoutInflater.from(context))
        val item = getItem(position)
        item?.let {
            binding.string.text = "${it.string1} - ${it.string2}"
        }
        return binding.root
    }

    override fun getFilter() = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filterResults = FilterResults()
            val suggestions = ArrayList<MStringString>()

            if (constraint == null || constraint.isEmpty()) {
                suggestions.addAll(listFull)
            }
            else {
                val filterPattern = constraint.toString().lowercase().trim()
                listFull.forEach {
                    if (it.string1.lowercase().contains(filterPattern)
                        || it.string2.lowercase().contains(filterPattern))
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
            addAll(filterResults?.values as ArrayList<MStringString>)
            notifyDataSetChanged()
        }

        @Suppress("UNCHECKED_CAST")
        override fun convertResultToString(resultValue: Any?): CharSequence {
            return (resultValue as MStringString).string1
        }
    }
}