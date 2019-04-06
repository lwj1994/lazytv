package me.luwenjie.lazytv.main.subscribe.channel

import android.os.Bundle
import android.os.Parcelable
import android.view.View
import com.airbnb.epoxy.EpoxyController
import com.airbnb.mvrx.MvRx
import com.airbnb.mvrx.args
import com.airbnb.mvrx.fragmentViewModel
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.fragment_subscribechannel.backView
import kotlinx.android.synthetic.main.fragment_subscribechannel.subscribechanneladView
import kotlinx.android.synthetic.main.fragment_subscribechannel.title
import me.luwenjie.lazytv.R
import me.luwenjie.lazytv.common.BaseListFragment
import me.luwenjie.lazytv.common.load
import me.luwenjie.lazytv.common.simpleController
import me.luwenjie.lazytv.player.PlayerActivity

/**
 * @author luwenjie on 2019/4/6 18:00:07
 */
class SubscribeChannelFragment : BaseListFragment() {
  private val argg by args<SubscribeChannelFragmentArgs>()
  private val viewModel: SubscribeChannelViewModel by fragmentViewModel()
  override val layoutRes: Int
    get() = R.layout.fragment_subscribechannel

  override fun initView(view: View) {
    backView.setOnClickListener {
      requireActivity().finish()
    }
    title.text = argg.title
    subscribechanneladView.load()
  }

  override fun initData(savedInstanceState: Bundle?) {
    viewModel.asyncSubscribe(this, SubscribeChannelState::request, onSuccess = {
      showRefreshing(false)
    }, onFail = {
      showRefreshing(false)
    })
    onRefresh()
  }

  override fun onRefresh() {
    viewModel.load(argg.groupUrl)
  }

  override fun epoxyController(): EpoxyController = simpleController(viewModel) { state ->
    state.list.forEach {
      subscribeChannelView {
        id(it.name, it.url)
        model(it)
        onClickListener { _ ->
          startActivity(PlayerActivity.newIntent(requireContext(), it.url))
        }
      }
    }
  }


  companion object {
    private const val TAG = "SubscribeChannelFragment"
    private const val ARG = "SubscribeChannelFragmentArgs"
    fun newInstance(arg: SubscribeChannelFragmentArgs) = SubscribeChannelFragment().apply {
      arguments = Bundle().apply {
        putParcelable(MvRx.KEY_ARG, arg)
      }
    }
  }
}

@Parcelize
class SubscribeChannelFragmentArgs(val groupUrl: String, val title: String) : Parcelable