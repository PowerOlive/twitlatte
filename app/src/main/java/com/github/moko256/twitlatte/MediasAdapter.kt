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

package com.github.moko256.twitlatte

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.github.moko256.latte.client.base.entity.Media
import com.github.moko256.twitlatte.mediaview.*

/**
 * Created by moko256 on 2016/10/29.
 *
 * @author moko256
 */
class MediasAdapter(
        fm: FragmentManager,
        private val medias: List<Media>,
        private val type: Int
) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        val media = medias[position]

        return when (media.mediaType) {
            "video_multi" -> MultiVideoFragment()
            "video_one" -> OneVideoFragment()
            "audio" -> AudioFragment()
            "gif" -> GifFragment()
            "picture" -> ImageFragment()
            else -> UnknownMediaFragment()
        }.apply {
            setMediaToArg(media, type)
        }

    }

    override fun getCount(): Int {
        return medias.size
    }
}