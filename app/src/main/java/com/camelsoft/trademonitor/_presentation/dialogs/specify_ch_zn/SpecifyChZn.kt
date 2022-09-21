package com.camelsoft.trademonitor._presentation.dialogs.specify_ch_zn

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.applandeo.materialcalendarview.CalendarView
import com.applandeo.materialcalendarview.builders.DatePickerBuilder
import com.camelsoft.trademonitor.R
import com.camelsoft.trademonitor._domain.models.MChZnXmlHead
import com.camelsoft.trademonitor._presentation.adapters.AutocompliteSSAdapter
import com.camelsoft.trademonitor._presentation.models.MStringString
import com.camelsoft.trademonitor._presentation.utils.timeToStringShort
import com.camelsoft.trademonitor.common.App.Companion.getAppContext
import com.camelsoft.trademonitor.databinding.DialogChznSpecifyBinding
import java.util.*
import kotlin.collections.ArrayList

class SpecifyChZn(
    private val context: Context,
    itemsInn: ArrayList<MStringString>,
    click: (mChZnXmlHead: MChZnXmlHead) -> Unit
) {
    private val layoutInflater = LayoutInflater.from(context)
    private val binding = DialogChznSpecifyBinding.inflate(layoutInflater)
    private val adb = AlertDialog.Builder(context).setView(binding.root).setCancelable(true)
    private val dialog = adb.create()
    private val mChZnXmlHead = MChZnXmlHead()
    private var calDate = Calendar.getInstance()

    init {
        binding.editInn.setAdapter(AutocompliteSSAdapter(context, itemsInn))

        binding.editDate.text = timeToStringShort(calDate.timeInMillis)
        binding.editDate.setOnClickListener {
            datePicker(context) {
                calDate = it[0]
                binding.editDate.text = timeToStringShort(calDate.timeInMillis)
            }
        }

        binding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        binding.btnOk.setOnClickListener {
            if (collectHead()) {
                click(mChZnXmlHead)
                dialog.dismiss()
            }
        }
        dialog.show()
    }

    private fun collectHead(): Boolean {
        if (binding.editInn.text.toString().isEmpty()) {
            Toast.makeText(context, getAppContext().resources.getString(R.string.need_fill_fields)+": "+getAppContext().resources.getString(R.string.inn), Toast.LENGTH_SHORT).show()
            return false
        }
        mChZnXmlHead.innMy = binding.editInn.text.toString()
        mChZnXmlHead.dateDoc = calDate.timeInMillis
        return true
    }

    @SuppressLint("ResourceType")
    private fun datePicker(context: Context, click: (MutableList<Calendar>) -> Unit) {
        DatePickerBuilder(context) {
            click(it)
        }
            .setPickerType(CalendarView.ONE_DAY_PICKER)
            .setHeaderColor(getAppContext().getColor(R.color.yellow_200))
            .build()
            .show()
    }
}