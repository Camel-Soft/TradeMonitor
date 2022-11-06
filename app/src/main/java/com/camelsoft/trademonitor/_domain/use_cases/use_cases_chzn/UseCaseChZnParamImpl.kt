package com.camelsoft.trademonitor._domain.use_cases.use_cases_chzn

import com.camelsoft.trademonitor._presentation.api.IChZnParam
import com.camelsoft.trademonitor._presentation.models.secondary.MStringString

class UseCaseChZnParamImpl: IChZnParam {
    override suspend fun getInnList(): ArrayList<MStringString> {
        val result = ArrayList<MStringString>()
        result.add(MStringString("6345029001", "ООО Пульсар"))
        result.add(MStringString("6322033717", "ООО ТД Миндаль"))
        result.add(MStringString("6321075658", "ООО Неотрейд"))
        result.add(MStringString("6382072421", "ООО Рубин"))
        result.add(MStringString("6321395954", "ООО Новый Калачик"))
        result.add(MStringString("6321218803", "ООО Комплект-С"))
        result.add(MStringString("6320044008", "ООО ОДАЙБА"))
        result.add(MStringString("6321441294", "ООО Здоровая Семья"))
        result.add(MStringString("6345029971", "ООО С-ТОРГ"))
        result.add(MStringString("632103950772", "ИП Соломатина Н.И."))
        result.add(MStringString("634500232108", "ИП Можаров В.И."))
        result.add(MStringString("632135557099", "ИП Мухиева Р. Р."))
        result.add(MStringString("632101020595", "ИП Воронин А. П."))
        result.add(MStringString("632111302359", "ИП Груздев А. Л."))
        return result
    }
}