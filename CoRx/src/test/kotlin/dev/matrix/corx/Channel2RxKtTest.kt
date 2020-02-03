package dev.matrix.corx

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import org.junit.Test
import java.util.concurrent.TimeUnit

/**
 * @author Rostyslav.Lesovyi
 */
@Suppress("EXPERIMENTAL_API_USAGE")
class Channel2RxKtTest {

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Channel
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Test
    fun receiveChannel_toObservable_positive() {
        val channel = Channel<String>(10)
        val observer = channel.toObservable().test()

        val values = arrayOf("1", "2", "3")
        for (value in values) {
            channel.offer(value)
        }
        channel.close()

        observer.awaitTerminalEvent(1, TimeUnit.SECONDS)
        observer.assertResult(*values)
    }

    @Test
    fun receiveChannel_toObservable_error() {
        val channel = Channel<String>(10)
        val observer = channel.toObservable().test()

        val exception = Exception("YAY!")
        channel.close(exception)

        observer.awaitTerminalEvent(1, TimeUnit.SECONDS)
        observer.assertError { it.message == exception.message }
    }

    @Test
    fun receiveChannel_toObservable_cancel() {
        val channel = Channel<String>(10)
        val observer = channel.toObservable().test()

        val exception = CancellationException("YAY!")
        channel.cancel(exception)

        observer.awaitTerminalEvent(1, TimeUnit.SECONDS)
        observer.assertError { it.message == exception.message }
    }

    @Test
    fun receiveChannel_toObservable_cancelNoError() {
        val channel = Channel<String>(10)
        val observer = channel.toObservable(completeOnCancel = true).test()

        channel.cancel()

        observer.awaitTerminalEvent(1, TimeUnit.SECONDS)
        observer.assertComplete()
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // BroadcastChannel
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Test
    fun broadcastChannel_toObservable_positive() {
        val channel = BroadcastChannel<String>(10)
        val observer = channel.toObservable().test()

        val values = arrayOf("1", "2", "3")
        for (value in values) {
            channel.offer(value)
        }
        channel.close()

        observer.awaitTerminalEvent(1, TimeUnit.SECONDS)
        observer.assertResult(*values)
    }

}
