package me.luwenjie.lazytv.main.channel

import android.content.DialogInterface
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import com.airbnb.epoxy.EpoxyController
import com.airbnb.mvrx.MvRx
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.args
import com.airbnb.mvrx.withState
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.fragment_main.adView
import kotlinx.android.synthetic.main.fragment_main.addButton
import kotlinx.android.synthetic.main.fragment_main.collectView
import me.luwenjie.lazytv.DeleteDialog
import me.luwenjie.lazytv.R
import me.luwenjie.lazytv.common.BaseListFragment
import me.luwenjie.lazytv.common.load
import me.luwenjie.lazytv.common.loadingView
import me.luwenjie.lazytv.common.simpleController
import me.luwenjie.lazytv.main.AddChannelDialog
import me.luwenjie.lazytv.main.emptyView
import me.luwenjie.lazytv.player.PlayerActivity
import org.koin.core.KoinApplication
import java.util.UUID

/**
 * @author luwenjie on 2019/3/15 14:59:33
 */
class ChannelFragment : BaseListFragment() {
  override val layoutRes: Int
    get() = R.layout.fragment_main

  private val argss by args<ChannelFragmentArgs>()
  private val viewModel: ChannelViewModel by activityViewModel()
  private val addDialog by lazy {
    AddChannelDialog()
  }

  override fun initView(view: View) {
    swipeRefreshLayout.isEnabled = true

    adView.load()
    addButton.setOnClickListener {
      addDialog.show(childFragmentManager, "")
    }
    collectView.setOnClickListener {

    }
  }


  override fun initData(savedInstanceState: Bundle?) {
    viewModel.asyncSubscribe(ChannelState::request, onSuccess = {
      showRefreshing(false)
      withState(viewModel){state->
        if (state.page == 0){
          recyclerView.post {
            recyclerView.scrollToPosition(0)
          }
        }
      }
    },
        onFail = { showRefreshing(false) })
    onRefresh()

    KoinApplication.logger.debug("${viewModel}")
  }

  override fun onRefresh() {
    viewModel.loadDataForCurrentGroup(true)
  }

  override fun epoxyController(): EpoxyController = simpleController(viewModel) { state ->
    KoinApplication.logger.debug("epoxyController ${ state.list.size}")
    if (state.list.isEmpty()){
      emptyView {
        id("emptyView")
      }
      return@simpleController
    }
    state.list.asSequence().forEach {
      channelView {
        id(it.url, it.name, it.groupName, it.id)
        model(it)
        onClickListener { _ ->
          startActivity(PlayerActivity.newIntent(requireContext(), it.url))
        }
        onLongClickListener {_->
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
      }
    }

    if (state.hasNextPage) {
      loadingView {
        id("${state.page}")
        onBind { model, view, position ->
          viewModel.loadDataForCurrentGroup(false, state.page + 1)
        }
      }
    }
  }


  companion object {
    private const val TAG = "ChannelFragment"
    val DEFAULT_GROUP_ID = UUID.fromString("12-32-423-321-123123").toString()
    fun newInstance(groupId: String = DEFAULT_GROUP_ID) = ChannelFragment().apply {
      arguments = Bundle().apply {
        putParcelable(MvRx.KEY_ARG, ChannelFragmentArgs(groupId))
      }
    }
  }
}

@Parcelize
class ChannelFragmentArgs(val groupId: String) : Parcelable