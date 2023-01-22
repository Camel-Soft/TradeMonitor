package com.camelsoft.trademonitor._data.storage.dbf

import com.camelsoft.trademonitor._domain.api.offl_dbf.ISearchOnPrice
import com.camelsoft.trademonitor._domain.use_cases.use_cases_repository.EventsGoods
import com.camelsoft.trademonitor._presentation.models.MGoodsBig
import org.xBaseJ.DBF
import org.xBaseJ.fields.CharField
import org.xBaseJ.fields.DateField
import org.xBaseJ.fields.LogicalField
import org.xBaseJ.fields.NumField
import java.io.File

class SearchOnPriceDbfImpl : ISearchOnPrice {
    private var dbInit = false
    private lateinit var dbPrice: DBF

    override suspend fun openPrice(priceBaseName: String, priceIndexName: String?): Boolean {
        try {
            val priceBaseFile = File(priceBaseName)
            if (!priceBaseFile.exists() || !priceBaseFile.isFile) {
                dbInit = false
                if (this::dbPrice.isInitialized) dbPrice.close()
                return false
            }
            if (priceIndexName == null) {
                dbInit = false
                if (this::dbPrice.isInitialized) dbPrice.close()
                return false
            }
            else {
                val priceIndexFile = File(priceIndexName)
                if (!priceIndexFile.exists() || !priceIndexFile.isFile) {
                    dbInit = false
                    if (this::dbPrice.isInitialized) dbPrice.close()
                    return false
                }
                dbPrice = DBF(priceBaseName, "IBM866")
                dbPrice.useIndex(priceIndexName)
                dbInit = true
                return true
            }
        }
        catch (e: Exception) {
            e.printStackTrace()
            dbInit = false
            if (this::dbPrice.isInitialized) dbPrice.close()
            return false
        }
    }

    override suspend fun searchPrice(mGoodsBig: MGoodsBig): EventsGoods<MGoodsBig> {
        try {
            if (!dbInit) {
                return EventsGoods.Error("[SearchOnPriceDbfImpl.searchPrice] Подготовка баз данных для поиска не проведена")
            }

            val cod: String = mGoodsBig.cod.trim()
            if (cod.isEmpty()) {
                return EventsGoods.Error("[SearchOnPriceDbfImpl.searchPrice] Код для поиска не определен")
            }

            val result: MGoodsBig = mGoodsBig

            val prcNomer: NumField = dbPrice.getField("PRC_NOMER") as NumField
            val prcDate: DateField = dbPrice.getField("PRC_DATE") as DateField
            val prcTime: CharField = dbPrice.getField("PRC_TIME") as CharField
            val prcType: CharField = dbPrice.getField("PRC_TYPE") as CharField
            val codSklad: CharField = dbPrice.getField("COD_SKL") as CharField
            val codGrt: CharField = dbPrice.getField("COD_GRT") as CharField
            val codSgrt: CharField = dbPrice.getField("COD_SGRT") as CharField
            val name: CharField = dbPrice.getField("NAME") as CharField
            val fullName: CharField = dbPrice.getField("FULLNAME") as CharField
            val edizm: CharField = dbPrice.getField("EDIZM") as CharField
            val inplace: NumField = dbPrice.getField("INPLACE") as NumField
            val made: CharField = dbPrice.getField("MADE") as CharField
            val litera: CharField = dbPrice.getField("LITERA") as CharField
            val modelline: LogicalField = dbPrice.getField("MODELLINE") as LogicalField
            val daySave: NumField = dbPrice.getField("DAY_SAVE") as NumField
            val cenaRozn1: NumField = dbPrice.getField("CENA_ROZN1") as NumField
            val cenaRozn2: NumField = dbPrice.getField("CENA_ROZN2") as NumField
            val cenaRozn3: NumField = dbPrice.getField("CENA_ROZN3") as NumField
            val cenaMrc: NumField = dbPrice.getField("CENA_MRC") as NumField
            val nds: NumField = dbPrice.getField("NDS") as NumField
            val nsp: NumField = dbPrice.getField("NSP") as NumField
            val cenaSpec: NumField = dbPrice.getField("CENA_SPEC") as NumField
            var capacity: NumField? = null
            var codVvp: CharField? = null
            try { capacity = dbPrice.getField("CAPACITY") as NumField } catch (_: Exception) {}
            try { codVvp = dbPrice.getField("COD_VVP") as CharField } catch (_: Exception) {}

            // Поиск
            if (dbPrice.find(cod)) {
                result.prc_number = prcNomer.get().trim()
                result.prc_date = prcDate.get().trim()
                result.prc_time = prcTime.get()
                result.prc_type = prcType.get()
                result.sklad = codSklad.get()
                result.grt = codGrt.get()
                result.sgrt = codSgrt.get()
                result.name = name.get()
                result.name_full = fullName.get()
                result.ed_izm = edizm.get()
                result.in_place = inplace.get().trim()
                result.made = made.get()
                result.litera = litera.get()
                result.modelline = modelline.get()
                result.day_save = daySave.get().trim()
                result.cena1 = cenaRozn1.get().trim()
                result.cena2 = cenaRozn2.get().trim()
                result.cena3 = cenaRozn3.get().trim()
                result.mrc = cenaMrc.get().trim()
                result.nds = nds.get().trim()
                result.nsp = nsp.get().trim()
                result.cena_spec = cenaSpec.get().trim()
                try { result.capacity = capacity?.get()?.trim() ?: "" } catch (e: Exception) { result.capacity = "" }
                try { result.cod_vvp = codVvp?.get() ?: "" } catch (e: Exception) { result.cod_vvp = "" }

                return EventsGoods.Success(data = result)
            }

            return EventsGoods.UnSuccess("Товар по коду не найден")
        }
        catch (e: Exception) {
            e.printStackTrace()
            return EventsGoods.Error("[SearchOnPriceDbfImpl.searchPrice] "+e.localizedMessage)
        }
    }

    override suspend fun closePrice(priceBaseName: String) {
        dbInit = false
        if (this::dbPrice.isInitialized) dbPrice.close()
    }
}
