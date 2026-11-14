package com.example.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.data.model.CustomerProfile
import com.example.ui.i18n.AppLanguage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "gramkart_prefs")

class UserPreferencesRepository(private val context: Context) {

    private object Keys {
        val LANGUAGE = stringPreferencesKey("app_language")
        val CUSTOMER_NAME = stringPreferencesKey("customer_name")
        val CUSTOMER_PHONE = stringPreferencesKey("customer_phone")
        val CUSTOMER_ADDRESS = stringPreferencesKey("customer_address")
        val IS_ADMIN_LOGGED_IN = booleanPreferencesKey("is_admin_logged_in")
        val IS_SEEDED = booleanPreferencesKey("is_demo_seeded")
    }

    val languageFlow: Flow<AppLanguage> = context.dataStore.data.map { prefs ->
        if (prefs[Keys.LANGUAGE] == AppLanguage.HI.code) AppLanguage.HI else AppLanguage.EN
    }

    val customerProfileFlow: Flow<CustomerProfile> = context.dataStore.data.map { prefs ->
        val defaultProfile = CustomerProfile()
        CustomerProfile(
            name = prefs[Keys.CUSTOMER_NAME] ?: defaultProfile.name,
            phone = prefs[Keys.CUSTOMER_PHONE] ?: defaultProfile.phone,
            address = prefs[Keys.CUSTOMER_ADDRESS] ?: defaultProfile.address
        )
    }

    val isAdminLoggedInFlow: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[Keys.IS_ADMIN_LOGGED_IN] ?: false
    }

    suspend fun isSeeded(): Boolean {
        return context.dataStore.data.first()[Keys.IS_SEEDED] ?: false
    }

    suspend fun setSeeded(seeded: Boolean) {
        context.dataStore.edit { it[Keys.IS_SEEDED] = seeded }
    }

    suspend fun setLanguage(language: AppLanguage) {
        context.dataStore.edit { it[Keys.LANGUAGE] = language.code }
    }

    suspend fun updateCustomerProfile(profile: CustomerProfile) {
        context.dataStore.edit { prefs ->
            prefs[Keys.CUSTOMER_NAME] = profile.name
            prefs[Keys.CUSTOMER_PHONE] = profile.phone
            prefs[Keys.CUSTOMER_ADDRESS] = profile.address
        }
    }

    suspend fun setAdminLoggedIn(loggedIn: Boolean) {
        context.dataStore.edit { it[Keys.IS_ADMIN_LOGGED_IN] = loggedIn }
    }
}
