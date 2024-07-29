package com.countrydelight.cdpreferencehelper.preference_helper

import com.countrydelight.preferencedatastorehelper.PreferenceDataStoreImpl

object PreferenceFunctionHelper {

    suspend fun PreferenceDataStoreImpl.getPreference1(defaultValue: Int): Int {
        return get(PreferenceKeysHelper.preference1Key, defaultValue)
    }

    suspend fun PreferenceDataStoreImpl.setPreference1(updatedValue: Int): Boolean {
        return put(PreferenceKeysHelper.preference1Key, updatedValue)
    }

    suspend fun PreferenceDataStoreImpl.removePreference1(): Boolean {
        return remove(PreferenceKeysHelper.preference1Key)
    }

    suspend fun PreferenceDataStoreImpl.getPreference2(defaultValue: Int): Int {
        return get(PreferenceKeysHelper.preference2Key, defaultValue)
    }

    suspend fun PreferenceDataStoreImpl.setPreference2(updatedValue: Int): Boolean {
        return put(PreferenceKeysHelper.preference2Key, updatedValue)
    }

    suspend fun PreferenceDataStoreImpl.removePreference2(): Boolean {
        return remove(PreferenceKeysHelper.preference2Key)
    }
}