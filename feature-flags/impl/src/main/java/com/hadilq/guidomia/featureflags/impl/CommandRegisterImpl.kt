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
package com.hadilq.guidomia.featureflags.impl

import com.hadilq.guidomia.di.api.AppScope
import com.hadilq.guidomia.di.api.SingleIn
import com.hadilq.guidomia.featureflags.api.*
import com.squareup.anvil.annotations.ContributesBinding
import javax.inject.Inject
import kotlin.reflect.KClass

@ContributesBinding(AppScope::class)
@SingleIn(AppScope::class)
class CommandRegisterImpl @Inject constructor(
  private val operation: CommandOperation,
) : CommandRegister by operation

@ContributesBinding(AppScope::class)
@SingleIn(AppScope::class)
class CommandResultRegisterImpl @Inject constructor(
  private val operation: CommandOperation,
) : CommandResultRegister by operation

@ContributesBinding(AppScope::class)
@SingleIn(AppScope::class)
class CommandShooterImpl @Inject constructor(
  private val operation: CommandOperation,
) : CommandShooter by operation

@ContributesBinding(AppScope::class)
@SingleIn(AppScope::class)
class CommandResultShooterImpl @Inject constructor(
  private val operation: CommandOperation,
) : CommandResultShooter by operation

@SingleIn(AppScope::class)
class CommandOperation @Inject constructor() : CommandRegister, CommandResultRegister,
  CommandShooter, CommandResultShooter {

  private val store = mutableMapOf<KClass<Command>, MutableSet<Cmd>>()

  @Suppress("UNCHECKED_CAST", "NAME_SHADOWING")
  override fun <C : Command> register(
    commandClass: KClass<C>,
    callback: CommandCallback<C>,
  ): Registration {
    val commandClass = commandClass as KClass<Command>
    val element = RequestCmd(callback)
    store[commandClass] = store[commandClass]?.apply { add(element) } ?: mutableSetOf(element)
    return RegistrationImpl(this, callback)
  }

  @Suppress("UNCHECKED_CAST", "NAME_SHADOWING")
  override fun <C : Command> register(
    commandClass: KClass<C>,
    key: CommandKey,
    callback: CommandResultCallback<C>
  ) {
    val commandClass = commandClass as KClass<Command>
    val element = ResultCmd(key, callback)
    store[commandClass] = store[commandClass]?.apply { add(element) } ?: mutableSetOf(element)
  }

  fun <C : Command> dispose(callback: CommandCallback<C>) {
    store.values.forEach { set ->
      set.firstOrNull { cmd ->
        when (cmd) {
          is RequestCmd<*> -> {
            cmd.callback === callback
          }
          else -> false
        }
      }?.also {
        set.remove(it)
        return
      }
    }
  }

  private fun <C : Command> disposeResult(callback: CommandResultCallback<C>) {
    store.values.forEach { set ->
      set.firstOrNull { cmd ->
        when (cmd) {
          is ResultCmd<*> -> {
            cmd.callback === callback
          }
          else -> false
        }
      }?.also {
        set.remove(it)
        return
      }
    }
  }

  @Suppress("UNCHECKED_CAST", "TYPE_INFERENCE_ONLY_INPUT_TYPES_WARNING")
  override suspend fun <C : Command> shoot(commandBall: CommandBall<C>): Boolean {
    var found = false
    store[commandBall.commandClass]?.forEach { cmd ->
      if (cmd is RequestCmd<*>) {
        (cmd.callback as CommandCallback<C>).invoke(commandBall)
        found = true
      }
    }
    return found
  }

  @Suppress("UNCHECKED_CAST", "TYPE_INFERENCE_ONLY_INPUT_TYPES_WARNING")
  override suspend fun <C : Command> shoot(commandBall: CommandResultBall<C>) {
    store[commandBall.commandClass]?.forEach { cmd ->
      if (cmd is ResultCmd<*> && cmd.key == commandBall.key) {
        (cmd.callback as CommandResultCallback<C>).invoke(commandBall)
        disposeResult(cmd.callback)
      }
    }
  }
}

private sealed class Cmd

private class RequestCmd<C : Command>(
  val callback: CommandCallback<C>,
) : Cmd()

private class ResultCmd<C : Command>(
  val key: CommandKey,
  val callback: CommandResultCallback<C>,
) : Cmd()

private class RegistrationImpl<C : Command>(
  private val disposer: CommandOperation,
  private val callback: CommandCallback<C>,
) : Registration {

  override fun dispose() {
    disposer.dispose(callback)
  }
}
