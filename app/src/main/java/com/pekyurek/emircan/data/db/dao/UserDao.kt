package com.pekyurek.emircan.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.pekyurek.emircan.data.db.dao.base.BaseDao
import com.pekyurek.emircan.domain.model.response.user.User

@Dao
interface UserDao : BaseDao<User> {

    @Transaction
    @Query("DELETE FROM user")
    fun deleteAllUser()

    @Query("SELECT * FROM user WHERE nickname = :nickname")
    fun getUserByNickname(nickname: String): User?

    @Query("SELECT * FROM user WHERE id = :id")
    fun getUserById(id: String): User?

    @Query("SELECT * FROM user")
    fun getAllUsers(): List<User>

}