package com.camelsoft.trademonitor._presentation.activity_main.fragment_price_goods

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.camelsoft.trademonitor.R
import com.camelsoft.trademonitor._domain.models.MPriceGoods
import com.camelsoft.trademonitor._presentation.utils.*
import com.camelsoft.trademonitor._presentation.utils.scan.getScanType
import com.camelsoft.trademonitor.common.App.Companion.getAppContext
import com.camelsoft.trademonitor.databinding.FragmentPriceGoodsItemBinding

class FragmentPriceGoodsAdapter : RecyclerView.Adapter<FragmentPriceGoodsAdapter.ViewHolder>() {

    private var list: List<MPriceGoods> = ArrayList()
    var setOnItemClickListener: ((Int) -> Unit)? = null
    var setOnItemLongClickListener: ((Int) -> Unit)? = null

    fun getList() = list

    fun submitList(newList: List<MPriceGoods>) {
        val oldList = list
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(
            ListItemsDiffCallback (oldList = oldList, newList = newList)
        )
        list = newList
        diffResult.dispatchUpdatesTo(this)
    }

    private class ListItemsDiffCallback (
        var oldList: List<MPriceGoods>,
        var newList: List<MPriceGoods>
    ): DiffUtil.Callback() {

        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id

        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }

    inner class ViewHolder(private var binding : FragmentPriceGoodsItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind (priceGoods: MPriceGoods) {
            binding.apply {
                textScancode.text = priceGoods.scancode
                textScancodeType.text = getScanType(priceGoods.scancode_type)
                textName.text = priceGoods.name
                textNote.text = priceGoods.note
                textCena.text = toMoney(priceGoods.cena)+" "+getAppContext().resources.getString(R.string.money)
                textQuantity.text = toQuantity(priceGoods.quantity)+" "+priceGoods.ed_izm
                // Обработка Рабочего кода
                val wrkMess = getWrkMess(priceGoods.status_code)
                textStatus.text = wrkMess.first
                textStatus.setTextColor(getAppContext().getColor(wrkMess.second))
                // Обработка Кода ошибки
                val errMess = getErrMess(priceGoods.status_code)
                textError.text = errMess.first
                textError.setTextColor(getAppContext().getColor(errMess.second))
            }
            itemView.apply {
                setOnClickListener {
                    setOnItemClickListener?.invoke(adapterPosition)
                }
                setOnLongClickListener {
                    setOnItemLongClickListener?.invoke(adapterPosition)
                    true
                }
                setBackgroundColor(getAppContext().getColor(getHolderColor(priceGoods.holder_color)))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(FragmentPriceGoodsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(priceGoods = list[position])
    }

    override fun getItemCount() = list.size
}