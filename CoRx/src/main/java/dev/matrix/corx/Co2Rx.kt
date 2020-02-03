package dev.matrix.corx

import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job

/**
 * @author Rostyslav.Lesovyi
 */

fun Job.toCompletable(cancelJobOnDispose: Boolean = true, completeOnJobCancel: Boolean = false) =
    Completable.create { emitter ->
        invokeOnCompletion {
            if (it != null) {
                if (completeOnJobCancel && it is CancellationException) {
                    emitter.onComplete()
                } else {
                    emitter.onError(it)
                }
            } else {
                emitter.onComplete()
            }
        }
        if (cancelJobOnDispose) {
            emitter.setCancellable { if (isActive) cancel() }
        }
    }

@Suppress("EXPERIMENTAL_API_USAGE")
fun <T> Deferred<T>.toSingle(cancelJobOnDispose: Boolean = true) =
    Single.create<T> { emitter ->
        invokeOnCompletion {
            if (it != null) {
                emitter.onError(it)
            } else {
                emitter.onSuccess(getCompleted())
            }
        }
        if (cancelJobOnDispose) {
            emitter.setCancellable { if (isActive) cancel() }
        }
    }

@Suppress("EXPERIMENTAL_API_USAGE")
fun <T> Deferred<T?>.toMaybe(cancelJobOnDispose: Boolean = true, completeOnJobCancel: Boolean = false) =
    Maybe.create<T> { emitter ->
        invokeOnCompletion {
            if (it != null) {
                if (completeOnJobCancel && it is CancellationException) {
                    emitter.onComplete()
                } else {
                    emitter.onError(it)
                }
            } else {
                val completed = getCompleted()
                if (completed != null) {
                    emitter.onSuccess(completed)
                } else {
                    emitter.onComplete()
                }
            }
        }

        if (cancelJobOnDispose) {
            emitter.setCancellable { if (isActive) cancel() }
        }
    }

@Suppress("EXPERIMENTAL_API_USAGE")
fun <T> Deferred<T?>.toObservable(cancelJobOnDispose: Boolean = true, completeOnJobCancel: Boolean = false) =
    Observable.create<T> { emitter ->
        invokeOnCompletion {
            if (it != null) {
                if (completeOnJobCancel && it is CancellationException) {
                    emitter.onComplete()
                } else {
                    emitter.onError(it)
                }
            } else {
                val completed = getCompleted()
                if (completed != null) {
                    emitter.onNext(completed)
                }
                emitter.onComplete()
            }
        }

        if (cancelJobOnDispose) {
            emitter.setCancellable { if (isActive) cancel() }
        }
    }
