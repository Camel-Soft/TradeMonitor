package com.camelsoft.trademonitor._domain.use_cases.use_cases_export

import com.camelsoft.trademonitor.R
import com.camelsoft.trademonitor._data.storage.room.IRoom
import com.camelsoft.trademonitor._domain.models.MAlkoColl
import com.camelsoft.trademonitor._domain.models.MAlkoMark
import com.camelsoft.trademonitor._domain.models.MChZnXmlHead
import com.camelsoft.trademonitor._presentation.utils.rm001d
import com.camelsoft.trademonitor._presentation.utils.timeToChZn
import com.camelsoft.trademonitor._presentation.utils.toMoneyKop
import com.camelsoft.trademonitor.common.App.Companion.getAppContext
import com.camelsoft.trademonitor.common.events.EventsSync
import org.redundent.kotlin.xml.PrintOptions
import org.redundent.kotlin.xml.xml
import java.io.File
import java.io.FileWriter
import javax.inject.Inject

class UseCaseExpChZnWithdrawal @Inject constructor(private val iRoom: IRoom) {
    private lateinit var file: File
    private lateinit var fileWriter: FileWriter

    suspend fun execute(alkoColl: MAlkoColl, mChZnXmlHead: MChZnXmlHead): EventsSync<File> {
        try{
            val listAlkoMark = iRoom.getRoomAlkoMarks(id_coll = alkoColl.id_coll)
            when (mChZnXmlHead.withdrawalType) {
                "PACKING" -> {
                    openFile(fileName = "${mChZnXmlHead.innMy}_${getAppContext().resources.getString(R.string.withdrawal)}_${getAppContext().resources.getString(R.string.packing)}.xml")
                    fileWriter.write(makePacking(listAlkoMark, mChZnXmlHead))
                    closeFile()
                    return EventsSync.Success(file)
                }
                else -> {
                    return EventsSync.Error(
                        getAppContext().resources.getString(R.string.error_in)+
                                " UseCaseExpChZnWithdrawal.execute: "+
                                getAppContext().resources.getString(R.string.error_withdrawal_type)
                    )
                }
            }
        }
        catch (e: Exception) {
            e.printStackTrace()
            return EventsSync.Error(
                getAppContext().resources.getString(R.string.error_in)+
                    " UseCaseExpChZnWithdrawal.execute: "+e.message
            )
        }
    }

    private fun makePacking(listAlkoMark: List<MAlkoMark>, mChZnXmlHead: MChZnXmlHead): String {
        try {
            val xml = xml("withdrawal") {
                globalProcessingInstruction("xml", "version" to "1.0", "encoding" to "UTF-8")
                attribute("version", 8)
                "trade_participant_inn" {
                    -mChZnXmlHead.innMy
                }
                "withdrawal_type" {
                    -mChZnXmlHead.withdrawalType
                }
                "withdrawal_date" {
                    -timeToChZn(mChZnXmlHead.dateDoc)
                }
                "products_list" {
                    listAlkoMark.forEach {
                        "product" {
                            "cis" {
                                -it.marka.rm001d().substring(0, 24)
                            }
                            "cost" {
                                -toMoneyKop(it.cena)
                            }
                        }
                    }
                }
            }

            return xml.toString(PrintOptions(singleLineTextElements = true))+"\n"
        }
        catch (e: Exception) {
            e.printStackTrace()
            throw Exception (getAppContext().resources.getString(R.string.error_in)+
                    " UseCaseExpChZnWithdrawal.makePacking: "+e.message)
        }
    }

    private fun openFile(fileName: String) {
        try {
            file = File(getAppContext().externalCacheDir, File.separator+fileName)
            file.delete()
            if (file.exists()) throw Exception(
                getAppContext().resources.getString(R.string.error_in)+
                        " UseCaseExpChZnWithdrawal.openFile: "+ getAppContext().resources.getString(R.string.error_clear_file)+" - $fileName"
            )
            if (!file.createNewFile()) throw Exception(
                getAppContext().resources.getString(R.string.error_in)+
                        " UseCaseExpChZnWithdrawal.openFile: "+getAppContext().resources.getString(R.string.error_create_file)+" - $fileName"
            )

            fileWriter = FileWriter(file)
        }
        catch (e: Exception) {
            e.printStackTrace()
            throw Exception(getAppContext().resources.getString(R.string.error_in)+
                    " UseCaseExpChZnWithdrawal.openFile: "+e.message)
        }
    }

    private fun closeFile() {
        try {
            if (this::fileWriter.isInitialized) {
                fileWriter.flush()
                fileWriter.close()
            }
        }
        catch (e: Exception) {
            e.printStackTrace()
            throw Exception(getAppContext().resources.getString(R.string.error_in)+
                    " UseCaseExpChZnWithdrawal.closeFile: "+e.message)
        }
    }
}