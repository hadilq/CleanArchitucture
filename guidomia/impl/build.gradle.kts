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

plugins {
  id("com.android.library")
  kotlin("android")
  kotlin("kapt")
  id("com.squareup.anvil") version Versions.anvil
  kotlin("plugin.serialization") version Versions.serialization
  id("de.mannodermaus.android-junit5")
}

configureAndroidLibrary()

android {
  buildFeatures {
    viewBinding = true
  }
}

anvil {
  generateDaggerFactories = true
}

dependencies {
  implementation(project(Modules.corePublic))
  implementation(project(Modules.diPublic))
  implementation(project(Modules.singleActivityPublic))
  implementation(project(Modules.guidomiaPublic))
  implementation(project(Modules.databasePublic))
  implementation(project(Modules.featureFlagsPublic))

  implementation(Depends.kotlinStdLib)
  implementation(Depends.coreKtx)
  implementation(Depends.appCompat)
  implementation(Depends.material)
  implementation(Depends.constraintLayout)
  implementation(Depends.fragment)
  implementation(Depends.dagger)
  implementation(Depends.viewModel)
  implementation(Depends.recyclerView)
  implementation(Depends.coroutines)
  implementation(Depends.jsonSerialization)

  testImplementation(Depends.junitJupiterApi)
  testRuntimeOnly(Depends.junitJupiterEngine)
  testImplementation(Depends.mockk)
  androidTestImplementation(Depends.testExtJunit)
  androidTestImplementation(Depends.espressoCore)
}
