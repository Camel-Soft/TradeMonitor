package com.camelsoft.trademonitor._presentation.utils

import android.content.Context
import android.content.Context.VIBRATOR_SERVICE
import android.media.MediaPlayer
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import com.camelsoft.trademonitor.R
import com.camelsoft.trademonitor.common.App.Companion.getAppContext

fun vibrateOnce() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager = getAppContext().getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        val vibrator = vibratorManager.defaultVibrator
        vibrator.vibrate(VibrationEffect.createOneShot(100,1))
    } else {
        val vibrator = getAppContext().getSystemService(VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(100)
    }
}

fun beepOnce() {
    val mediaPlayer = MediaPlayer.create(getAppContext(), R.raw.beep)
    mediaPlayer.start()
}
