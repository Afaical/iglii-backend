package com.igli.backend.models

class UserModel {

    var userFirstName: String? = null
    var userSurname: String? = null
    var userPseudo: String? = null
    var userPassword: String? = null
    var userSex: String? = null
    var userFirebaseToken: String? = null

    constructor()

    constructor(userFirstName: String?, userSurname: String?, userPseudo: String?, userPassword: String?, userSex: String?, userFirebaseToken: String?) {
        this.userFirstName = userFirstName
        this.userSurname = userSurname
        this.userPseudo = userPseudo
        this.userPassword = userPassword
        this.userSex = userSex
        this.userFirebaseToken = userFirebaseToken
    }

    constructor(userFirstName: String?, userSurname: String?, userPseudo: String?, userPassword: String?, userSex: String?) {
        this.userFirstName = userFirstName
        this.userSurname = userSurname
        this.userPseudo = userPseudo
        this.userPassword = userPassword
        this.userSex = userSex
    }
}