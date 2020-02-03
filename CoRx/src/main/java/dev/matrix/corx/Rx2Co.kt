package dev.matrix.corx

import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.observers.DisposableMaybeObserver
import io.reactivex.observers.DisposableSingleObserver
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * @author Rostyslav.Lesovyi
 */

suspend fun Completable.await() = suspendCancellableCoroutine<Unit> {
    val disposable = subscribeWith(object : DisposableCompletableObserver() {
        override fun onComplete() = it.resume(Unit)
        override fun onError(e: Throwable) = it.resumeWithException(e)
    })
    it.invokeOnCancellation { disposable.dispose() }
}

suspend fun <T> Single<T>.await() = suspendCancellableCoroutine<T> {
    val disposable = subscribeWith(object : DisposableSingleObserver<T>() {
        override fun onSuccess(t: T) = it.resume(t)
        override fun onError(e: Throwable) = it.resumeWithException(e)
    })
    it.invokeOnCancellation { disposable.dispose() }
}

suspend fun <T> Maybe<T>.await() = suspendCancellableCoroutine<T?> {
    val disposable = subscribeWith(object : DisposableMaybeObserver<T>() {
        override fun onSuccess(t: T) = it.resume(t)
        override fun onComplete() = it.resume(null)
        override fun onError(e: Throwable) = it.resumeWithException(e)
    })
    it.invokeOnCancellation { disposable.dispose() }
}

suspend fun <T> Observable<T>.await() = suspendCancellableCoroutine<List<T>> {
    val disposable = toList().subscribeWith(object : DisposableSingleObserver<List<T>>() {
        override fun onSuccess(t: List<T>) = it.resume(t)
        override fun onError(e: Throwable) = it.resumeWithException(e)
    })
    it.invokeOnCancellation { disposable.dispose() }
}
