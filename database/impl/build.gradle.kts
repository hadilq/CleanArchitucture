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
}

configureAndroidLibrary()

android {
  defaultConfig {
    javaCompileOptions {
      annotationProcessorOptions {
        arguments += mapOf(
          "room.schemaLocation" to "$projectDir/schemas",
          "room.incremental" to "true",
          "room.expandProjection" to "true"
        )
      }
    }
  }
}

anvil {
  generateDaggerFactories = true
}

dependencies {
  implementation(project(Modules.corePublic))
  implementation(project(Modules.diPublic))
  implementation(project(Modules.databasePublic))
  implementation(project(Modules.featureFlagsPublic))

  kapt(Depends.roomCompiler)

  implementation(Depends.kotlinStdLib)
  implementation(Depends.dagger)
  implementation(Depends.coroutines)
  implementation(Depends.roomRuntime)
  implementation(Depends.roomKtx)

  testImplementation(Depends.junit)
  androidTestImplementation(Depends.testExtJunit)
  androidTestImplementation(Depends.espressoCore)
}
