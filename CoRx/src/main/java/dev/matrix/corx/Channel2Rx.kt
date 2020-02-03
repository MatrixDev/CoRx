package dev.matrix.corx

import io.reactivex.Observable
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.ReceiveChannel

/**
 * @author Rostyslav.Lesovyi
 */

fun <T> ReceiveChannel<T>.toObservable(completeOnCancel: Boolean = false) = Observable.create<T> {
    val channel = this
    val job = GlobalScope.async {
        try {
            for (value in channel) {
                it.onNext(value)
            }
            it.onComplete()
        } catch (e: Throwable) {
            if (completeOnCancel && e is CancellationException) {
                it.onComplete()
            } else {
                it.onError(e)
            }
        }
    }
    it.setCancellable {
        job.cancel()
        channel.cancel()
    }
}

@Suppress("EXPERIMENTAL_API_USAGE")
fun <T> BroadcastChannel<T>.toObservable(): Observable<T> {
    return openSubscription().toObservable()
}
