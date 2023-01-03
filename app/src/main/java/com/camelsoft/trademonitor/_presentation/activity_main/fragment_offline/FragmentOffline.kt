package com.camelsoft.trademonitor._presentation.activity_main.fragment_offline

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.camelsoft.trademonitor.R
import com.camelsoft.trademonitor._presentation.dialogs.showConfirm
import com.camelsoft.trademonitor._presentation.models.MOffline
import com.camelsoft.trademonitor._presentation.services.OfflineService
import com.camelsoft.trademonitor._presentation.utils.isServiceRunning
import com.camelsoft.trademonitor.common.App.Companion.getAppContext
import com.camelsoft.trademonitor.common.settings.Settings
import com.camelsoft.trademonitor.databinding.FragmentOfflineBinding
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
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

        showStandBy()
        beforeObserver()
        startObserver()
        clickDownload()
        clickCancel()
        clickClear()
    }

    private fun clickDownload() {
        binding.btnDownload.setOnClickListener {
            showConfirm(weakContext.get()!!, resources.getString(R.string.monitor_offlsource_label), resources.getString(R.string.offlsource_download_request)) {
                clearOffline()
                Intent(weakContext.get(), OfflineService::class.java).also {
                    weakContext.get()!!.startService(it)
                }
            }
        }
    }

    private fun clickCancel() {
        binding.btnCancel.setOnClickListener {
            showConfirm(weakContext.get()!!, resources.getString(R.string.monitor_offlsource_label), resources.getString(R.string.cancel_download)+"?") {
                Intent(weakContext.get(), OfflineService::class.java).also {
                    weakContext.get()!!.stopService(it)
                }
            }
        }
    }

    private fun clickClear() {
        binding.btnClear.setOnClickListener {
            showConfirm(weakContext.get()!!, resources.getString(R.string.monitor_offlsource_label), resources.getString(R.string.delete_files)+"?") {
                clearOffline()
            }
        }
    }

    private fun clearOffline() {
        val taskFolder = File(getAppContext().externalCacheDir, File.separator+"offlBase")
        taskFolder.deleteRecursively()
        settings.putMOffline(MOffline(
            status = 0,
            info = "",
            stageCurrent = 0,
            stageTotal = 0,
            stageName = "",
            stagePercent = 0
        ))
    }

    private fun beforeObserver() {
        if (!isServiceRunning(weakContext.get()!!, OfflineService::class.java)) {
            val mOffline = settings.getMOffline()
            if (mOffline.status > 0) {
                mOffline.status = -1
                settings.putMOffline(mOffline)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun startObserver() {
        settings.mOfflineLiveData.observe(viewLifecycleOwner) { mOffline ->
            binding.textInfo.text = mOffline.info
            if (mOffline.status <= 0) showStandBy() else {
                showProgress()
                binding.textStage.text = "${mOffline.stageName} ${mOffline.stageCurrent}/${mOffline.stageTotal}"
                binding.progressBar.progress = mOffline.stagePercent
                binding.textPercent.text = "${mOffline.stagePercent}%"
            }
        }
    }

    private fun showStandBy() {
        binding.apply {
            textStage.visibility = View.GONE
            progressBar.visibility = View.GONE
            textPercent.visibility = View.GONE
            btnDownload.visibility = View.VISIBLE
            btnCancel.visibility = View.INVISIBLE
            btnClear.visibility = View.VISIBLE
        }
    }

    private fun showProgress() {
        binding.apply {
            textStage.visibility = View.VISIBLE
            progressBar.visibility = View.VISIBLE
            textPercent.visibility = View.VISIBLE
            btnDownload.visibility = View.INVISIBLE
            btnCancel.visibility = View.VISIBLE
            btnClear.visibility = View.INVISIBLE
        }
    }
}
