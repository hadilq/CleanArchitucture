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
  kotlin("jvm")
  kotlin("kapt")
  id("com.squareup.anvil") version Versions.anvil
}

tasks.test {
  useJUnitPlatform()
}

anvil {
  generateDaggerFactories = true
}

dependencies {
  implementation(project(Modules.featureFlagsPublic))
  implementation(project(Modules.diPublic))

  implementation(Depends.dagger)
  implementation(Depends.kotlinStdLib)
  implementation(Depends.coroutines)

  testImplementation(Depends.kotlinTest)
  testImplementation(Depends.junitJupiterApi)
  testRuntimeOnly(Depends.junitJupiterEngine)
  testImplementation(Depends.mockk)
}