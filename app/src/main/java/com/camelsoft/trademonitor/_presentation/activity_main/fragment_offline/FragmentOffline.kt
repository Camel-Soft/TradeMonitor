package com.camelsoft.trademonitor._presentation.activity_main.fragment_offline

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.RECEIVER_NOT_EXPORTED
import androidx.core.content.ContextCompat.registerReceiver
import androidx.fragment.app.Fragment
import com.camelsoft.trademonitor.R
import com.camelsoft.trademonitor._presentation.services.OfflineService
import com.camelsoft.trademonitor.common.settings.Settings
import com.camelsoft.trademonitor.databinding.FragmentOfflineBinding
import dagger.hilt.android.AndroidEntryPoint
import java.lang.ref.WeakReference
import javax.inject.Inject

@AndroidEntryPoint
class FragmentOffline: Fragment() {
    private lateinit var binding: FragmentOfflineBinding
    private lateinit var weakContext: WeakReference<Context>
    @Inject lateinit var settings: Settings

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOfflineBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        weakContext = WeakReference<Context>(requireContext())

        //showEmptySource()

        showDoneSource()

        val intentFilter = IntentFilter("sdfg")

       // registerReceiver(weakContext.get()!!, offlineReceiver, intentFilter, RECEIVER_NOT_EXPORTED)


        binding.btnDownload.setOnClickListener {
            Intent(weakContext.get(), OfflineService::class.java).also {
                it.action = "maine Action"
                weakContext.get()!!.startService(it)
            }
        }

        binding.btnClear.setOnClickListener {
            Intent(weakContext.get(), OfflineService::class.java).also {
                weakContext.get()!!.stopService(it)
            }

//            val ttt = settings.getTest()
//            if (ttt == null) binding.textInfo.text = "null" else binding.textInfo.text = ttt



        }

    }

    private val offlineReceiver : BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            TODO("Not yet implemented")
        }
    }







    private fun showEmptySource() {
        binding.apply {
            textStage.text = ""
            textStage.visibility = View.GONE
            progressBar.progress = 0
            progressBar.visibility = View.GONE
            textProcent.text = ""
            textProcent.visibility = View.GONE
            textInfo.text = resources.getString(R.string.empty)
            textInfo.visibility = View.VISIBLE
            btnClear.isEnabled = false
            btnDownload.isEnabled = true
        }
    }

    private fun showDownloadProcessSource() {
        binding.apply {
            textStage.text = ""
            textStage.visibility = View.VISIBLE
            progressBar.progress = 0
            progressBar.visibility = View.VISIBLE
            textProcent.text = ""
            textProcent.visibility = View.VISIBLE
            textInfo.text = ""
            textInfo.visibility = View.VISIBLE
            btnClear.isEnabled = false
            btnDownload.isEnabled = false
        }
    }

    private fun showDoneSource() {
        binding.apply {
            textStage.text = ""
            textStage.visibility = View.GONE
            progressBar.progress = 0
            progressBar.visibility = View.GONE
            textProcent.text = ""
            textProcent.visibility = View.GONE
            textInfo.text = ""
            textInfo.visibility = View.VISIBLE
            btnClear.isEnabled = true
            btnDownload.isEnabled = true
        }
    }
}
