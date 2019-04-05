package me.luwenjie.lazytv.main.profile

import me.luwenjie.lazytv.common.BaseState

/**
 * @author luwenjie on 2019/3/15 15:01:27
 */
data class ProfileState(val list:List<ProfileModel> = emptyList()):BaseState() {

  companion object {
    private const val TAG = "ChannelState"
  }
}