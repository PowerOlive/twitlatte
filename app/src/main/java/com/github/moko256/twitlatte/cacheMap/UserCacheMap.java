/*
 * Copyright 2015-2018 The twitlatte authors
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

package com.github.moko256.twitlatte.cacheMap;

import android.content.Context;

import com.github.moko256.twitlatte.GlobalApplication;
import com.github.moko256.twitlatte.database.CachedUsersSQLiteOpenHelper;
import com.github.moko256.twitlatte.entity.AccessToken;
import com.github.moko256.twitlatte.entity.User;

import java.util.ArrayList;
import java.util.Collection;

import androidx.collection.LruCache;

/**
 * Created by moko256 on 2016/12/22.
 *
 * @author moko256
 */

public class UserCacheMap {

    private CachedUsersSQLiteOpenHelper diskCache;
    private final LruCache<Long, User> cache=new LruCache<>(GlobalApplication.statusCacheListLimit / 4);

    public void prepare(Context context, AccessToken accessToken){
        if (diskCache != null){
            diskCache.close();
        }
        if (cache.size() > 0){
            cache.evictAll();
        }
        diskCache = new CachedUsersSQLiteOpenHelper(context,accessToken);
    }

    public int size() {
        return cache.size();
    }

    public void add(User user) {
        if (user != null) {
            cache.put(user.getId(), user);
            diskCache.addCachedUser(user);
        }
    }

    public void addAll(Collection<User> c) {
        if (c.size() > 0) {
            ArrayList<User> list = new ArrayList<>(c.size());
            for (User user : c) {
                if (user != null) {
                    cache.put(user.getId(), user);
                    list.add(user);
                }
            }
            diskCache.addCachedUsers(list);
        }
    }

    public User get(long id) {
        User memoryCache = cache.get(id);
        if (memoryCache == null){
            User storageCache = diskCache.getCachedUser(id);
            if (storageCache != null){
                cache.put(storageCache.getId(), storageCache);
            }
            return storageCache;
        } else {
            return memoryCache;
        }
    }
}