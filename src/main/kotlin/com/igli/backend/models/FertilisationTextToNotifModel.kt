package com.igli.backend.models

class FertilisationTextToNotifModel {

    var id: Int? = null
    var wantBePregnant: Boolean = false
    var isWomen: Boolean = false
    var isOvulationDate: Boolean = false
    var isDateToStartNewCycle: Boolean = false
    var notifText: String? = null
    var normalText: String? = null
    var pourcentage: String? = null

    constructor()

    constructor(id: Int?, wantBePregnant: Boolean, isWomen: Boolean, isOvulationDate: Boolean, isDateToStartNewCycle: Boolean, notifText: String?, normalText: String?, pourcentage: String?) {
        this.id = id
        this.wantBePregnant = wantBePregnant
        this.isWomen = isWomen
        this.isOvulationDate = isOvulationDate
        this.isDateToStartNewCycle = isDateToStartNewCycle
        this.notifText = notifText
        this.normalText = normalText
        this.pourcentage = pourcentage
    }
}