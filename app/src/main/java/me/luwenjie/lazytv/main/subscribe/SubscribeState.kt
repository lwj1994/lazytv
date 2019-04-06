package me.luwenjie.lazytv.main.subscribe

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.Uninitialized
import me.luwenjie.lazytv.GroupModel
import me.luwenjie.lazytv.common.BaseState


/**
 * @author luwenjie on 2019/4/6 14:42:01
 */
data class SubscribeState(val request: Async<List<GroupModel>> = Uninitialized, val feeds: List<GroupModel> = emptyList()) : BaseState() {

  val isLoading: Boolean
    get() {
      return request is Loading
    }

  fun generateNewState(
      request: Async<List<GroupModel>>): SubscribeState {
    return copy(request = request, feeds = request()?: emptyList())
  }

  companion object {
    private const val TAG = "SubscribeState"
  }
}


