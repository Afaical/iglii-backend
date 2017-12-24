package com.igli.backend.models

import java.util.*

class WomenModel {

    var womenPseudo: String? = null
    var womenMenPseudo: String? = null
    var womenWantToBePregnant: Boolean = false
    var womenCycleDuration: Int? = null
    var womenDateStartActualCycle: String? = null

    constructor()

    constructor(womenPseudo: String?, womenMenPseudo: String?, womenWantToBePregnant: Boolean, womenCycleDuration: Int?, womenDateStartActualCycle: String?) {
        this.womenPseudo = womenPseudo
        this.womenMenPseudo = womenMenPseudo
        this.womenWantToBePregnant = womenWantToBePregnant
        this.womenCycleDuration = womenCycleDuration
        this.womenDateStartActualCycle = womenDateStartActualCycle
    }
}