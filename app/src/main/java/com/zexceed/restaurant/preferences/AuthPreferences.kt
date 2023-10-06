package com.zexceed.restaurant.preferences

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

class AuthPreferences(private val context: Context) {
    private fun getSharedPreferences(): SharedPreferences {
        return context.getSharedPreferences(AUTH_PREF_NAME, Activity.MODE_PRIVATE)
    }

    fun AuthCustomer(code: String) {
        getSharedPreferences().edit().apply {
            putString(AUTH_PREF_TOKEN, code)
            putString(AUTH_PREF_ROLE, "customer")
            apply()
        }
    }

//    fun AuthStaff(code: String) {
//        getSharedPreferences().edit().apply {
//            putString(AUTH_PREF_TOKEN, code)
//            putString(AUTH_PREF_ROLE, "customer")
//        }
//    }

    fun logout() {
        getSharedPreferences().edit().apply {
            clear()
            apply()
        }
    }

    fun getToken() : String {
        return getSharedPreferences().getString(AUTH_PREF_TOKEN, "").toString()
    }

    fun getRole() : String {
        return getSharedPreferences().getString(AUTH_PREF_ROLE, "").toString()
    }

    fun isAuthenticated() : Boolean {
        return !getSharedPreferences().getString(AUTH_PREF_TOKEN, "").isNullOrEmpty()
    }

    companion object {
        const val AUTH_PREF_NAME = "authPrefs"

        const val AUTH_PREF_TOKEN = "authToken"
        const val AUTH_PREF_ROLE = "authRole"

        const val AUTH_PREF_CUSTOMER = "customer"
        const val AUTH_PREF_STAFF = "staff"
    }

}