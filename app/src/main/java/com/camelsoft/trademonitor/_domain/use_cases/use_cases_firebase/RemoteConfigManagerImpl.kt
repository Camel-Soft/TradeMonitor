package com.camelsoft.trademonitor._domain.use_cases.use_cases_firebase

import com.camelsoft.trademonitor.R
import com.camelsoft.trademonitor._presentation.api.IRemoteConfigFirebase
import com.camelsoft.trademonitor._presentation.models.MStringString
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RemoteConfigManagerImpl: IRemoteConfigFirebase {
    private val gson = Gson()

    companion object {
        private const val CONFIG_CACHE_EXPIRATION_SECONDS = 3600L
    }

    private val firebaseRemoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
    private val firebaseSettings = remoteConfigSettings {
        minimumFetchIntervalInSeconds = CONFIG_CACHE_EXPIRATION_SECONDS
    }

    init {
        firebaseRemoteConfig.setConfigSettingsAsync(firebaseSettings)
        firebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config)
        firebaseRemoteConfig.fetchAndActivate()
            .addOnCompleteListener {

        }
    }


//    private val firebaseRemoteConfig = FirebaseRemoteConfig.getInstance().apply {
//        val firebaseRemoteConfigSettings = FirebaseRemoteConfigSettings.Builder()
//            .setMinimumFetchIntervalInSeconds(CONFIG_CACHE_EXPIRATION_SECONDS)
//            .build()
//        setConfigSettingsAsync(firebaseRemoteConfigSettings)
//        setDefaultsAsync(R.xml.remote_config)
//        fetchAndActivate().addOnCompleteListener {
//        }
//    }

    override suspend fun getInnList(): ArrayList<MStringString> {
        val json = firebaseRemoteConfig.getString(RemoteConfigParam.INN.key)
        val typeToken = object : TypeToken<ArrayList<MStringString>>() {}.type
        return gson.fromJson(json, typeToken)
    }

    override suspend fun getEnabled(): Boolean {
        val json = firebaseRemoteConfig.getString(RemoteConfigParam.ENABLED.key)
        return gson.fromJson(json, Boolean::class.java)
    }
}
