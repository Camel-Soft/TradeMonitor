package com.camelsoft.trademonitor._presentation.barcode_scanners.empty_scanner

import android.content.Context
import com.camelsoft.trademonitor._presentation.api.scan.IResultScan
import com.camelsoft.trademonitor._presentation.api.scan.IScanner

class EmptyScannerImpl : IScanner {
    override fun reg(context: Context, iResultScan: IResultScan, scanPropId: Int) { }
    override fun unreg() { }
}
