package me.luwenjie.lazytv.common

import com.airbnb.mvrx.BaseMvRxActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class BaseActivity : BaseMvRxActivity(), CoroutineScope {
  override val coroutineContext: CoroutineContext
    get() = Job() + Dispatchers.Main

}