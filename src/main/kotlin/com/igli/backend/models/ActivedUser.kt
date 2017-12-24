package com.igli.backend.models

class ActivedUser {

    var actived: Boolean = false
    var user: UserModel? = null
    var menPartnerPseudo: String? = null


    constructor()

    constructor(actived: Boolean, user: UserModel?, menPartnerPseudo: String?) {
        this.actived = actived
        this.user = user
        this.menPartnerPseudo = menPartnerPseudo
    }
}