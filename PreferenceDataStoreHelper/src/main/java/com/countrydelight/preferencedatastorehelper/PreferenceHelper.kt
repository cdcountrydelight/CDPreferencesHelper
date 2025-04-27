package com.countrydelight.preferencedatastorehelper

import android.content.Context
import androidx.datastore.core.DataMigration
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.countrydelight.preferencedatastorehelper.utils.PreferenceConstantHelper
import com.countrydelight.preferencedatastorehelper.utils.PreferenceFunctionHelper.preferenceCall
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map


/**
 * Implementation of the IDataStoreManager interface for managing preferences using DataStore.
 */
class PreferenceHelper(
    context: Context,
    preferenceName: String = "${context.applicationContext.packageName}.preferences",
    corruptionHandler: ReplaceFileCorruptionHandler<Preferences>? = null,
    produceMigrations: (Context) -> List<DataMigration<Preferences>> = { listOf() },
    scope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
    onPreferenceErrorOccurred: ((exception: Exception) -> Unit)? = null
) : IDataStoreManager {

    init {
        PreferenceConstantHelper.onPreferenceErrorOccurred = onPreferenceErrorOccurred
    }

    // Define a DataStore property extension for the context
    private val Context.dataStore by preferencesDataStore(
        name = preferenceName,
        corruptionHandler = corruptionHandler,
        produceMigrations = produceMigrations,
        scope = scope
    )

    // Initialize the DataStore instance
    private val dataStore = context.dataStore

    /**
     * Retrieves a value from the DataStore.
     *
     * @param key The key for the preference.
     * @param defaultValue The default value to return if the preference is not found.
     * @return The value from the DataStore or the default value if not found.
     */
    override suspend fun <T> get(key: Preferences.Key<T>, defaultValue: T): T = preferenceCall {
        // Map the DataStore data to the value associated with the key or the default value
        dataStore.data.map {
            it[key] ?: defaultValue
        }.first() // Get the first (and only) element from the flow
    } ?: defaultValue


    /**
     * Saves a value to the DataStore.
     *
     * @param key The key for the preference.
     * @param value The value to save.
     * @return true if the operation was successful, false otherwise.
     */
    override suspend fun <T> put(key: Preferences.Key<T>, value: T): Boolean = preferenceCall {
        // Edit the DataStore to save the value associated with the key
        dataStore.edit { preferences ->
            preferences[key] = value
        }
    } != null  // Return true if the operation was successful, false otherwise


    /**
     * Saves multiple key-value pairs to the DataStore in a single transaction.
     *
     * @param pairs A vararg of key-value preference pairs to be saved.
     * @return True if the operation was successful, false otherwise.
     */
    override suspend fun putAll(vararg pairs: Preferences.Pair<*>): Boolean = preferenceCall {
        // Edit the DataStore to save the values associated with the keys
        dataStore.edit { preferences ->
            preferences.putAll(*pairs)
        }
    } != null


    /**
     * Removes a value from the DataStore.
     *
     * @param key The key for the preference.
     * @return true if the operation was successful, false otherwise.
     */
    override suspend fun <T> remove(key: Preferences.Key<T>): Boolean = preferenceCall {
        // Edit the DataStore to remove the value associated with the key
        dataStore.edit { preferences ->
            preferences.remove(key)
        }
    } != null  // Return true if the operation was successful, false otherwise


    /**
     * Clears all values from the DataStore.
     *
     * @return true if the operation was successful, false otherwise.
     */
    override suspend fun clearAll(): Boolean = preferenceCall {
        // Edit the DataStore to clear all preferences
        dataStore.edit { preferences ->
            preferences.clear()
        }
    } != null  // Return true if the operation was successful, false otherwise


    /**
     * Returns the data store.
     */
    fun getDataStore() = dataStore
}