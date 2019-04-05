package me.luwenjie.lazytv.main.channel

import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import me.luwenjie.lazytv.ChannelModel
import me.luwenjie.lazytv.GroupModel
import me.luwenjie.lazytv.common.BaseViewModel
import me.luwenjie.lazytv.util.ioThread
import java.util.ArrayList
import java.util.UUID
import kotlin.coroutines.CoroutineContext


/**
 * @author luwenjie on 2019/3/15 15:50:38
 */
class ChannelViewModel(s: ChannelState, private val groupId: String) : BaseViewModel<ChannelState>(
    s) {
  override val coroutineContext: CoroutineContext
    get() = Job() + Dispatchers.IO

  init {
    createGroupIfEmpty()
  }

  private fun createGroupIfEmpty() {
    launch {
      val groupCount = db.channelDao().getGroupCount()
      if (groupCount == 0) {
        db.channelDao().insertGroupModel(GroupModel(
            UUID.fromString("默认分组").toString(), "默认分组", 0
        ))
      }
    }
  }

  fun loadDataForCurrentGroup(clear: Boolean, page: Int = 0) {
    withState { state ->
      if (state.request is Loading) return@withState
      val ppage = if (clear) 0 else page
      db.channelDao().getChannelByGroupId(groupId, ppage).subscribeOn(ioThread).execute {

        val list = if (clear) it() ?: emptyList() else this.list + emptyList()

        copy(request = it, list = list, page = ppage)
      }

    }
  }

  fun addChannel(model: ChannelModel) {
    withState {
      setState {
        copy(list = ArrayList(list).apply {
          add(0, model)
        })
      }
    }
  }


  companion object : MvRxViewModelFactory<ChannelViewModel, ChannelState> {
    private const val TAG = "ChannelViewModel"
    override fun create(
        viewModelContext: ViewModelContext,
        state: ChannelState
    ): ChannelViewModel {
      return ChannelViewModel(state, state.groupId)
    }
  }
}