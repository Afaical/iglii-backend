package com.igli.backend.models

class FertilisationDateModel {

    var womenPseudo: String? = null
    var datesDetails = arrayListOf<DateDetailModel>()

    constructor()

    constructor(womenPseudo: String?, datesDetails: ArrayList<DateDetailModel>) {
        this.womenPseudo = womenPseudo
        this.datesDetails = datesDetails
    }
}