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

package com.github.moko256.twitlatte.cacheMap

import android.content.Context
import com.github.moko256.latte.client.base.entity.AccessToken
import com.github.moko256.latte.client.base.entity.User
import com.github.moko256.twitlatte.LIMIT_OF_SIZE_OF_OBJECT_CACHE
import com.github.moko256.twitlatte.collections.LruCache
import com.github.moko256.twitlatte.database.CachedUsersSQLiteOpenHelper

/**
 * Created by moko256 on 2016/12/22.
 *
 * @author moko256
 */

class UserCacheMap(base: UserCacheMap?, context: Context, accessToken: AccessToken) {

    private val cache: LruCache<Long, User> = base?.cache ?: LruCache(LIMIT_OF_SIZE_OF_OBJECT_CACHE)
    private val diskCache = CachedUsersSQLiteOpenHelper(context, accessToken)

    fun close() {
        diskCache.close()
        cache.clearIfNotEmpty()
    }

    fun add(user: User) {
        cache.put(user.id, user)
        diskCache.addCachedUser(user)
    }

    fun addAll(c: Collection<User>) {
        if (c.isNotEmpty()) {
            c.forEach {
                cache.put(it.id, it)
            }
            diskCache.addCachedUsers(c)
        }
    }

    fun get(id: Long): User? {
        val memoryCache = cache.get(id)
        return if (memoryCache == null) {
            val storageCache = diskCache.getCachedUser(id)
            if (storageCache != null) {
                cache.put(storageCache.id, storageCache)
            }
            storageCache
        } else {
            memoryCache
        }
    }
}