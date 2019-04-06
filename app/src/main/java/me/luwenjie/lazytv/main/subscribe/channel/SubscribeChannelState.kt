package me.luwenjie.lazytv.main.subscribe.channel

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Uninitialized
import me.luwenjie.lazytv.SubscribeModel.SubscribeChannel
import me.luwenjie.lazytv.common.BaseState

/**
 * @author luwenjie on 2019/3/15 15:01:27
 */
data class SubscribeChannelState(val request: Async<List<SubscribeChannel>> = Uninitialized,
    val list: List<SubscribeChannel> = emptyList()) : BaseState() {


  fun generateNewState(
      request: Async<List<SubscribeChannel>>): SubscribeChannelState {
    val newly = request() ?: emptyList()
    return copy(request = request, list = newly)
  }

  companion object {
    private const val TAG = "ChannelState"
  }
}