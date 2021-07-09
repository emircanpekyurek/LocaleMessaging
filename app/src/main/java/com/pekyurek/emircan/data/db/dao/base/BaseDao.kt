package com.pekyurek.emircan.data.db.dao.base

import androidx.room.*

interface BaseDao<T> {

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(data: T)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(data: List<T>)

    @Update
    fun update(data: T)

    @Delete
    fun delete(data: T)

    @Transaction
    @Delete
    fun delete(data: List<T>)

}