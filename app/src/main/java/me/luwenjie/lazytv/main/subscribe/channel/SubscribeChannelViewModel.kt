package me.luwenjie.lazytv.main.subscribe.channel

import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import io.reactivex.Observable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import me.luwenjie.lazytv.SubscribeModel
import me.luwenjie.lazytv.SubscribeModel.SubscribeChannel
import me.luwenjie.lazytv.common.BaseViewModel
import me.luwenjie.lazytv.util.OkHttpUtil
import me.luwenjie.lazytv.util.ioThread
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import kotlin.coroutines.CoroutineContext


/**
 * @author luwenjie on 2019/3/15 15:50:38
 */
class SubscribeChannelViewModel(s: SubscribeChannelState) : BaseViewModel<SubscribeChannelState>(
    s) {
  private val gson = Gson()
  override val coroutineContext: CoroutineContext
    get() = Job() + Dispatchers.IO



  fun load(url:String) {
    val request = Request.Builder()
        .url(url)
        .addHeader("User-Agent",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36")
        .build()
    withState {state ->
      Observable.create<List<SubscribeChannel>> {e->
        OkHttpUtil.okHttpClient.newCall(request).enqueue(object :Callback{
          override fun onFailure(call: Call, er: IOException) {
            e.onError(Exception(er))
          }

          override fun onResponse(call: Call, response: Response) {
            val json = response.body()?.string() ?: ""
            try {
              val content = gson.fromJson(json, SubscribeModel::class.java) ?: return
              e.onNext(content.channels)
            }catch (er: JsonSyntaxException){
              e.onError(Exception(er))
            }

          }

        })
      }.subscribeOn(ioThread).execute {
        generateNewState(it)
      }
    }


  }

  companion object : MvRxViewModelFactory<SubscribeChannelViewModel, SubscribeChannelState> {
    private const val TAG = "SubscribeChannelViewModel"

    override fun create(
        viewModelContext: ViewModelContext,
        state: SubscribeChannelState
    ): SubscribeChannelViewModel {
      return SubscribeChannelViewModel(state)
    }
  }
}