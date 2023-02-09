package com.camelsoft.trademonitor._presentation.dialogs

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.camelsoft.trademonitor.databinding.DialogProgressBinding

class ShowProgress(context: Context) {
    private val layoutInflater = LayoutInflater.from(context)
    private val binding = DialogProgressBinding.inflate(layoutInflater)
    private val dialog = AlertDialog.Builder(context).setView(binding.root).setCancelable(false).create()

    fun showDialog() { dialog.show() }
    fun hideDialog() { dialog.cancel() }
    fun setTextTop(text: String) { binding.textTop.text = text }
    fun setTextBottom(text: String) { binding.textBottom.text = text }
    fun setMax(max: Int) { binding.progressBar.max = max }
    fun setProgress(progress: Int) { binding.progressBar.progress = progress }
    fun setIndeterminate(flag: Boolean) { binding.progressBar.isIndeterminate = flag }
}
