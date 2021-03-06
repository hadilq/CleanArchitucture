/**
 * Copyright 2021 Hadi Lashkari Ghouchani

 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 * http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hadilq.guidomia.singleactivity.impl

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.github.hadilq.commandku.api.CommandExecutor
import com.hadilq.guidomia.core.api.viewBinding
import com.hadilq.guidomia.di.api.SingleActivityScope
import com.hadilq.guidomia.di.api.SingleIn
import com.hadilq.guidomia.featureflags.api.FeatureFlag
import com.hadilq.guidomia.featureflags.api.featureFlag
import com.hadilq.guidomia.guidomia.api.GetGuidomiaNavigatorFactoryCommand
import com.hadilq.guidomia.guidomia.api.GetGuidomiaNavigatorFactoryCommandResult
import com.hadilq.guidomia.guidomia.api.GuidomiaNavigatorFactory
import com.hadilq.guidomia.singleactivity.impl.databinding.ActivityMainBinding
import com.hadilq.guidomia.singleactivity.impl.di.SingleActivityComponent
import com.hadilq.guidomia.singleactivity.impl.di.SingleActivityComponentProvider
import javax.inject.Inject

@SingleIn(SingleActivityScope::class)
class SingleActivity : AppCompatActivity() {

  private val component: SingleActivityComponent by lazy {
    (application as SingleActivityComponentProvider)
      .singleActivityComponentProvider
      .build()
  }

  @Inject
  internal lateinit var executor: CommandExecutor

  private var guidomiaNavigatorFactory: FeatureFlag<GuidomiaNavigatorFactory>? = null

  private val binding by viewBinding { ActivityMainBinding.inflate(layoutInflater) }

  override fun onCreate(savedInstanceState: Bundle?) {
    component.inject(this)
    super.onCreate(savedInstanceState)
    setContentView(binding.root)

    if (savedInstanceState == null) {
      openFirstPossiblePage()
    }
  }

  private fun openFirstPossiblePage() = lifecycleScope.launchWhenCreated {
    when (val flag = fetchFlag()) {
      is FeatureFlag.On -> flag.value.create(this@SingleActivity).commit()
    }
  }

  private suspend fun fetchFlag(): FeatureFlag<GuidomiaNavigatorFactory> =
    guidomiaNavigatorFactory ?: run {
      executor.featureFlag(
        GetGuidomiaNavigatorFactoryCommand(),
        GetGuidomiaNavigatorFactoryCommandResult::class,
      ).to { result }
        .also { guidomiaNavigatorFactory = it }
    }

}
