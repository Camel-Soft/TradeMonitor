package com.camelsoft.trademonitor.common

class Constants {
    companion object {
        // Common
        const val DEVELOPER_EMAIL = "development.camelsoft@gmail.com"

        // Безопасность
        const val JWT_ISSUER = "tms"
        const val JWT_JWK_USERS_PUBLIC = "{\"kty\":\"RSA\",\"e\":\"AQAB\",\"use\":\"sig\",\"kid\":\"uf7Fgh7-j3Fk9TG-hc8y7-7327-dhy6-7239-z0jd7rbl720z\",\"n\":\"j4XhRWfviL8jatLDNTB-lInV_WBfLChLcfoB2TJHwaJQ0cnHzzbiWXY94BBV56bRwsR2kwl6Ro9ZgQJMk79kLESau2BNSqSA-rAlInLKWU_npp47Z-BLyim_dOM1dsy6vlPWEF3XuYHk-XFvA_4ebvKFur6MOm9d422a3HGCLwEaPIzSmXBjsmzL6iS-vMPDqT3ouSWBCSoSiiWmG0yD4ktTP7EcIBKRiU5si0Qu5oBMDThODQuQ-vb-b4oZoxoVeOlv5Qyud_ELjkE2wZWmWeLJ05YFVOHWQ9rsA5ocFsnnhXuHXzihs3PaX3VqYRJD5TZloesewTwIuu3I44uSeQ\"}"

        // Actions for Intents
        const val ACTION_BROADCAST_OFFLINE = "ACTION_BROADCAST_OFFLINE"

        // Уведомления
        const val OFFLINE_NOTIFICATION_CHANNEL_ID = "OFFLINE_NOTIFICATION_CHANNEL_ID"
        const val OFFLINE_NOTIFICATION_ID = 0
        const val OFFLINE_PENDING_INTENT_REQUEST_CODE = 0

        // Автонавигация
        const val NAVIGATE_FRAGMENT_KEY = "NAVIGATE_FRAGMENT_KEY"
        const val NAVIGATE_FRAGMENT_VALUE_OFFLINE = "NAVIGATE_FRAGMENT_VALUE_OFFLINE"

        // Имена
        const val OFFL_BASE_FOLDER_NAME = "offlBase"
        const val OFFL_BASE_FOLDER_TMP_NAME = "offlBaseTmp"
        const val OFFL_BASE_ARC_FILE_NAME = "offlbase.zip"
        const val SCAN_DBF = "scan.dbf"
        const val SCAN_NDX = "scan.ndx"
        const val PRICE_DBF = "price.dbf"
        const val PRICE_NDX = "price.ndx"
        const val ARTKL_DBF = "artkl.dbf"
        const val ARTKL_NDX = "artkl.ndx"
        const val GRT_DBF = "grt.dbf"
        const val GRT_NDX = "grt.ndx"
        const val SGRT_DBF = "sgrt.dbf"
        const val SGRT_NDX = "sgrt.ndx"
        const val FIRM_DBF = "firm.dbf"
        const val FIRM_NDX = "firm.ndx"
    }
}
