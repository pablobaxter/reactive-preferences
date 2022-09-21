package com.frybits.preferences.rx2

import io.reactivex.Notification
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import org.assertj.core.api.Assertions.assertThat
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

/*
 *  Copyright 2022 Pablo Baxter
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 * Created by Pablo Baxter (Github: pablobaxter)
 * https://github.com/pablobaxter/rx-preferences
 *
 * Re-implementation of https://github.com/f2prateek/rx-preferences/blob/master/rx-preferences/src/test/java/com/f2prateek/rx/preferences2/RecordingObserver.java
 */

/** A test {@link Observer} and JUnit rule which guarantees all events are asserted. */
class RecordingObserver<T : Any> private constructor(): Observer<T> {
    val events = ArrayDeque<Notification<T>>()

    override fun onSubscribe(d: Disposable) {}

    override fun onNext(t: T) {
        events.add(Notification.createOnNext(t))
    }

    override fun onComplete() {
        events.add(Notification.createOnComplete())
    }

    override fun onError(e: Throwable) {
        events.add(Notification.createOnError(e))
    }

    private fun takeNotification(): Notification<T> {
        return events.removeFirst()
    }

    fun takeValue(): T {
        val notification = takeNotification()
        assertThat(notification.isOnNext).isTrue
        return notification.value!!
    }

    fun assertValue(value: T): RecordingObserver<T> {
        assertThat(takeValue()).isEqualTo(value)
        return this
    }

    fun assertNoEvents() {
        assertThat(events).isEmpty()
    }

    class Rule : TestRule {
        private val subscribers = arrayListOf<RecordingObserver<*>>()

        fun <T : Any> create(): RecordingObserver<T> {
            val subscriber = RecordingObserver<T>()
            subscribers.add(subscriber)
            return subscriber
        }

        override fun apply(base: Statement, description: Description): Statement {
            return object : Statement() {
                override fun evaluate() {
                    base.evaluate()
                    subscribers.forEach { it.assertNoEvents() }
                }
            }
        }
    }
}
