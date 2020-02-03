package dev.matrix.corx

import io.reactivex.Observable
import io.reactivex.subjects.ReplaySubject
import kotlinx.coroutines.channels.toList
import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.junit.Assert.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 * @author Rostyslav.Lesovyi
 */
@Suppress("BlockingMethodInNonBlockingContext", "EXPERIMENTAL_API_USAGE")
class Rx2ChannelKtTest {

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Channel
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Test
    fun toChannel_positive() = runBlocking {
        val disposeLock = CountDownLatch(1)

        val values = listOf(1, 2, 3)
        val subject = Observable.fromIterable(values)
            .doFinally { disposeLock.countDown() }

        assertEquals(values, subject.toChannel(10).toList())
        assertTrue(disposeLock.await(1, TimeUnit.SECONDS))
    }

    @Test
    fun toChannel_error() = runBlocking {
        val exception = Exception("YAY!")
        val subject = Observable.error<Int>(exception)

        try {
            subject.toChannel(10).toList()
            throw AssertionError()
        } catch (e: Exception) {
            assertEquals(e.message, exception.message)
        }
    }

    @Test
    fun toChannel_cancel() = runBlocking {
        val disposeLock = CountDownLatch(1)

        val subject = Observable.never<Int>()
            .doFinally { disposeLock.countDown() }

        subject.toChannel(10).cancel()
        assertTrue(disposeLock.await(1, TimeUnit.SECONDS))
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // BroadcastChannel
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Test
    fun toBroadcastChannel_positive() = runBlocking {
        val disposeLock = CountDownLatch(1)

        val subject = ReplaySubject.create<Int>()
        val broadcast = subject
            .doFinally { disposeLock.countDown() }
            .toBroadcastChannel(10)

        val channel = broadcast.openSubscription()
        val values = listOf(1, 2, 3)
        for (value in values) {
            subject.onNext(value)
        }
        subject.onComplete()

        assertEquals(values, channel.toList())

        broadcast.close()
        assertTrue(disposeLock.await(1, TimeUnit.SECONDS))
    }

    @Test
    fun toBroadcastChannel_error() = runBlocking {
        val exception = Exception("YAY!")
        val subject = Observable.error<Int>(exception)

        try {
            subject.toBroadcastChannel(10).openSubscription().toList()
            throw AssertionError()
        } catch (e: Exception) {
            assertEquals(e.message, exception.message)
        }
    }

    @Test
    fun toBroadcastChannel_cancel() = runBlocking {
        val disposeLock = CountDownLatch(1)

        val subject = Observable.never<Int>()
            .doFinally { disposeLock.countDown() }

        subject.toBroadcastChannel(10).cancel()
        assertTrue(disposeLock.await(1, TimeUnit.SECONDS))
    }

}