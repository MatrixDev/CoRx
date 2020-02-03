package dev.matrix.corx

import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.Test

import org.junit.Assert.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 * @author Rostyslav.Lesovyi
 */
@Suppress("EXPERIMENTAL_API_USAGE", "BlockingMethodInNonBlockingContext")
class Rx2FlowKtTest {

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Completable
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Test
    fun completable_toFlow_positive() = runBlocking {
        val flow = Completable.complete().toFlow()

        withTimeout(100) {
            assertEquals(flow.count(), 0)
        }
    }

    @Test
    fun completable_toFlow_error() = runBlocking {
        val exception = Exception("test")
        val flow = Completable.error(exception).toFlow()

        withTimeout(100) {
            try {
                flow.count()
                throw AssertionError()
            } catch (e: Exception) {
                assertEquals(e.message, exception.message)
            }
        }
    }

    @Test
    fun completable_toFlow_cancel() = runBlocking {
        val disposeLock = CountDownLatch(1)
        val subscribeLock = CountDownLatch(1)

        val flow = Completable.never()
            .doOnDispose { disposeLock.countDown() }
            .doOnSubscribe { subscribeLock.countDown() }
            .toFlow()

        val job = async(Dispatchers.IO) {
            flow.count()
        }

        assertTrue(subscribeLock.await(1, TimeUnit.SECONDS))
        job.cancel()
        assertTrue(disposeLock.await(1, TimeUnit.SECONDS))
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Single
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Test
    fun single_toFlow_positive() = runBlocking {
        val value = "YES!"
        val flow = Single.just(value).toFlow()

        withTimeout(100) {
            assertEquals(flow.single(), value)
        }
    }

    @Test
    fun single_toFlow_error() = runBlocking {
        val exception = Exception("test")
        val flow = Single.error<String>(exception).toFlow()

        withTimeout(100) {
            try {
                flow.count()
                throw AssertionError()
            } catch (e: Exception) {
                assertEquals(e.message, exception.message)
            }
        }
    }

    @Test
    fun single_toFlow_cancel() = runBlocking {
        val disposeLock = CountDownLatch(1)
        val subscribeLock = CountDownLatch(1)

        val flow = Single.never<String>()
            .doOnDispose { disposeLock.countDown() }
            .doOnSubscribe { subscribeLock.countDown() }
            .toFlow()

        val job = async(Dispatchers.IO) {
            flow.count()
        }

        assertTrue(subscribeLock.await(1, TimeUnit.SECONDS))
        job.cancel()
        assertTrue(disposeLock.await(1, TimeUnit.SECONDS))
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Maybe
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Test
    fun maybe_toFlow_positive() = runBlocking {
        val value = "YES!"
        val flow = Maybe.just(value).toFlow()

        withTimeout(100) {
            assertEquals(flow.single(), value)
        }
    }

    @Test
    fun maybe_toFlow_positiveEmpty() = runBlocking {
        val flow = Maybe.empty<String>().toFlow()

        withTimeout(100) {
            assertEquals(flow.count(), 0)
        }
    }

    @Test
    fun maybe_toFlow_error() = runBlocking {
        val exception = Exception("test")
        val flow = Maybe.error<String>(exception).toFlow()

        withTimeout(100) {
            try {
                flow.count()
                throw AssertionError()
            } catch (e: Exception) {
                assertEquals(e.message, exception.message)
            }
        }
    }

    @Test
    fun maybe_toFlow_cancel() = runBlocking {
        val disposeLock = CountDownLatch(1)
        val subscribeLock = CountDownLatch(1)

        val flow = Maybe.never<String>()
            .doOnDispose { disposeLock.countDown() }
            .doOnSubscribe { subscribeLock.countDown() }
            .toFlow()

        val job = async(Dispatchers.IO) {
            flow.count()
        }

        assertTrue(subscribeLock.await(1, TimeUnit.SECONDS))
        job.cancel()
        assertTrue(disposeLock.await(1, TimeUnit.SECONDS))
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Observable
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Test
    fun observable_toFlow_positive() = runBlocking {
        val value = listOf(1, 2, 3)
        val flow = Observable.fromIterable(value).toFlow()

        withTimeout(100) {
            assertEquals(flow.toList(), value)
        }
    }

    @Test
    fun observable_toFlow_positiveEmpty() = runBlocking {
        val flow = Observable.empty<String>().toFlow()

        withTimeout(100) {
            assertEquals(flow.count(), 0)
        }
    }

    @Test
    fun observable_toFlow_error() = runBlocking {
        val exception = Exception("test")
        val flow = Observable.error<String>(exception).toFlow()

        withTimeout(100) {
            try {
                flow.count()
                throw AssertionError()
            } catch (e: Exception) {
                assertEquals(e.message, exception.message)
            }
        }
    }

    @Test
    fun observable_toFlow_cancel() = runBlocking {
        val disposeLock = CountDownLatch(1)
        val subscribeLock = CountDownLatch(1)

        val flow = Observable.never<String>()
            .doOnDispose { disposeLock.countDown() }
            .doOnSubscribe { subscribeLock.countDown() }
            .toFlow()

        val job = async(Dispatchers.IO) {
            flow.count()
        }

        assertTrue(subscribeLock.await(1, TimeUnit.SECONDS))
        job.cancel()
        assertTrue(disposeLock.await(1, TimeUnit.SECONDS))
    }

}
