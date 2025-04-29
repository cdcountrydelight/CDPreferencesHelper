package com.countrydelight.preferencedatastorehelper

import android.content.Context
import androidx.datastore.core.DataMigration
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.countrydelight.preferencedatastorehelper.utils.PreferenceConstantHelper
import com.countrydelight.preferencedatastorehelper.utils.PreferenceFunctionHelper.preferenceCall
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


open class PreferenceHelper(
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
    override fun getDataStore() = dataStore


    /**
     * Retrieves a value associated with the given key from preferences asynchronously.
     *
     * This method retrieves a value from preferences using the provided key. If the key doesn't exist,
     * it returns the provided default value. The operation is performed asynchronously on the specified
     * coroutine dispatcher and the result is delivered to the main thread using a callback.
     *
     * @param key The key to retrieve the value for.
     * @param defaultValue The value to return if the key is not found in preferences.
     * @param coroutineScope The coroutine dispatcher to use for the asynchronous operation. Defaults to {@link kotlinx.coroutines.Dispatchers.IO}.
     * @param onResponse A lambda function to be called with the retrieved value on the main thread.  This will always be executed, even if an error occurs during retrieval.
     * @param <T> The type of the value associated with the key.
     */
    fun <T> get(
        key: Preferences.Key<T>,
        defaultValue: T,
        coroutineScope: CoroutineDispatcher = Dispatchers.IO,
        onResponse: (T) -> Unit
    ) {
        CoroutineScope(coroutineScope).launch {
            val response = get(key, defaultValue)
            withContext(Dispatchers.Main) {
                onResponse(response)
            }
        }
    }

    /**
     * Asynchronously puts a key-value pair into the preferences.
     *
     * @param key The key to store the value under.  Must be a valid {@link Preferences.Key}.
     * @param value The value to store.  The type must match the type of the provided key.
     * @param coroutineScope The coroutine dispatcher to use for the operation. Defaults to {@link Dispatchers.IO}.
     * @param onComplete A lambda function to be invoked on the main thread after the operation completes.
     *                   It receives a boolean indicating whether the operation was successful.  May be null.
     */
    fun <T> put(
        key: Preferences.Key<T>,
        value: T,
        coroutineScope: CoroutineDispatcher = Dispatchers.IO,
        onComplete: ((isSuccessfull: Boolean) -> Unit)? = null
    ) {
        CoroutineScope(coroutineScope).launch {
            val response = put(key, value)
            withContext(Dispatchers.Main) {
                onComplete?.invoke(response)
            }
        }
    }

    /**
     * Puts all the given key-value pairs into the preferences asynchronously.
     *
     * This method uses coroutines to perform the put operation in the background,
     * preventing blocking the main thread.  The result (success or failure) is
     * then dispatched to the main thread.
     *
     * @param pairs An array of {@link Preferences.Pair} objects representing the key-value pairs to put.
     *              Use the spread operator to pass multiple pairs easily (e.g., `putAll(pair1, pair2, pair3)`).
     * @param coroutineScope The {@link CoroutineDispatcher} to use for the asynchronous operation. Defaults to {@link Dispatchers.IO}.
     * @param onComplete A lambda function that is invoked on the main thread after the operation completes.
     *                   It receives a boolean indicating whether the operation was successful.  Can be null.
     */
    fun <T> putAll(
        vararg pairs: Preferences.Pair<*>,
        coroutineScope: CoroutineDispatcher = Dispatchers.IO,
        onComplete: ((isSuccessfull: Boolean) -> Unit)? = null
    ) {
        CoroutineScope(coroutineScope).launch {
            val response = putAll(*pairs)
            withContext(Dispatchers.Main) {
                onComplete?.invoke(response)
            }
        }
    }

    /**
     * Removes a preference value associated with the given key. This operation is performed asynchronously.
     *
     * @param key The [Preferences.Key] identifying the preference to remove.
     * @param coroutineScope The [CoroutineDispatcher] to use for the removal operation. Defaults to `Dispatchers.IO`.
     * @param onComplete A lambda function to be invoked on the main thread after the removal operation completes.
     *                   It receives a boolean indicating whether the removal was successful.  Can be null.
     */
    fun <T> remove(
        key: Preferences.Key<T>,
        coroutineScope: CoroutineDispatcher = Dispatchers.IO,
        onComplete: ((isSuccessfull: Boolean) -> Unit)? = null
    ) {
        CoroutineScope(coroutineScope).launch {
            val response = remove(key)
            withContext(Dispatchers.Main) {
                onComplete?.invoke(response)
            }
        }
    }

    /**
     * Clears all data asynchronously.
     *
     * This method performs a clearing operation on a background thread and then invokes the provided
     * callback on the main thread with the result.  If no callback is provided, the result is silently discarded.
     *
     * @param coroutineScope The [CoroutineDispatcher] to use for the background operation. Defaults to `Dispatchers.IO`.
     * @param onComplete A callback function that is invoked on the main thread after the operation completes.
     *                   The boolean parameter indicates whether the operation was successful.  Can be null.
     */
    fun clearAll(
        coroutineScope: CoroutineDispatcher = Dispatchers.IO,
        onComplete: ((isSuccessfull: Boolean) -> Unit)? = null
    ) {
        CoroutineScope(coroutineScope).launch {
            val response = clearAll()
            withContext(Dispatchers.Main) {
                onComplete?.invoke(response)
            }
        }
    }
}