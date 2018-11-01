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
package com.gitlab.sample.cleanarchitecture

import android.support.v4.app.FragmentManager
import com.gitlab.sample.presentation.common.BaseFragmentFactory
import com.gitlab.sample.presentation.common.FragmentType
import com.gitlab.sample.presentation.common.Navigator
import javax.inject.Inject

class MainNavigator @Inject constructor() : Navigator {
    @Inject
    lateinit var factory: BaseFragmentFactory

    override fun launchFragment(fm: FragmentManager) {
        factory.commitFragment(fm.beginTransaction(), factory.create(FragmentType.ALBUMS))
    }
}