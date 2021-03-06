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
package com.hadilq.guidomia.guidomia.impl.data.datasource

import com.github.hadilq.commandku.api.CommandExecutor
import com.hadilq.guidomia.database.api.CarDataEntityCommand
import com.hadilq.guidomia.database.api.GetCarEntityCommand
import com.hadilq.guidomia.database.api.GetCarEntityCommandResult
import com.hadilq.guidomia.featureflags.api.FeatureFlag
import com.hadilq.guidomia.featureflags.api.featureFlag
import com.hadilq.guidomia.guidomia.impl.data.mapper.CarDatabaseMapper
import com.hadilq.guidomia.guidomia.impl.domain.entity.CarEntity
import javax.inject.Inject

class CarDatabaseDataSource @Inject constructor(
  private val executor: CommandExecutor,
  private val mapper: CarDatabaseMapper,
) {

  private var command: FeatureFlag<CarDataEntityCommand>? = null

  suspend fun featureFlag(): Boolean = when (fetchFlag()) {
    is FeatureFlag.On -> true
    is FeatureFlag.Off -> false
  }

  suspend fun isEmpty(): Boolean = when (val flag = fetchFlag()) {
    is FeatureFlag.On -> flag.value.isEmpty()
    is FeatureFlag.Off -> true
  }

  suspend fun fetchCars(): List<CarEntity> = when (val flag = fetchFlag()) {
    is FeatureFlag.On -> flag.value.getAll().map { mapper.map(it) }
    is FeatureFlag.Off -> emptyList()
  }

  suspend fun save(cars: List<CarEntity>) {
    when (val flag = fetchFlag()) {
      is FeatureFlag.On -> flag.value.insertAll(cars.map { mapper.map(it) })
    }
  }

  private suspend fun fetchFlag(): FeatureFlag<CarDataEntityCommand> = command ?: run {
    executor.featureFlag(GetCarEntityCommand(), GetCarEntityCommandResult::class)
      .to { result }
      .also { command = it }
  }
}
