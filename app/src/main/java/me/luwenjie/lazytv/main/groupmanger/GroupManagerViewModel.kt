package me.luwenjie.lazytv.main.groupmanger

import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import me.luwenjie.lazytv.common.BaseViewModel

/**
 * @author luwenjie on 2019/3/24 20:30:25
 */
class GroupManagerViewModel(s: GroupManagerState) : BaseViewModel<GroupManagerState>(s) {


  companion object : MvRxViewModelFactory<GroupManagerViewModel, GroupManagerState> {
    private const val TAG = "GroupManagerViewModel"
    override fun create(viewModelContext: ViewModelContext,
        state: GroupManagerState): GroupManagerViewModel {
      return GroupManagerViewModel(state)
    }
  }
}