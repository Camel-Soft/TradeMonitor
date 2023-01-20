package com.camelsoft.trademonitor.common

class Constants {
    companion object {
        const val DEVELOPER_EMAIL = "development.camelsoft@gmail.com"

        const val JWT_ISSUER = "tms"
        const val JWT_JWK_USERS_PUBLIC = "{\"kty\":\"RSA\",\"e\":\"AQAB\",\"use\":\"sig\",\"kid\":\"uf7Fgh7-j3Fk9TG-hc8y7-7327-dhy6-7239-z0jd7rbl720z\",\"n\":\"j4XhRWfviL8jatLDNTB-lInV_WBfLChLcfoB2TJHwaJQ0cnHzzbiWXY94BBV56bRwsR2kwl6Ro9ZgQJMk79kLESau2BNSqSA-rAlInLKWU_npp47Z-BLyim_dOM1dsy6vlPWEF3XuYHk-XFvA_4ebvKFur6MOm9d422a3HGCLwEaPIzSmXBjsmzL6iS-vMPDqT3ouSWBCSoSiiWmG0yD4ktTP7EcIBKRiU5si0Qu5oBMDThODQuQ-vb-b4oZoxoVeOlv5Qyud_ELjkE2wZWmWeLJ05YFVOHWQ9rsA5ocFsnnhXuHXzihs3PaX3VqYRJD5TZloesewTwIuu3I44uSeQ\"}"

        const val ACTION_BROADCAST_OFFLINE = "ACTION_BROADCAST_OFFLINE"

        const val OFFLINE_NOTIFICATION_CHANNEL_ID = "OFFLINE_NOTIFICATION_CHANNEL_ID"
        const val OFFLINE_NOTIFICATION_ID = 0
        const val OFFLINE_PENDING_INTENT_REQUEST_CODE = 0

        const val NAVIGATE_FRAGMENT_KEY = "NAVIGATE_FRAGMENT_KEY"
        const val NAVIGATE_FRAGMENT_VALUE_OFFLINE = "NAVIGATE_FRAGMENT_VALUE_OFFLINE"
    }
}
