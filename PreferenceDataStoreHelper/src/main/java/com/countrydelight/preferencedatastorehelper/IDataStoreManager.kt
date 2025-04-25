package com.countrydelight.preferencedatastorehelper

import androidx.datastore.preferences.core.Preferences

/**
 * Interface defining the contract for a DataStore manager.
 * Provides methods to interact with the DataStore for storing, retrieving, and managing preferences.
 */
internal interface IDataStoreManager {

    /**
     * Retrieves a value from the DataStore.
     *
     * @param key The key for the preference.
     * @param defaultValue The default value to return if the preference is not found.
     * @return The value from the DataStore or the default value if not found.
     */
    suspend fun <T> get(key: Preferences.Key<T>, defaultValue: T): T

    /**
     * Saves a value to the DataStore.
     *
     * @param key The key for the preference.
     * @param value The value to save.
     * @return True if the operation was successful, false otherwise.
     */
    suspend fun <T> put(key: Preferences.Key<T>, value: T): Boolean


    /**
     * Saves multiple key-value pairs to the DataStore in a single transaction.
     *
     * @param pairs A vararg of key-value preference pairs to be saved.
     * @return True if the operation was successful, false otherwise.
     */
    suspend fun putAll(vararg pairs: Preferences.Pair<*>): Boolean

    /**
     * Removes a value from the DataStore.
     *
     * @param key The key for the preference.
     * @return True if the operation was successful, false otherwise.
     */
    suspend fun <T> remove(key: Preferences.Key<T>): Boolean

    /**
     * Clears all values from the DataStore.
     *
     * @return True if the operation was successful, false otherwise.
     */
    suspend fun clearAll(): Boolean
}