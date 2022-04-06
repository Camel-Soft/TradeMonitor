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

    inner class ViewHolder(var binding : FragmentPriceGoodsItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind (priceGoods: MPriceGoods, position: Int) {
            binding.apply {
                textScancode.text = priceGoods.scancode
                textScancodeType.text = priceGoods.scancode_type
                textName.text = priceGoods.name
                textNote.text = priceGoods.note
                textCena.text = priceGoods.cena.toString()
            }
            itemView.apply {
                setOnClickListener {
                    setOnItemClickListener?.invoke(position)
                }
                setOnLongClickListener {
                    setOnItemLongClickListener?.invoke(position)
                    true
                }
            }
        }
    }
    var setOnItemClickListener: ((Int) -> Unit)? = null
    var setOnItemLongClickListener: ((Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(FragmentPriceGoodsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(priceGoods = differ.currentList[position], position = position)
    }

    override fun getItemCount() = differ.currentList.size
}