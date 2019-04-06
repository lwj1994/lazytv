package me.luwenjie.lazytv.main.subscribe

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import com.airbnb.epoxy.EpoxyController
import com.airbnb.mvrx.activityViewModel
import kotlinx.android.synthetic.main.fragment_subscribe.addButton
import me.luwenjie.lazytv.DeleteDialog
import me.luwenjie.lazytv.R
import me.luwenjie.lazytv.common.BaseListFragment
import me.luwenjie.lazytv.common.simpleController
import me.luwenjie.lazytv.main.emptyView
import me.luwenjie.lazytv.main.subscribe.channel.ChannelActivity
import me.luwenjie.lazytv.main.subscribe.channel.SubscribeChannelFragmentArgs

/**
 * @author luwenjie on 2019/4/6 14:41:37
 */
class SubscribeFragment : BaseListFragment() {
  override val layoutRes: Int
    get() = R.layout.fragment_subscribe
  private val viewModel: SubscribeViewModel by activityViewModel()
  private val addDialog by lazy {
    AddSubscribeDialog()
  }

  override fun initView(view: View) {
    addButton.setOnClickListener {
      addDialog.show(childFragmentManager, "")
    }
  }

  override fun initData(savedInstanceState: Bundle?) {
    viewModel.asyncSubscribe(this, SubscribeState::request, onSuccess = {
      showRefreshing(false)
    }, onFail = {
      showRefreshing(false)
    })
    onRefresh()
  }

  override fun onRefresh() {
    viewModel.fetchFeeds()
  }

  override fun epoxyController(): EpoxyController = simpleController(viewModel) { state ->
    if (state.feeds.isEmpty()){
      emptyView {
        id("emptyView")
      }
      return@simpleController
    }
    state.feeds.forEach {
      subscribeView {
        id(it.id, it.name)
        model(it)
        onLongClickItem{v->
          DeleteDialog().apply {
            setOnClickListener(onClickListener = DialogInterface.OnClickListener { dialog, which ->
              dismiss()
              if (which == 0){
                viewModel.deleteChannel(it)
              }else{

              }
            })
          }.show(childFragmentManager,"")
          true
        }
        onClickItem { v ->
          startActivity(
              ChannelActivity.newIntent(v.context, SubscribeChannelFragmentArgs(it.url, it.name)))
        }
      }
    }
  }


  companion object {
    private const val TAG = "SubscribeFragment"
    fun newInstance() = SubscribeFragment()
  }
}