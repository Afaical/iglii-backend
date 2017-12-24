package com.igli.backend

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.igli.backend.database.*
import com.igli.backend.models.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.util.*
import kotlin.collections.ArrayList

@CrossOrigin
@RestController
class Controller {

    @Autowired
    private lateinit var userTable: UserTable
    @Autowired
    private lateinit var womenTable: WomenTable
    @Autowired
    private lateinit var fertilisationDateTable: FertilisationDateTable
    @Autowired
    private lateinit var fertilisationTextToNotifTable: FertilisationTextToNotifTable
    @Autowired
    private lateinit var adviceTable: AdviceTable

    /**
     *********************************** USER ***********************************
     */
    @RequestMapping("/user/addUser", method = arrayOf(RequestMethod.POST))
    fun addUser(
            @RequestParam(value = "userFirstName", required = true) userFirstName: String,
            @RequestParam(value = "userSurname", required = true) userSurname: String,
            @RequestParam(value = "userPseudo", required = true) userPseudo: String,
            @RequestParam(value = "userPassword", required = true) userPassword: String,
            @RequestParam(value = "userSex", required = true) userSex: String,
            @RequestParam(value = "userFirebaseToken", required = false) userFirebaseToken: String?): Int {
        return userTable.addUser(UserModel(userFirstName, userSurname, userPseudo.toLowerCase(), userPassword, userSex, userFirebaseToken))
    }

    @RequestMapping("/user/getAllUser", method = arrayOf(RequestMethod.GET))
    fun getAllUser() = userTable.getAllUser()

    @RequestMapping("/user/makeConnexion", method = arrayOf(RequestMethod.GET))
    fun makeConnexion(
            @RequestParam(value = "userPseudo", required = true) userPseudo: String,
            @RequestParam(value = "userPassword", required = true) userPassword: String) : Any {
        return userTable.getUserInfo(userPseudo.toLowerCase(), userPassword)
    }

//    @RequestMapping("/user/checkIfUserExist", method = arrayOf(RequestMethod.GET))
//    fun checkIfUserExist(
//            @RequestParam(value = "userPseudo", required = true) userPseudo: String): QueryResponse {
//        return userTable.checkIfUserExist(userPseudo)
//    }

    @RequestMapping("/user/updateUser", method = arrayOf(RequestMethod.PUT))
    fun updateUser(
            @RequestParam(value = "userFirstName", required = true) userFirstName: String,
            @RequestParam(value = "userSurname", required = true) userSurname: String,
            @RequestParam(value = "userPseudo", required = true) userPseudo: String,
            @RequestParam(value = "userPassword", required = true) userPassword: String,
            @RequestParam(value = "userSex", required = true) userSex: String,
            @RequestParam(value = "userFirebaseToken", required = false) userFirebaseToken: String?): QueryResponse {
        return userTable.updateUser(UserModel(userFirstName, userSurname, userPseudo.toLowerCase(), userPassword, userSex, userFirebaseToken))
    }

    @RequestMapping("/user/deleteUser", method = arrayOf(RequestMethod.DELETE))
    fun deleteUser(
            @RequestParam(value = "userPseudo", required = true) userPseudo: String): QueryResponse {
        return userTable.deleteUser(userPseudo.toLowerCase())
    }


    /**
     *********************************** WOMEN ***********************************
     */
    @RequestMapping("/womenUser/addWomenUser", method = arrayOf(RequestMethod.POST))
    fun addWomenUser(
            @RequestParam(value = "womenPseudo", required = true) womenPseudo: String,
            @RequestParam(value = "womenMenPseudo", required = false) womenMenPseudo: String,
            @RequestParam(value = "womenWantToBePregnant", required = true) womenWantToBePregnant: Boolean,
            @RequestParam(value = "womenCycleDuration", required = true) womenCycleDuration: Int,
            @RequestParam(value = "womenDateStartActualCycle", required = true) womenDateStartActualCycle: String): QueryResponse {
        return womenTable.addWomenUser(WomenModel(womenPseudo.toLowerCase(), womenMenPseudo, womenWantToBePregnant, womenCycleDuration, womenDateStartActualCycle))
    }

    @RequestMapping("/womenUser/getAllWomenUser", method = arrayOf(RequestMethod.GET))
    fun getAllWomenUser() = womenTable.getAllWomenUser()

    @RequestMapping("/womenUser/getWomenUserInfo", method = arrayOf(RequestMethod.GET))
    fun getWomenUserInfo(
            @RequestParam(value = "womenPseudo", required = true) womenPseudo: String): WomenModel {
        return womenTable.getWomenUserInfo(womenPseudo.toLowerCase())
    }

