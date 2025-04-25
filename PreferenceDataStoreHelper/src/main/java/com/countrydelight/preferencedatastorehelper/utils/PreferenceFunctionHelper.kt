package com.countrydelight.preferencedatastorehelper.utils

object PreferenceFunctionHelper {

    /**
     * Executes a suspend function within a try-catch block.
     *
     * @param call The suspend function to execute.
     * @return The result of the function call, or null if an exception occurs.
     */
    suspend fun <T> preferenceCall(call: suspend () -> T): T? {
        return try {
            call()
        } catch (exception: Exception) {
            PreferenceConstantHelper.onPreferenceErrorOccurred?.invoke(exception)
            null
        }
    }
}