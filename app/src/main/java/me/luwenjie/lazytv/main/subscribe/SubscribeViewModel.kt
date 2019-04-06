package me.luwenjie.lazytv.main.subscribe

import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import io.reactivex.Observable
import kotlinx.coroutines.launch
import me.luwenjie.lazytv.ChannelModel
import me.luwenjie.lazytv.GroupModel
import me.luwenjie.lazytv.LazytvResult
import me.luwenjie.lazytv.SubscribeModel
import me.luwenjie.lazytv.common.BaseViewModel
import me.luwenjie.lazytv.generateUUID
import me.luwenjie.lazytv.main.channel.ChannelFragment
import me.luwenjie.lazytv.util.OkHttpUtil
import me.luwenjie.lazytv.util.ioThread
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import okhttp3.Response
import org.koin.core.KoinApplication
import java.io.IOException

/**
 * @author luwenjie on 2019/4/6 14:42:48
 */
class SubscribeViewModel(s: SubscribeState) : BaseViewModel<SubscribeState>(s) {
//  val liveParseJson = MutableLiveData<LazytvResult>()
  private val gson = Gson()
  fun fetchFeeds() {
    Observable.create<List<GroupModel>> {
      val groups = db.channelDao().getGroups().filter {g->
        g.id != ChannelFragment.DEFAULT_GROUP_ID
      }
      it.onNext(groups)
      it.onComplete()
    }.subscribeOn(ioThread).execute {
      generateNewState(it)
    }
  }

  fun parseSubscribeJson(
      url: String,result:(LazytvResult)->Unit) {

    launch {
      try {
        val request = Request.Builder()
            .url(url)
            .addHeader("User-Agent",
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36")
            .build()
        OkHttpUtil.okHttpClient.newCall(request).enqueue(object : Callback {
          override fun onFailure(call: Call, e: IOException) {
            KoinApplication.logger.debug("解析订阅错误 $url\n $e")
            result(LazytvResult(LazytvResult.FAIL, "请求失败，请检查网络或者url是否有效"))
          }

          override fun onResponse(call: Call, response: Response) {
            val json = response.body()?.string() ?: ""
            result(LazytvResult(LazytvResult.SUCCESS, "请求成功，正在解析数据..."))
            KoinApplication.logger.debug("解析订阅 $url\n $json")
            try {

              val content = gson.fromJson(json, SubscribeModel::class.java) ?: return
              val groupId = generateUUID("${content.title}${content.uuid}")

              db.channelDao().insertGroup(
                  GroupModel(groupId, name = content.title, oderId = 0,url = url,
                      childChannels = content.channels.map {
                        ChannelModel(
                            id = generateUUID(it.name, it.url),
                            groupId = groupId,
                            url = it.url,
                            name = it.name,
                            groupName = content.title
                        )
                      })
              )
              result(LazytvResult(LazytvResult.SUCCESS, "添加成功"))

            } catch (e: JsonSyntaxException) {
              result(LazytvResult(LazytvResult.FAIL, "解析失败，请检查url是否满足解析格式"))
            }
          }
        })

      }catch (e:Exception){
        result(LazytvResult(LazytvResult.FAIL, "解析失败，请检查url是否满足解析格式"))
      }

    }

  }

  fun deleteChannel(it: GroupModel) {
    launch {
      db.channelDao().deleteGroup(it.id)
      setState {
        copy(feeds = ArrayList(feeds).apply {
          remove(it)
        })
      }
    }
  }


  companion object : MvRxViewModelFactory<SubscribeViewModel, SubscribeState> {
    private const val TAG = "SubscribeViewModel"
    override fun create(viewModelContext: ViewModelContext,
        state: SubscribeState): SubscribeViewModel {
      return SubscribeViewModel(state)
    }
  }
}