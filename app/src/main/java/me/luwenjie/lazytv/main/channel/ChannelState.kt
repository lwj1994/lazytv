package me.luwenjie.lazytv.main.channel

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Uninitialized
import me.luwenjie.lazytv.ChannelModel
import me.luwenjie.lazytv.common.BaseState

/**
 * @author luwenjie on 2019/3/15 15:01:27
 */
data class ChannelState(val request: Async<List<ChannelModel>> = Uninitialized,
    val list: List<ChannelModel> = emptyList(), val page: Int = 0,
    val groupId: String = "", val channelSum: Int = 0) : BaseState() {

  val hasNextPage: Boolean
    get() {
      return (page + 1) * 15 < channelSum
    }

  constructor(args: ChannelFragmentArgs) : this(groupId = args.groupId)

  companion object {
    private const val TAG = "ChannelState"
  }
}