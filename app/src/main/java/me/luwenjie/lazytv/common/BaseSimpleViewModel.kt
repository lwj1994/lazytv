package me.luwenjie.lazytv.common

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Lifecycle.State.CREATED
import androidx.lifecycle.Lifecycle.State.DESTROYED
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import me.luwenjie.lazytv.AppDatabase
import kotlin.LazyThreadSafetyMode.NONE
import kotlin.coroutines.CoroutineContext

abstract class BaseSimpleViewModel : ViewModel(), LifecycleOwner, CoroutineScope {
  protected val dao = AppDatabase.getInstance().channelDao()
  override val coroutineContext: CoroutineContext
    get() = Job() + Dispatchers.Main

  private val lifecycleRegistry: LifecycleRegistry by lazy(NONE) {
    LifecycleRegistry(this)
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