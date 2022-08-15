package com.del.ta_10.util

import android.content.Context
import android.content.SharedPreferences
import com.del.ta_10.data.response.AuthResponse
import com.del.ta_10.data.response.DataAuth
import com.google.gson.Gson
import org.koin.ext.scope

class SharedPrefLogin(mContext: Context) {
    private val sp: SharedPreferences = mContext.getSharedPreferences(mypref, Context.MODE_PRIVATE)

    fun setStatusLogin(status : Boolean){
        sp.edit().putBoolean(login, status).apply()
    }

    fun setUser(user: String){
        sp.edit().putString(users, user).apply()
    }

    fun getStatusLogin() : Boolean{
        return sp.getBoolean(login, false)
    }

    fun getUser(): DataAuth{
        return Gson().fromJson(sp.getString(users,null), DataAuth::class.java)
    }

    fun logout(): Boolean{
        sp.edit().putBoolean(login, false).apply()
        sp.edit().putString(users, "").apply()
        return  true;
    }

    fun resetDataUser(newUser: String){
        sp.edit().putString(users, "").apply()
        sp.edit().putString(users, newUser).apply()
    }


    companion object{
        const val login = "session_status"
        const val id = "id"
        const val users = "user"
        const val email = "username"
        private const val mypref = "MAIN_PRF"
    }

}