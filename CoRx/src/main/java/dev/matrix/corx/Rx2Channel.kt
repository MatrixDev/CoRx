package dev.matrix.corx

import io.reactivex.Observable
import io.reactivex.observers.DisposableObserver
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel

/**
 * @author Rostyslav.Lesovyi
 */

@Suppress("EXPERIMENTAL_API_USAGE")
fun <T> Observable<T>.toChannel(capacity: Int = Channel.RENDEZVOUS): ReceiveChannel<T> {
    val channel = Channel<T>(capacity)
    val disposable = subscribeWith(object : DisposableObserver<T>() {
        override fun onNext(t: T) {
            channel.offer(t)
        }
        override fun onComplete() {
            channel.close()
        }
        override fun onError(e: Throwable) {
            channel.close(e)
        }
    })
    channel.invokeOnClose { disposable.dispose() }
    return channel
}

@Suppress("EXPERIMENTAL_API_USAGE")
fun <T> Observable<T>.toBroadcastChannel(capacity: Int = Channel.RENDEZVOUS): BroadcastChannel<T> {
    val channel = BroadcastChannel<T>(capacity)
    val disposable = subscribeWith(object : DisposableObserver<T>() {
        override fun onNext(t: T) {
            channel.offer(t)
        }
        override fun onComplete() {
            channel.close()
        }
        override fun onError(e: Throwable) {
            channel.close(e)
        }
    })
    channel.invokeOnClose { disposable.dispose() }
    return channel
}
