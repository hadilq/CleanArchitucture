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
package com.hadilq.guidomia.database.impl.di

import android.content.Context
import androidx.room.Room
import com.hadilq.guidomia.database.impl.AppDatabase
import com.hadilq.guidomia.database.impl.CarDao
import com.hadilq.guidomia.di.api.AppScope
import com.hadilq.guidomia.di.api.SingleIn
import com.squareup.anvil.annotations.ContributesTo
import dagger.Module
import dagger.Provides

@[Module ContributesTo(AppScope::class)]
object DatabaseModule {

  @[Provides SingleIn(AppScope::class)]
  fun provideDatabase(context: Context): AppDatabase = Room.databaseBuilder(
    context.applicationContext,
    AppDatabase::class.java, "car-database"
  ).build()

  @[Provides SingleIn(AppScope::class)]
  fun provideCarDao(database: AppDatabase): CarDao = database.carDao()
}