    @RequestMapping("/womenUser/menUserPartnerExist", method = arrayOf(RequestMethod.GET))
    fun getPartenerOfMenUser(
            @RequestParam(value = "womenPseudo", required = true) womenPseudo: String): WomenModel {
        return womenTable.menUserPartnerExist(womenPseudo.toLowerCase())
    }

    @RequestMapping("/womenUser/updateWomenUser", method = arrayOf(RequestMethod.PUT))
    fun updateWomenUser(
            @RequestParam(value = "womenPseudo", required = true) womenPseudo: String,
            @RequestParam(value = "womenMenPseudo", required = false) womenMenPseudo: String,
            @RequestParam(value = "womenWantToBePregnant", required = true) womenWantToBePregnant: Boolean,
            @RequestParam(value = "womenCycleDuration", required = true) womenCycleDuration: Int,
            @RequestParam(value = "womenDateStartActualCycle", required = true) womenDateStartActualCycle: String): QueryResponse {
        return womenTable.updateWomenUser(WomenModel(womenPseudo.toLowerCase(), womenMenPseudo.toLowerCase(), womenWantToBePregnant, womenCycleDuration, womenDateStartActualCycle))
    }

    @RequestMapping("/womenUser/deleteWomenUser", method = arrayOf(RequestMethod.DELETE))
    fun deleteWomenUser(
            @RequestParam(value = "womenPseudo", required = true) womenPseudo: String): QueryResponse {
        return womenTable.deleteWomenUser(womenPseudo)
    }


    /**
     *********************************** FERTILISATION ***********************************
     */
    @RequestMapping("/fertilisationDate/addFertilisationDate", method = arrayOf(RequestMethod.POST))
    fun addFertilisationDate(
            @RequestParam(value = "fertilisationDate", required = true) fertilisationDate: String): QueryResponse {
        val gson :Gson = GsonBuilder().create()
        val fertilisationDate2 = gson.fromJson(fertilisationDate, FertilisationDateModel::class.java)
        return fertilisationDateTable.addFertilisationDate(fertilisationDate2)
    }

    @RequestMapping("/fertilisationDate/getAllFertilisationDate", method = arrayOf(RequestMethod.GET))
    fun getAllFertilisationDate() = fertilisationDateTable.getAllFertilisationDate()

    @RequestMapping("/fertilisationDate/getOneFertilisationDateInfo", method = arrayOf(RequestMethod.GET))
    fun getOneFertilisationDateInfo(
            @RequestParam(value = "womenPseudo", required = true) womenPseudo: String): Any {
        return fertilisationDateTable.getOneFertilisationDateInfo(womenPseudo.toLowerCase())
    }

    @RequestMapping("/fertilisationDate/updateFertilisationDate", method = arrayOf(RequestMethod.PUT))
    fun updateFertilisationDate(
            @RequestParam(value = "fertilisationDate", required = true) fertilisationDate: String) : QueryResponse {
        val gson :Gson = GsonBuilder().create()
        val fertilisationDate2 = gson.fromJson(fertilisationDate, FertilisationDateModel::class.java)
        return fertilisationDateTable.updateFertilisationDate(fertilisationDate2)
    }

    @RequestMapping("/fertilisationDate/deleteFertilisationDate", method = arrayOf(RequestMethod.DELETE))
    fun deleteFertilisationDate(
            @RequestParam(value = "womenPseudo", required = true) womenPseudo: String): QueryResponse {
        return fertilisationDateTable.deleteFertilisationDate(womenPseudo.toLowerCase())
    }


    /**
     *********************************** FERTILISATION TEXT TO NOTIF ***********************************
     */
    @RequestMapping("/fertilisationTextToNotif/addFertilisationTextToNotif", method = arrayOf(RequestMethod.POST))
    fun addFertilisationTextToNotif(
            @RequestParam(value = "wantBePregnant", required = true) wantBePregnant: Boolean,
            @RequestParam(value = "isWomen", required = true) isWomen: Boolean,
            @RequestParam(value = "ovulationDate", required = true) isOvulationDate: Boolean,
            @RequestParam(value = "dateToStartNewCycle", required = true) isDateToStartNewCycle: Boolean,
            @RequestParam(value = "notifText", required = true) notifText: String,
            @RequestParam(value = "normalText", required = true) normalText: String,
            @RequestParam(value = "pourcentage", required = true) pourcentage: String): QueryResponse {
        return fertilisationTextToNotifTable
                .addFertilisationTextToNotif(FertilisationTextToNotifModel(
                        Random().nextInt(7122017), wantBePregnant, isWomen, isOvulationDate,
                        isDateToStartNewCycle, notifText, normalText, pourcentage))
    }

