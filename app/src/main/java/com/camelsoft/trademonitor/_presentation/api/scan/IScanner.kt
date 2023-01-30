package com.camelsoft.trademonitor._presentation.api.scan

import android.content.Context

interface IScanner {
    fun reg(context: Context, iResultScan: IResultScan, scanPropId: Int)
    fun unreg()
}
