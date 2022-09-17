package com.camelsoft.trademonitor._presentation.utils.dialogs

import android.content.Context
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import com.applandeo.materialcalendarview.CalendarView
import com.applandeo.materialcalendarview.builders.DatePickerBuilder
import com.camelsoft.trademonitor._domain.models.MChZnXmlHead
import com.camelsoft.trademonitor.databinding.DialogChznSpecifyBinding
import java.util.*

class SpecifyChZn(context: Context, click: (mChZnXmlHead: MChZnXmlHead) -> Unit) {
    private val layoutInflater = LayoutInflater.from(context)
    private val binding = DialogChznSpecifyBinding.inflate(layoutInflater)
    private val adb = AlertDialog.Builder(context).setView(binding.root).setCancelable(true)
    private val dialog = adb.create()
    private val mChZnXmlHead = MChZnXmlHead()
    private var calendar = Calendar.getInstance()
    private val datePickerBuilder = DatePickerBuilder(context){calendar = it[0]}
        .setPickerType(CalendarView.ONE_DAY_PICKER)
        .build()

    init {
        val items = arrayOf("123456789", "987654321", "123000000000", "9873333333", "00000000000")
        val adapter = ArrayAdapter(context, android.R.layout.simple_dropdown_item_1line, items)
        binding.editInn.setAdapter(adapter)

        binding.editDate.setOnClickListener {
            datePickerBuilder.show()
        }

        binding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        binding.btnOk.setOnClickListener {
            click(mChZnXmlHead)
            dialog.dismiss()
        }
        dialog.show()
    }
}
