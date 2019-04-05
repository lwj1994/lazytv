package me.luwenjie.lazytv.main

import androidx.lifecycle.MutableLiveData
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import kotlinx.coroutines.launch
import me.luwenjie.lazytv.GroupModel
import me.luwenjie.lazytv.common.BaseState
import me.luwenjie.lazytv.common.BaseViewModel
import java.util.UUID

/**
 * @author luwenjie on 2019/3/24 01:54:36
 */
class MainViewModel(s: MainState) : BaseViewModel<MainState>(s) {
  val groupLive = MutableLiveData<List<GroupModel>>()
  val selectGroupLive = MutableLiveData<GroupModel>()
  val createGroupLive = MutableLiveData<Boolean>()
  val selectGroupId: String
    get() = selectGroupLive.value?.id ?: ""
  var preSelectGroupId = ""

  fun getGroups() = launch {
    val groups = db.channelDao().getGroups()
    groupLive.postValue(groups)
  }

  fun selectFirst() {
    launch {
      val groups = db.channelDao().getGroups()
      if (groups.isNotEmpty())
        selectGroupLive.postValue(groups[0])
    }
  }

  fun createGroup(name: String) {
    launch {
      val existed = db.channelDao().getGroup(name) != null

      if (!existed) {
        db.channelDao().insertGroupModel(GroupModel(
            name = name,
            id = UUID.fromString(name).toString()
        ))
      }
      createGroupLive.postValue(existed)
    }
  }

  fun createChannel(name: String, id: String, url: String) {
    launch {
      val existed = db.channelDao().getChannel(name) != null

      if (!existed) {
        db.channelDao().insertGroupModel(GroupModel(
            name = name,
            id = UUID.fromString(name).toString()
        ))
      }
      createGroupLive.postValue(existed)
    }
  }

  companion object : MvRxViewModelFactory<MainViewModel, MainState> {
    private const val TAG = "MianViewModel"
    override fun create(
        viewModelContext: ViewModelContext,
        state: MainState
    ): MainViewModel {
      return MainViewModel(state)
    }
  }
}


data class MainState(val tag: String = "") : BaseState()
