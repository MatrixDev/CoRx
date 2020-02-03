package dev.matrix.corx

import io.reactivex.*
import io.reactivex.observers.DisposableObserver
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flow

/**
 * @author Rostyslav.Lesovyi
 */

fun Completable.toFlow() = flow<Unit> {
    await()
}

fun <T> Single<T>.toFlow() = flow {
    emit(await())
}

fun <T> Maybe<T>.toFlow() = flow {
    val result = await()
    if (result != null) {
        emit(result)
    }
}

@Suppress("EXPERIMENTAL_API_USAGE")
fun <T> Observable<T>.toFlow() = channelFlow {
    val disposable = subscribeWith(object : DisposableObserver<T>() {
        override fun onNext(t: T) {
            offer(t)
        }
        override fun onComplete() {
            close()
        }
        override fun onError(e: Throwable) {
            close(e)
        }
    })
    invokeOnClose { disposable.dispose() }
}
