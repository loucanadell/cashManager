package com.msc.cashmanager.model

class AuthSession {
    companion object {
        var userId :String? = ""
        var accessToken :String = ""
        var billAmount = ""
        var idCart = ""
        var user = User("", "", "", "", "")
        lateinit var instance :AuthSession
        fun getAuthInfo():AuthSession {
            if (instance == null) {
                instance = AuthSession()
            }
            return instance
        }
    }
}