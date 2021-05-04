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

import com.hadilq.guidomia.database.api.CarDataEntityCommand
import com.hadilq.guidomia.guidomia.impl.data.mapper.CarDatabaseMapper
import com.hadilq.guidomia.guidomia.impl.domain.entity.CarEntity
import javax.inject.Inject

class CarDatabaseDataSource @Inject constructor(
  private val command: CarDataEntityCommand,
  private val mapper: CarDatabaseMapper,
) {

  suspend fun isEmpty() = command.isEmpty()

  suspend fun fetchCars(): List<CarEntity> =
    command.getAll().map { mapper.map(it) }

  suspend fun save(cars: List<CarEntity>) {
    command.insertAll(cars.map { mapper.map(it) })
  }
}
