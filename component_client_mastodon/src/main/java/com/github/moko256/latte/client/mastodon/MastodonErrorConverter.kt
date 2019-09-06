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

package com.github.moko256.latte.client.mastodon

import com.github.moko256.latte.client.mastodon.gson.gson
import com.google.gson.JsonParseException
import com.google.gson.annotations.SerializedName
import com.sys1yagi.mastodon4j.MastodonRequest
import com.sys1yagi.mastodon4j.api.exception.Mastodon4jRequestException

/**
 * Created by moko256 on 2018/12/08.
 *
 * @author moko256
 */

internal fun <T : Any> MastodonRequest<T>.executeAndConvertError(): T {
    try {
        return execute()
    } catch (e: Mastodon4jRequestException) {
        throw Exception(
                e.response?.use { response ->

                    response.body()?.use { body ->
                        try {
                            gson.fromJson(body.charStream(), Error::class.java).error
                        } catch (e: JsonParseException) {
                            body.string()
                        }
                    } ?: response.message()

                } ?: e.toString(), e
        )
    }
}

internal data class Error(
        @SerializedName("error") val error: String
)