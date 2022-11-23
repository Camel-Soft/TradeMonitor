package com.camelsoft.trademonitor._presentation.activity_main.fragment_alko_mark

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.camelsoft.trademonitor.R
import com.camelsoft.trademonitor._presentation.models.alko.MAlkoMark
import com.camelsoft.trademonitor._presentation.utils.*
import com.camelsoft.trademonitor._presentation.utils.scan.getScanType
import com.camelsoft.trademonitor.common.App
import com.camelsoft.trademonitor.databinding.FragmentAlkoMarkItemBinding

class FragmentAlkoMarkAdapter : RecyclerView.Adapter<FragmentAlkoMarkAdapter.ViewHolder>() {

    private var list: List<MAlkoMark> = ArrayList()
    var setOnItemClickListener: ((Int) -> Unit)? = null
    var setOnItemLongClickListener: ((Int) -> Unit)? = null

    fun getList() = list

    fun submitList(newList: List<MAlkoMark>) {
        val oldList = list
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(
            ListItemsDiffCallback (oldList = oldList, newList = newList)
        )
        list = newList
        diffResult.dispatchUpdatesTo(this)
    }

    private class ListItemsDiffCallback (
        var oldList: List<MAlkoMark>,
        var newList: List<MAlkoMark>
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

    inner class ViewHolder(private var binding : FragmentAlkoMarkItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind (alkoMark: MAlkoMark) {
            binding.apply {
                textMark.text = alkoMark.marka
                textMarkType.text = alkoMark.marka_type
                textScancode.text = alkoMark.scancode
                textScancodeType.text = getScanType(alkoMark.scancode_type)
                textName.text = alkoMark.name
                textNote.text = alkoMark.note
                textQuantity.text = toQuantity(alkoMark.quantity)
                textCena.text = "x  "+toMoney(alkoMark.cena)+" "+App.getAppContext().resources.getString(R.string.money)
                textSumma.text = "=  "+autoSumm(kolvo = toQuantity(alkoMark.quantity), cena = toMoney(alkoMark.cena))+" "+ App.getAppContext().resources.getString(R.string.money)
                // Обработка Рабочего кода
                val wrkMess = getWrkMess(alkoMark.status_code)
                textStatus.text = wrkMess.first
                textStatus.setTextColor(App.getAppContext().getColor(wrkMess.second))
                // Обработка Кода ошибки
                val errMess = getErrMess(alkoMark.status_code)
                textError.text = errMess.first
                textError.setTextColor(App.getAppContext().getColor(errMess.second))
            }
            itemView.apply {
                setOnClickListener {
                    setOnItemClickListener?.invoke(adapterPosition)
                }
                setOnLongClickListener {
                    setOnItemLongClickListener?.invoke(adapterPosition)
                    true
                }
                setBackgroundColor(
                    App.getAppContext().getColor(getHolderColor(alkoMark.holder_color)))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(FragmentAlkoMarkItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(alkoMark = list[position])
    }

    override fun getItemCount() = list.size
}