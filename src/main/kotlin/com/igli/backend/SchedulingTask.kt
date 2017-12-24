package com.igli.backend

import com.igli.backend.database.FertilisationDateTable
import com.igli.backend.database.FertilisationTextToNotifTable
import com.igli.backend.database.UserTable
import com.igli.backend.database.WomenTable
import com.igli.backend.models.FCMSendModel
import com.igli.backend.models.WomenWillReceiveNotifModel
import org.json.JSONObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.RestController
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.io.OutputStreamWriter
import java.util.*
import java.text.SimpleDateFormat
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

//@CrossOrigin
@RestController
@Component
class SchedulingTask {

    @Autowired
    private lateinit var fertilisationDateTable: FertilisationDateTable
    @Autowired
    private lateinit var womenTable: WomenTable
    @Autowired
    private lateinit var userTable: UserTable
    @Autowired
    private lateinit var fertilisationTextToNotifTable: FertilisationTextToNotifTable

    val calendar = Calendar.getInstance()
    var formatter = SimpleDateFormat("dd/MM/yyyy")

    val AUTH_KEY_FCM = "AAAAcaVWoVg:APA91bE4E6SwHi63yAoH8h5pJFnX_VV2l1oPepO3nWqkYUik8jFPQ74DiyjXAQaUjXeEbgyJ4g57zjjEql5erbxo96G0gmasFJIs2j2rWFOPpUaJgOzJGb3lhNgeg7zSkfrv3EYec_y1"
    val API_URL_FCM = "https://fcm.googleapis.com/fcm/send"
    val userDeviceIdKey = "ciHYA5i5H9Y:APA91bF_cs-h_ooQI4Zb-hOim47RbTbDXvheC2WaaQnhtvIrmhVWQJTjbaxlIgLtgGXhtXNQsUTvwlrS7bDy8D79SdsQDqeTkyK1_xc1KExeeQYYuoEGg5KsSQ9NtgG7AgcxHB_iR132"

//    @Scheduled(cron = "0 0 7 * * *")
    fun getAllActualFertilisationDateFromServer() {
        val womenWillBeReceiveNotification = arrayListOf<WomenWillReceiveNotifModel>()

        //Get actual date: day, month, year
        val actualYear = calendar.get(Calendar.YEAR)
        val actualMonth = (calendar.get(Calendar.MONTH)+1)
        val actualDay = calendar.get(Calendar.DAY_OF_MONTH)

        if (fertilisationDateTable.getAllFertilisationDate().size > 0) {
            for (i in fertilisationDateTable.getAllFertilisationDate()){
                for (j in i.datesDetails) {
                    val mCalendar = Calendar.getInstance()
                     mCalendar.time = formatter.parse(j.date)
                    if ((mCalendar[Calendar.DAY_OF_MONTH] == actualDay)
                            && ((mCalendar[Calendar.MONTH]+1) == actualMonth)
                            && (mCalendar[Calendar.YEAR] == actualYear)) {
                        womenWillBeReceiveNotification.add(WomenWillReceiveNotifModel(i.womenPseudo, j))
                    }
                }
            }
            if (womenWillBeReceiveNotification.size > 0){
                getFertilisationNotifTextAndSendIt(getCouplesWillReceiveFertilisationNotif(womenWillBeReceiveNotification))
            }
        }
    }

    fun getFertilisationNotifTextAndSendIt(couplesWillReceiveNotif: ArrayList<HashMap<String, Any>>) {
        val fcmSendModelList = arrayListOf<FCMSendModel>()
        for (i in couplesWillReceiveNotif){
            val womenPseudo = i["womenPseudo"] as String
            val wFirebaseToken = i["womenMenFirebaseToken"] as String
            val mFirebaseToken = i["menFirebaseToken"] as String
            val wantBePregnant = i["womenWantToBePregnant"] as Boolean
            val isOvulationDate = i["ovulationDate"] as Boolean
            val isDateToStartNewCycle = i["dateToStartNewCycle"] as Boolean

            if (mFirebaseToken.isNotEmpty()){
                val notif = fertilisationTextToNotifTable.getOneFertilisationTextToNotif(wantBePregnant, false, isOvulationDate, isDateToStartNewCycle)
                fcmSendModelList.add(FCMSendModel(mFirebaseToken,
                        "Igli", womenPseudo+" "+notif.notifText,
                        notif.pourcentage, if(isDateToStartNewCycle) "update" else ""))
            }
            val notif = fertilisationTextToNotifTable.getOneFertilisationTextToNotif(wantBePregnant, true, isOvulationDate, isDateToStartNewCycle)
            fcmSendModelList.add(FCMSendModel(wFirebaseToken,
                    "Igli", notif.notifText,
                    notif.pourcentage, if(isDateToStartNewCycle) "update" else ""))
        }

        if (fcmSendModelList.size > 0){
            for (i in fcmSendModelList){
                sendNotification(i.to!!, i.title!!, i.body!!, i.subText!!, i.clickAction!!)
            }
        }
    }

    fun getCouplesWillReceiveFertilisationNotif(list: ArrayList<WomenWillReceiveNotifModel>) : ArrayList<HashMap<String, Any>> {
        val couplesWillReceiveNotif = arrayListOf<HashMap<String, Any>>()
        for (i in list){
            val menPseudo = womenTable.getWomenUserInfo(i.womenPseudo!!).womenMenPseudo
            val womenPseudo = womenTable.getWomenUserInfo(i.womenPseudo!!).womenPseudo
            val womenWantBePregnant = womenTable.getWomenUserInfo(i.womenPseudo!!).womenWantToBePregnant
            val womenMenFirebaseToken = userTable.getUserFirebaseToken(i.womenPseudo!!)
            val menFirebaseToken = userTable.getUserFirebaseToken(menPseudo!!)

            val map = HashMap<String, Any>()
            map.put("womenMenFirebaseToken", womenMenFirebaseToken)
            map.put("womenPseudo", womenPseudo!!)
            map.put("menFirebaseToken", menFirebaseToken  )
            map.put("womenWantToBePregnant", womenWantBePregnant)
            map.put("ovulationDate", i.dateDetail!!.ovulationDate)
            map.put("dateToStartNewCycle", i.dateDetail!!.dateToStartNewCycle)

            couplesWillReceiveNotif.add(map)
        }
        return couplesWillReceiveNotif
    }

    fun sendNotification(userDeviceIdKey: String, title: String, body: String, subText: String, clickAction: String) {
        val authKey = AUTH_KEY_FCM
        val FMCurl = API_URL_FCM

        val url = URL(FMCurl)
        val conn = url.openConnection() as HttpURLConnection

        conn.useCaches = false
        conn.doInput = true
        conn.doOutput = true

        conn.requestMethod = "POST"
        conn.setRequestProperty("Authorization","key="+authKey)
        conn.setRequestProperty("Content-Type","application/json")

        val json = JSONObject()
        json.put("to", userDeviceIdKey.trim())
        val info = JSONObject()
        info.put("title", title) // Notification title
        info.put("body", body) // Notification body
        info.put("click_action", clickAction) // Notification action
        json.put("notification", info)

        val wr = OutputStreamWriter(conn.outputStream)
        wr.write(json.toString())
        wr.flush()

        var status = 0
        if (conn != null){
            status = conn.responseCode
            if (status == 200) {
                val reader = BufferedReader(InputStreamReader(conn.inputStream))
                System.out.println("Android Notification Response : " + reader.readLine())
            }
        }
    }
}