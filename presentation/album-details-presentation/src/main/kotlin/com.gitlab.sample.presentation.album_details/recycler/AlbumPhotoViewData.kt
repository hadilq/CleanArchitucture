/***
 * Copyright 2018 Hadi Lashkari Ghouchani
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * */
package com.gitlab.sample.presentation.album_details.recycler

import com.gitlab.sample.domain.album_details.entities.AlbumDetailsEntity
import com.gitlab.sample.presentation.album_details.R
import com.gitlab.sample.presentation.common.ViewData

data class AlbumPhotoViewData(val entity: AlbumDetailsEntity): ViewData {
    override fun getType(): Int {
        return R.layout.photo
    }
}