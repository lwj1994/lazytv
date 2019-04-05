package me.luwenjie.lazytv.main.channel

import android.os.Bundle
import android.os.Parcelable
import android.view.View
import com.airbnb.epoxy.EpoxyController
import com.airbnb.mvrx.MvRx
import com.airbnb.mvrx.args
import com.airbnb.mvrx.fragmentViewModel
import kotlinx.android.parcel.Parcelize
import me.luwenjie.lazytv.common.BaseListFragment
import me.luwenjie.lazytv.common.simpleController
import me.luwenjie.lazytv.player.PlayerActivity

/**
 * @author luwenjie on 2019/3/15 14:59:33
 */
class ChannelFragment : BaseListFragment() {
  private val argss by args<ChannelFragmentArgs>()
  private val viewModel: ChannelViewModel by fragmentViewModel(keyFactory = {
    argss.groupId
  })


  override fun initView(view: View) {
    swipeRefreshLayout.isEnabled = false
  }


  override fun initData(savedInstanceState: Bundle?) {
    onRefresh()
  }

  override fun onRefresh() {
    viewModel.loadDataForCurrentGroup(true)
  }

  override fun epoxyController(): EpoxyController = simpleController(viewModel) { state ->
    state.list.asSequence().forEach {
      channelView {
        id(it.url, it.name, it.groupName)
        model(it)
        onClickListener { _ ->
          startActivity(PlayerActivity.newIntent(requireContext(), it.url))
        }
      }
    }
  }


  companion object {
    private const val TAG = "ChannelFragment"
    fun newInstance(groupId: String) = ChannelFragment().apply {
      arguments = Bundle().apply {
        putParcelable(MvRx.KEY_ARG, ChannelFragmentArgs(groupId))
      }
    }
  }
}

@Parcelize
class ChannelFragmentArgs(val groupId: String) : Parcelable