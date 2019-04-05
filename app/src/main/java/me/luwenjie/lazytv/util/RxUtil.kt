package me.luwenjie.lazytv.util

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.exceptions.UndeliverableException
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import java.io.IOException
import java.net.SocketException

val ioThread: Scheduler
  get() = Schedulers.io()

val mainThread: Scheduler
  get() = AndroidSchedulers.mainThread()

operator fun CompositeDisposable.plus(disposable: Disposable): CompositeDisposable {
  add(disposable)
  return this
}

object RxUtil {
  fun initErrorHandler() {
    RxJavaPlugins.setErrorHandler { throwable ->
      var e = throwable
      Gakki.debug("RxUtil","e = $e")
      if (e is UndeliverableException) {
        e = e.cause
      }
      if ((e is IOException) || (e is SocketException)) {
        // fine, irrelevant network problem or API that throws on cancellation
        return@setErrorHandler
      }
      if ((e is NullPointerException) || (e is IllegalArgumentException)) {
        // that's likely a bug in the application
        Thread.currentThread().uncaughtExceptionHandler.uncaughtException(Thread.currentThread(), e)
        return@setErrorHandler
      }

      if (e is IllegalStateException) {
        // that's a bug in RxJava or in a custom operator
        Thread.currentThread().uncaughtExceptionHandler
            .uncaughtException(Thread.currentThread(), e);
        return@setErrorHandler
      }
    }
  }
}