package me.luwenjie.lazytv.common

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Lifecycle.State.CREATED
import androidx.lifecycle.Lifecycle.State.DESTROYED
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import com.airbnb.mvrx.BaseMvRxViewModel
import com.airbnb.mvrx.BuildConfig
import com.airbnb.mvrx.MvRxState
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import me.luwenjie.lazytv.AppDatabase
import kotlin.LazyThreadSafetyMode.NONE
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel<S : MvRxState>(initialState: S) : BaseMvRxViewModel<S>(initialState,
    debugMode = BuildConfig.DEBUG), LifecycleOwner, CoroutineScope {
  protected val db = AppDatabase.getInstance()
  override val coroutineContext: CoroutineContext
    get() = Job() + Dispatchers.IO

  private val lifecycleRegistry: LifecycleRegistry by lazy(NONE) {
    LifecycleRegistry(this)
  }
  protected val lifeScope by lazy(NONE) {
    AndroidLifecycleScopeProvider.from(this)
  }

  init {
    lifecycleRegistry.markState(CREATED)
  }

  override fun getLifecycle(): Lifecycle {
    return lifecycleRegistry
  }

  override fun onCleared() {
    super.onCleared()
    lifecycleRegistry.markState(DESTROYED)
  }
}