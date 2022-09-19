package com.frybits.preferences.rx2

import io.reactivex.Notification
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import org.assertj.core.api.Assertions.assertThat
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

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
