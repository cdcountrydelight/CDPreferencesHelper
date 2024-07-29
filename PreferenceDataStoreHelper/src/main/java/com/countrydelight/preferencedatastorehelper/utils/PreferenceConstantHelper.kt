package com.countrydelight.preferencedatastorehelper.utils

/**
 * Helper object to handle preference-related constants and configurations.
 */
internal object PreferenceConstantHelper {

    /**
     * Function to be called when a preference-related error occurs.
     * This function can be set from outside to handle errors in a custom way.
     */
    var onPreferenceErrorOccurred: ((exception: Exception) -> Unit)? = null

}