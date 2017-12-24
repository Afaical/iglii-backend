package com.igli.backend.database

import com.igli.backend.models.ActivedUser
import com.igli.backend.models.QueryResponse
import com.igli.backend.models.UserModel
import com.mongodb.BasicDBObject
import com.mongodb.MongoClient
import com.mongodb.MongoClientURI
import org.springframework.stereotype.Component

@Component
class UserTable {


    private var uri = MongoClientURI("mongodb://igliiDBuser:iglii@ds131237.mlab.com:31237/iglii_database")
    private var mongoClient = MongoClient(uri)

    //    mongodb://<dbuser>:<dbpassword>@ds131237.mlab.com:31237/iglii_database
    //    private var mongoClient = MongoClient("localhost")
    private var igliiDatabase = mongoClient.getDB("iglii_database")
    private var userCollection = igliiDatabase.getCollection("UserCollection")

    private var womenTable = WomenTable()

    private fun getMap(user: UserModel) : Map<String, Any>{
        val map = HashMap<String, Any>()
        map.put("userFirstName", user.userFirstName!!)
        map.put("userSurname", user.userSurname!!)
        map.put("userPseudo", user.userPseudo!!)
        map.put("userPassword", user.userPassword!!)
        map.put("userSex", user.userSex!!)
        map.put("userFirebaseToken", user.userFirebaseToken!!)
        return  map
    }

    fun addUser(user: UserModel) : Int {
        if (userExist(user.userPseudo!!)){
            return 300 // Pseudo exist
        } else {
            try {
                userCollection.insert(BasicDBObject(getMap(user)))
                return 200 // User add successful
            } catch (e: Exception){
                return 400 // Failed to add user
            }
        }
    }

    fun getAllUser() : ArrayList<UserModel> {
        val userList = arrayListOf<UserModel>()
        val cursor = userCollection.find()
        cursor.use { cursor ->
            while (cursor.hasNext()) {
                val obj = cursor.next()
                userList.add(UserModel(
                        obj["userFirstName"] as String,
                        obj["userSurname"] as String,
                        obj["userPseudo"] as String,
                        obj["userPassword"] as String,
                        obj["userSex"] as String,
                        obj["userFirebaseToken"] as String))
            }
        }
        return userList
    }

    fun getUserInfo(userPseudo: String, userPassword: String) : ActivedUser {
        var activedUser = ActivedUser()
        for (i in getAllUser()){
            if ((i.userPseudo.equals(userPseudo)) && (i.userPassword.equals(userPassword))){
                activedUser.user = i
                for (j in womenTable.getAllWomenUser()){
                    if (i.userSex.equals("Masculin")){
                        if (j.womenMenPseudo.equals(userPseudo)){
                            activedUser.actived = true
                            activedUser.menPartnerPseudo = j.womenPseudo
                            return activedUser
                        }
                    } else if (i.userSex.equals("Feminin")){
                        if (j.womenPseudo.equals(userPseudo)){
                            activedUser.actived = true
                            return activedUser
                        }
                    }
                }
            }
        }
        return activedUser

//        return getAllUser().firstOrNull {
//            (it.userPseudo.equals(userPseudo)) && (it.userPassword.equals(userPassword))
//        } ?: UserModel()
    }

    fun getUserFirebaseToken(userPseudo: String) : String {
        return getAllUser()
                .firstOrNull { it.userPseudo == userPseudo }
                ?.let { it.userFirebaseToken!! }
                ?: ""
    }

    fun userExist(userPseudo: String) : Boolean {
        return getAllUser().any { it.userPseudo.equals(userPseudo) }
    }

    fun updateUser(user: UserModel) : QueryResponse {
        val newDocument = BasicDBObject(getMap(user))
        val searchQuery = BasicDBObject().append("userPseudo", user.userPseudo)
        try {
            userCollection.update(searchQuery, newDocument)
            return QueryResponse(true)
        } catch (e: Exception){
            return QueryResponse(false)
        }
    }

    fun deleteUser(userPseudo: String) : QueryResponse {
        val query = BasicDBObject()
        query.append("userPseudo", userPseudo)
        try {
            userCollection.remove(query)
            return QueryResponse(true)
        } catch (e: Exception){
            return QueryResponse(false)
        }
    }
}