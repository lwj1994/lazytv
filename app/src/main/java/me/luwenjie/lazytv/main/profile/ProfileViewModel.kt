package me.luwenjie.lazytv.main.profile

import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import me.luwenjie.lazytv.common.BaseViewModel

/**
 * @author luwenjie on 2019/3/15 15:50:38
 */
class ProfileViewModel(s: ProfileState) : BaseViewModel<ProfileState>(s) {

  companion object : MvRxViewModelFactory<ProfileViewModel, ProfileState> {
    private const val TAG = "ChannelViewModel"
    override fun create(viewModelContext: ViewModelContext,
        state: ProfileState): ProfileViewModel {
      return ProfileViewModel(state)
    }
  }
}