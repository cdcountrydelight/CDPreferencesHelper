package com.countrydelight.cdpreferencehelper.preference_helper

import com.countrydelight.preferencedatastorehelper.PreferenceHelper

object PreferenceFunctionHelper {

    suspend fun PreferenceHelper.getPreference1(defaultValue: Int): Int {
        return get(PreferenceKeysHelper.preference1Key, defaultValue)
    }

    suspend fun PreferenceHelper.setPreference1(updatedValue: Int): Boolean {
        return put(PreferenceKeysHelper.preference1Key, updatedValue)
    }

    suspend fun PreferenceHelper.removePreference1(): Boolean {
        return remove(PreferenceKeysHelper.preference1Key)
    }

    suspend fun PreferenceHelper.getPreference2(defaultValue: Int): Int {
        return get(PreferenceKeysHelper.preference2Key, defaultValue)
    }

    suspend fun PreferenceHelper.setPreference2(updatedValue: Int): Boolean {
        return put(PreferenceKeysHelper.preference2Key, updatedValue)
    }

    suspend fun PreferenceHelper.removePreference2(): Boolean {
        return remove(PreferenceKeysHelper.preference2Key)
    }


    suspend fun PreferenceHelper.setAllPreferencesAtOnce(): Boolean {
        val firstValue = getPreference1(-1)
        val secondValue = getPreference2(-1)
        return putAll(
            PreferenceKeysHelper.preference2Key to firstValue + 1,
            PreferenceKeysHelper.preference1Key to secondValue + 1
        )
    }
}