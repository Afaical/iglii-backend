package com.igli.backend.models

class FertilisationInfoModel {

    var notifText: String? = null
    var pourcentage: String? = null

    constructor()

    constructor(notifText: String?, pourcentage: String?) {
        this.notifText = notifText
        this.pourcentage = pourcentage
    }
}