package me.luwenjie.lazytv.common

import com.airbnb.mvrx.BaseMvRxFragment

/**
 * @author luwenjie on 2019/3/24 01:35:56
 */
abstract class BaseFragment:BaseMvRxFragment() {

  override fun invalidate() {

  }

  companion object {
    private const val TAG = "BaseFragment"
  }
}