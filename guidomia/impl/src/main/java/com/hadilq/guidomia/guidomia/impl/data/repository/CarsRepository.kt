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
package com.hadilq.guidomia.guidomia.impl.data.repository

import com.hadilq.guidomia.guidomia.impl.data.datasource.CarDatabaseDataSource
import com.hadilq.guidomia.guidomia.impl.data.datasource.CarsCacheDataSource
import com.hadilq.guidomia.guidomia.impl.data.datasource.CarsDataSource
import com.hadilq.guidomia.guidomia.impl.domain.entity.CarEntity
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class CarsRepository @Inject constructor(
  private val carsDataSource: CarsDataSource,
  private val cacheDataSource: CarsCacheDataSource,
  private val carDatabaseDataSource: CarDatabaseDataSource
) {

  fun getCars(): Flow<List<CarEntity>> =
    if (cacheDataSource.caching.isEmpty()) {
      flow {
        if (carDatabaseDataSource.isEmpty()) {
          emitAll(carsDataSource.fetchCars()
            .onEach { carDatabaseDataSource.save(it) }
            .onEach { cacheDataSource.caching = it }
          )
        } else {
          emit(carDatabaseDataSource.fetchCars())
        }
      }
    } else {
      flowOf(cacheDataSource.caching)
    }
}
