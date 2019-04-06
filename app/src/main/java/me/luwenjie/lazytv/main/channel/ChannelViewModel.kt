package me.luwenjie.lazytv.main.channel

import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.uber.autodispose.autoDisposable
import io.reactivex.Observable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import me.luwenjie.lazytv.ChannelModel
import me.luwenjie.lazytv.GroupModel
import me.luwenjie.lazytv.LazytvResult
import me.luwenjie.lazytv.common.BaseViewModel
import me.luwenjie.lazytv.util.ioThread
import me.luwenjie.lazytv.util.mainThread
import org.koin.core.KoinApplication
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
        val result = db.channelDao().insertGroupModel(GroupModel(
            ChannelFragment.DEFAULT_GROUP_ID, "默认分组", "", 0
        ))
        KoinApplication.logger.debug("insertGroupModel:默认分组 result =  ${result}")
      }

      val counts = db.channelDao().getGroupChildCounts(ChannelFragment.DEFAULT_GROUP_ID)
      KoinApplication.logger.debug("默认分组有 ${counts} 条节目")
    }
  }

  fun deleteChannel(model: ChannelModel) {
    launch {
      db.channelDao().deleteGroupAndChildren(model.id)
      setState {
        copy(list = ArrayList(list).apply {
          remove(model)
        })
      }
    }
  }

  fun loadDataForCurrentGroup(clear: Boolean, page: Int = 0) {
    withState { state ->
      if (state.request is Loading) return@withState
      val ppage = if (clear) 0 else page
      Observable.create<List<ChannelModel>> { e ->
        e.onNext(db.channelDao().getChannelByGroupId(groupId, ppage))
        e.onComplete()
      }.subscribeOn(ioThread).execute {
        val newly = it() ?: emptyList()
        val list = if (clear) newly else this.list + newly
        copy(request = it, list = list, page = ppage,
            channelSum = if (clear) db.channelDao().getGroupChildCounts(
                ChannelFragment.DEFAULT_GROUP_ID) else channelSum)
      }
    }
  }

  fun addChannel(name: String, url: String, result: (LazytvResult) -> Unit) {
    val model = ChannelModel(groupId = ChannelFragment.DEFAULT_GROUP_ID,
        name = name,
        url = url,
        id = "${UUID.randomUUID()}$name-$url",
        groupName = "默认分组",
        image = "")
    db.channelDao().insertChannelModel(model).subscribeOn(ioThread).observeOn(
        mainThread).autoDisposable(
        lifeScope).subscribe({
      result(LazytvResult(msg = "添加成功"))
      loadDataForCurrentGroup(true)
    }, {
      result(LazytvResult(LazytvResult.FAIL, msg = "添加失败：$it"))
      KoinApplication.logger.debug("$it")
    })
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