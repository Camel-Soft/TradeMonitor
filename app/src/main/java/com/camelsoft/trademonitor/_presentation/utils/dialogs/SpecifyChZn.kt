package com.camelsoft.trademonitor._presentation.utils.dialogs

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.camelsoft.trademonitor._domain.models.MChZnXmlHead
import com.camelsoft.trademonitor.databinding.DialogChznSpecifyBinding

class SpecifyChZn(context: Context, click: (mChZnXmlHead: MChZnXmlHead) -> Unit) {
    private val layoutInflater = LayoutInflater.from(context)
    private val binding = DialogChznSpecifyBinding.inflate(layoutInflater)
    private val adb = AlertDialog.Builder(context).setView(binding.root).setCancelable(true)
    private val dialog = adb.create()

    init {
        binding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        binding.btnOk.setOnClickListener {
            click(MChZnXmlHead())
            dialog.dismiss()
        }
        dialog.show()
    }
}
