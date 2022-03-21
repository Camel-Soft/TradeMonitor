package com.camelsoft.trademonitor._presentation.activity_main.fragment_price_goods

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.camelsoft.trademonitor._domain.models.MPriceGoods
import com.camelsoft.trademonitor.databinding.FragmentPriceGoodsItemBinding

class FragmentPriceGoodsAdapter : RecyclerView.Adapter<FragmentPriceGoodsAdapter.ViewHolder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<MPriceGoods>() {
        override fun areItemsTheSame(oldItem: MPriceGoods, newItem: MPriceGoods): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MPriceGoods, newItem: MPriceGoods): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    fun setList(list: List<MPriceGoods>) = differ.submitList(list)

    inner class ViewHolder(var binding : FragmentPriceGoodsItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun getItemCount() = differ.currentList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(FragmentPriceGoodsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val priceGoods = differ.currentList[position]
        holder.binding.apply {
            textScancode.text = priceGoods.scancode
            textScancodeType.text = priceGoods.scancode_type
            textName.text = priceGoods.name
            textNote.text = priceGoods.note
            textCena.text = priceGoods.cena.toString()
        }
        holder.itemView.apply {
            setOnClickListener {
                onItemClickListener?.let { it(priceGoods) }
            }
            setOnLongClickListener {
                onItemLongClickListener?.let { it(priceGoods) }
                true
            }
        }
    }

    private var onItemClickListener: ((MPriceGoods) -> Unit)? = null

    fun setOnItemClickListener(listener: (MPriceGoods) -> Unit) {
        onItemClickListener = listener
    }

    private var onItemLongClickListener: ((MPriceGoods) -> Unit)? = null

    fun setOnItemLongClickListener(listener: (MPriceGoods) -> Unit) {
        onItemLongClickListener = listener
    }
}