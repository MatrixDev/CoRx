package dev.matrix.corx

import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.*
import kotlinx.coroutines.*
import org.junit.Test
import org.junit.Assert.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 * @author Rostyslav.Lesovyi
 */
@Suppress("BlockingMethodInNonBlockingContext")
class Rx2CoKtTest {

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Completable
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Test
    fun completable_await_positive() = runBlocking {
        val completable = CompletableSubject.create()

        val job = async(Dispatchers.IO) {
            completable.await()
        }

        completable.onComplete()
        withTimeout(100) {
            job.await()
        }
    }

    @Test
    fun completable_await_negative() = runBlocking {
        val completable = Completable.never()
        val job = launch(Dispatchers.IO) {
            completable.await()
        }

        assert(job.isActive)
        delay(100)
        assert(job.isActive)

        job.cancel()
    }

    @Test
    fun completable_await_cancel() = runBlocking {
        val disposeLock = CountDownLatch(1)
        val subscribeLock = CountDownLatch(1)

        val completable = Completable.never()
            .doOnDispose { disposeLock.countDown() }
            .doOnSubscribe { subscribeLock.countDown() }

        val job = launch(Dispatchers.IO) {
            completable.await()
        }

        assertTrue(subscribeLock.await(1, TimeUnit.SECONDS))
        job.cancel()
        assertTrue(disposeLock.await(1, TimeUnit.SECONDS))
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Single
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Test
    fun single_await_positive() = runBlocking {
        val result = "YES!"
        val single = SingleSubject.create<String>()

        val job = async(Dispatchers.IO) {
            single.await()
        }

        single.onSuccess(result)

        withTimeout(100) {
            assertEquals(job.await(), result)
        }
    }

    @Test
    fun single_await_negative() = runBlocking {
        val single = Single.never<String>()
        val job = launch(Dispatchers.IO) {
            single.await()
        }

        assert(job.isActive)
        delay(100)
        assert(job.isActive)

        job.cancel()
    }

    @Test
    fun single_await_cancel() = runBlocking {
        val disposeLock = CountDownLatch(1)
        val subscribeLock = CountDownLatch(1)

        val single = Single.never<String>()
            .doOnDispose { disposeLock.countDown() }
            .doOnSubscribe { subscribeLock.countDown() }

        val job = launch(Dispatchers.IO) {
            single.await()
        }

        assertTrue(subscribeLock.await(1, TimeUnit.SECONDS))
        job.cancel()
        assertTrue(disposeLock.await(1, TimeUnit.SECONDS))
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Maybe
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Test
    fun maybe_await_positive() = runBlocking {
        val result = "YES!"
        val maybe = MaybeSubject.create<String>()

        val job = async(Dispatchers.IO) {
            maybe.await()
        }

        maybe.onSuccess(result)

        withTimeout(100) {
            assertEquals(job.await(), result)
        }
    }

    @Test
    fun maybe_await_positiveEmpty() = runBlocking {
        val maybe = MaybeSubject.create<String>()

        val job = async(Dispatchers.IO) {
            maybe.await()
        }

        maybe.onComplete()

        withTimeout(100) {
            assertEquals(job.await(), null)
        }
    }

    @Test
    fun maybe_await_negative() = runBlocking {
        val maybe = Maybe.never<String>()
        val job = launch(Dispatchers.IO) {
            maybe.await()
        }

        assert(job.isActive)
        delay(100)
        assert(job.isActive)

        job.cancel()
    }

    @Test
    fun maybe_await_cancel() = runBlocking {
        val disposeLock = CountDownLatch(1)
        val subscribeLock = CountDownLatch(1)

        val maybe = Maybe.never<String>()
            .doOnDispose { disposeLock.countDown() }
            .doOnSubscribe { subscribeLock.countDown() }

        val job = launch(Dispatchers.IO) {
            maybe.await()
        }

        assertTrue(subscribeLock.await(1, TimeUnit.SECONDS))
        job.cancel()
        assertTrue(disposeLock.await(1, TimeUnit.SECONDS))
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Observable
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Test
    fun observable_await_positive() = runBlocking {
        val result = "YES!"
        val observable = ReplaySubject.create<String>()

        val job = async(Dispatchers.IO) {
            observable.await()
        }

        observable.onNext(result)
        observable.onComplete()

        withTimeout(100) {
            assertEquals(job.await(), listOf(result))
        }
    }

    @Test
    fun observable_await_positiveEmpty() = runBlocking {
        val observable = ReplaySubject.create<String>()

        val job = async(Dispatchers.IO) {
            observable.await()
        }

        observable.onComplete()

        withTimeout(100) {
            assertTrue(job.await().isEmpty())
        }
    }

    @Test
    fun observable_await_negative() = runBlocking {
        val observable = Observable.never<String>()
        val job = launch(Dispatchers.IO) {
            observable.await()
        }

        assert(job.isActive)
        delay(100)
        assert(job.isActive)

        job.cancel()
    }

    @Test
    fun observable_await_cancel() = runBlocking {
        val disposeLock = CountDownLatch(1)
        val subscribeLock = CountDownLatch(1)

        val observable = Observable.never<String>()
            .doOnDispose { disposeLock.countDown() }
            .doOnSubscribe { subscribeLock.countDown() }

        val job = launch(Dispatchers.IO) {
            observable.await()
        }

        assertTrue(subscribeLock.await(1, TimeUnit.SECONDS))
        job.cancel()
        assertTrue(disposeLock.await(1, TimeUnit.SECONDS))
    }
}
