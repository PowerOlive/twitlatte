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

package com.github.moko256.latte.client.twitter

import com.github.moko256.latte.client.base.entity.User

/**
 * Created by moko256 on 2018/12/01.
 *
 * @author moko256
 */

internal fun twitter4j.User.convertToCommonUser(): User {
    val urls = if (descriptionURLEntities.isNotEmpty()) {
        convertToContentAndLinks(
                description,
                emptyArray(),
                emptyArray(),
                emptyArray(),
                emptyArray(),
                descriptionURLEntities
        )
    } else {
        null
    }

    return User(
            id = id,
            name = name,
            screenName = screenName,
            location = location,
            description = urls?.first ?: description,
            profileImageURLHttps = profileImageURLHttps,
            url = url,
            isProtected = isProtected,
            followersCount = followersCount,
            friendsCount = friendsCount,
            createdAt = createdAt,
            favoritesCount = favouritesCount,
            profileBannerImageUrl = profileBannerURL?.removeSuffix("/web"),
            statusesCount = statusesCount,
            isVerified = isVerified,
            descriptionLinks = urls?.second,
            emojis = null
    )
}