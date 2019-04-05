package me.luwenjie.lazytv.main.groupmanger

import me.luwenjie.lazytv.common.BaseState

/**
 * @author luwenjie on 2019/3/24 20:30:53
 */
data class GroupManagerState(val list:List<GroupManagerModel> = emptyList()) : BaseState() {


  companion object {
    private const val TAG = "GroupManagerState"
  }
}


data class GroupManagerModel(val name: String, val id: String, val count: Int)