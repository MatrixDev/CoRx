package dev.matrix.corx

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Job
import org.junit.Test
import org.junit.Assert.*
import java.util.concurrent.TimeUnit

/**
 * @author Rostyslav.Lesovyi
 */
class Co2RxKtTest {

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Completable
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Test
    fun toCompletable_positive() {
        val job = Job()
        val observer = job.toCompletable().test()

        job.complete()

        observer.awaitTerminalEvent(1, TimeUnit.SECONDS)
        observer.assertComplete()
    }

    @Test
    fun toCompletable_error() {
        val job = Job()
        val observer = job.toCompletable().test()
        val exception = Exception("YES!")

        job.completeExceptionally(exception)

        observer.awaitTerminalEvent(1, TimeUnit.SECONDS)
        observer.assertError { it.message == exception.message }
    }

    @Test
    fun toCompletable_cancel() {
        val job = Job()
        val observer = job.toCompletable().test()
        val exception = CancellationException("YES!")

        job.cancel(exception)

        observer.awaitTerminalEvent(1, TimeUnit.SECONDS)
        observer.assertError { it.message == exception.message }
    }

    @Test
    fun toCompletable_cancelNoError() {
        val job = Job()
        val observer = job.toCompletable(completeOnJobCancel = true).test()
        val exception = CancellationException("YES!")

        job.cancel(exception)

        observer.awaitTerminalEvent(1, TimeUnit.SECONDS)
        observer.assertComplete()
    }

    @Test
    fun toCompletable_dispose() {
        val job = Job()
        job.toCompletable().subscribe().dispose()

        assertTrue(job.isCancelled)
    }

    @Test
    fun toCompletable_disposeNoCancel() {
        val job = Job()
        job.toCompletable(cancelJobOnDispose = false).subscribe().dispose()

        assertTrue(job.isActive)
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Single
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Test
    fun toSingle_positive() {
        val job = CompletableDeferred<String>()
        val observer = job.toSingle().test()
        val result = "YES!"

        job.complete(result)

        observer.awaitTerminalEvent(1, TimeUnit.SECONDS)
        observer.assertValue(result)
    }

    @Test
    fun toSingle_error() {
        val job = CompletableDeferred<String>()
        val observer = job.toSingle().test()
        val exception = Exception("YES!")

        job.completeExceptionally(exception)

        observer.awaitTerminalEvent(1, TimeUnit.SECONDS)
        observer.assertError { it.message == exception.message }
    }

    @Test
    fun toSingle_cancel() {
        val job = CompletableDeferred<String>()
        val observer = job.toSingle().test()
        val exception = CancellationException("YES!")

        job.cancel(exception)

        observer.awaitTerminalEvent(1, TimeUnit.SECONDS)
        observer.assertError { it.message == exception.message }
    }

    @Test
    fun toSingle_dispose() {
        val job = CompletableDeferred<String>()
        job.toSingle().subscribe().dispose()

        assertTrue(job.isCancelled)
    }

    @Test
    fun toSingle_disposeNoCancel() {
        val job = CompletableDeferred<String>()
        job.toSingle(cancelJobOnDispose = false).subscribe().dispose()

        assertTrue(job.isActive)
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Maybe
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Test
    fun toMaybe_positive() {
        val job = CompletableDeferred<String>()
        val observer = job.toMaybe().test()
        val result = "YES!"

        job.complete(result)

        observer.awaitTerminalEvent(1, TimeUnit.SECONDS)
        observer.assertValue(result)
    }

    @Test
    fun toMaybe_error() {
        val job = CompletableDeferred<String>()
        val observer = job.toMaybe().test()
        val exception = Exception("YES!")

        job.completeExceptionally(exception)

        observer.awaitTerminalEvent(1, TimeUnit.SECONDS)
        observer.assertError { it.message == exception.message }
    }

    @Test
    fun toMaybe_cancel() {
        val job = CompletableDeferred<String>()
        val observer = job.toMaybe().test()
        val exception = CancellationException("YES!")

        job.cancel(exception)

        observer.awaitTerminalEvent(1, TimeUnit.SECONDS)
        observer.assertError { it.message == exception.message }
    }

    @Test
    fun toMaybe_cancelNoError() {
        val job = CompletableDeferred<String>()
        val observer = job.toMaybe(completeOnJobCancel = true).test()
        val exception = CancellationException("YES!")

        job.cancel(exception)

        observer.awaitTerminalEvent(1, TimeUnit.SECONDS)
        observer.assertComplete()
    }

    @Test
    fun toMaybe_dispose() {
        val job = CompletableDeferred<String>()
        job.toMaybe().subscribe().dispose()

        assertTrue(job.isCancelled)
    }

    @Test
    fun toMaybe_disposeNoCancel() {
        val job = CompletableDeferred<String>()
        job.toMaybe(cancelJobOnDispose = false).subscribe().dispose()

        assertTrue(job.isActive)
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Observable
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Test
    fun toObservable_positive() {
        val job = CompletableDeferred<String>()
        val observer = job.toObservable().test()
        val result = "YES!"

        job.complete(result)

        observer.awaitTerminalEvent(1, TimeUnit.SECONDS)
        observer.assertValue(result)
    }

    @Test
    fun toObservable_error() {
        val job = CompletableDeferred<String>()
        val observer = job.toObservable().test()
        val exception = Exception("YES!")

        job.completeExceptionally(exception)

        observer.awaitTerminalEvent(1, TimeUnit.SECONDS)
        observer.assertError { it.message == exception.message }
    }

    @Test
    fun toObservable_cancel() {
        val job = CompletableDeferred<String>()
        val observer = job.toObservable().test()
        val exception = CancellationException("YES!")

        job.cancel(exception)

        observer.awaitTerminalEvent(1, TimeUnit.SECONDS)
        observer.assertError { it.message == exception.message }
    }

    @Test
    fun toObservable_cancelNoError() {
        val job = CompletableDeferred<String>()
        val observer = job.toObservable(completeOnJobCancel = true).test()
        val exception = CancellationException("YES!")

        job.cancel(exception)

        observer.awaitTerminalEvent(1, TimeUnit.SECONDS)
        observer.assertComplete()
    }

    @Test
    fun toObservable_dispose() {
        val job = CompletableDeferred<String>()
        job.toObservable().subscribe().dispose()

        assertTrue(job.isCancelled)
    }

    @Test
    fun toObservable_disposeNoCancel() {
        val job = CompletableDeferred<String>()
        job.toObservable(cancelJobOnDispose = false).subscribe().dispose()

        assertTrue(job.isActive)
    }

}