    @RequestMapping("/fertilisationTextToNotif/getFertilisationTextToNotif", method = arrayOf(RequestMethod.GET))
    fun getFertilisationTextToNotif() = fertilisationTextToNotifTable.getAllFertilisationTextToNotif()

    @RequestMapping("/fertilisationTextToNotif/getOneFertilisationTextToNotif", method = arrayOf(RequestMethod.GET))
    fun getOneFertilisationTextToNotif(
            @RequestParam(value = "wantBePregnant", required = true) wantBePregnant: Boolean,
            @RequestParam(value = "isWomen", required = true) isWomen: Boolean,
            @RequestParam(value = "ovulationDate", required = true) isOvulationDate: Boolean,
            @RequestParam(value = "dateToStartNewCycle", required = true) isDateToStartNewCycle: Boolean) : FertilisationTextToNotifModel {
        return fertilisationTextToNotifTable.getOneFertilisationTextToNotif(wantBePregnant, isWomen, isOvulationDate, isDateToStartNewCycle)
    }

    @RequestMapping("/fertilisationTextToNotif/updateFertilisationTextToNotif", method = arrayOf(RequestMethod.PUT))
    fun updateFertilisationTextToNotif(
            @RequestParam(value = "id", required = true) id: Int,
            @RequestParam(value = "wantBePregnant", required = true) wantBePregnant: Boolean,
            @RequestParam(value = "isWomen", required = true) isWomen: Boolean,
            @RequestParam(value = "ovulationDate", required = true) isOvulationDate: Boolean,
            @RequestParam(value = "dateToStartNewCycle", required = true) isDateToStartNewCycle: Boolean,
            @RequestParam(value = "notifText", required = true) notifText: String,
            @RequestParam(value = "normalText", required = true) normalText: String,
            @RequestParam(value = "pourcentage", required = true) pourcentage: String) : QueryResponse {
        return fertilisationTextToNotifTable
                .updateFertilisationTextToNotif(FertilisationTextToNotifModel(
                        id, wantBePregnant, isWomen, isOvulationDate,
                        isDateToStartNewCycle, notifText, normalText, pourcentage))
    }

    @RequestMapping("/fertilisationTextToNotif/deleteFertilisationTextToNotif", method = arrayOf(RequestMethod.DELETE))
    fun deleteFertilisationTextToNotif(
            @RequestParam(value = "id", required = true) id: Int): QueryResponse {
        return fertilisationTextToNotifTable.deleteFertilisationTextToNotif(id)
    }


    /**
     *********************************** ADVICE ***********************************
     */
    @RequestMapping("/advice/addAdvice", method = arrayOf(RequestMethod.POST))
    fun addAdvice(
            @RequestParam(value = "adviceWomenWantBePregnant", required = true) adviceWomenWantBePregnant: Boolean,
            @RequestParam(value = "adviceText", required = true) adviceText: String): QueryResponse {
        return adviceTable
                .addAdvice(AdviceModel(Random().nextInt(8122017), adviceWomenWantBePregnant, adviceText))
    }

    @RequestMapping("/advice/getAllAdvice", method = arrayOf(RequestMethod.GET))
    fun getAllAdvice() = adviceTable.getAllAdvice()

    @RequestMapping("/advice/getAllAdviceToBePregnant", method = arrayOf(RequestMethod.GET))
    fun getAllAdviceToBePregnant() = adviceTable.getAllAdviceToBePregnant()

    @RequestMapping("/advice/getAllAdviceToNotBePregnant", method = arrayOf(RequestMethod.GET))
    fun getAllAdviceToNotBePregnant() = adviceTable.getAllAdviceToNotBePregnant()

    @RequestMapping("/advice/updateAdvice", method = arrayOf(RequestMethod.PUT))
    fun updateAdvice(
            @RequestParam(value = "adviceId", required = true) adviceId: Int,
            @RequestParam(value = "adviceWomenWantBePregnant", required = true) adviceWomenWantBePregnant: Boolean,
            @RequestParam(value = "adviceText", required = true) adviceText: String) : QueryResponse {
        return adviceTable
                .updateAdvice(AdviceModel(adviceId, adviceWomenWantBePregnant, adviceText))
    }

    @RequestMapping("/advice/deleteAdvice", method = arrayOf(RequestMethod.DELETE))
    fun deleteAdvice(
            @RequestParam(value = "adviceId", required = true) adviceId: Int): QueryResponse {
        return adviceTable.deleteAdvice(adviceId)
    }
}