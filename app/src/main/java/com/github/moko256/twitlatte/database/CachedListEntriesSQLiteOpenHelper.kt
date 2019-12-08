/*
 * Copyright 2015-2019 The twitlatte authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.moko256.twitlatte.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.github.moko256.latte.client.base.entity.AccessToken
import com.github.moko256.latte.client.base.entity.ListEntry
import com.github.moko256.twitlatte.database.utils.*
import java.io.File

/**
 * Created by moko256 on 2019/01/02.
 *
 * @author moko256
 */

class CachedListEntriesSQLiteOpenHelper : SQLiteOpenHelper {

    constructor(context: Context, accessToken: AccessToken?, userId: Long) : this(
            context,
            if (accessToken != null) {
                File(context.cacheDir, "${accessToken.getKeyString()}/$userId/ListEntries.db").absolutePath
            } else {
                null
            }
    )

    constructor(context: Context, name: String?) : super(context, name, null, 1)

    private companion object {
        private const val TABLE_NAME = "ListEntries"
        private val TABLE_COLUMNS = arrayOf("listId", "title", "description", "isPublic")
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("create table $TABLE_NAME(${TABLE_COLUMNS.joinToString(",")})")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    }

    fun getListEntries(): List<ListEntry> {
        try {
            return read {
                selectMultipleAsList(
                    TABLE_NAME,
                    TABLE_COLUMNS
                ) {
                    ListEntry(
                        getLong(0),
                        getString(1),
                        getString(2),
                        getBoolean(3)
                    )
                }
            }
        } catch (e: Throwable) {
            write {
                delete(TABLE_NAME, null, null)
            }
            return listOf()
        }
    }

    fun setListEntries(listEntries: List<ListEntry>) {
        transaction {
            delete(TABLE_NAME, null, null)

            listEntries.forEach {
                val contentValues = ContentValues(4)
                contentValues.put(TABLE_COLUMNS[0], it.listId)
                contentValues.put(TABLE_COLUMNS[1], it.title)
                contentValues.put(TABLE_COLUMNS[2], it.description)
                contentValues.put(TABLE_COLUMNS[3], it.isPublic)

                insert(TABLE_NAME, null, contentValues)
            }
        }
    }
}
