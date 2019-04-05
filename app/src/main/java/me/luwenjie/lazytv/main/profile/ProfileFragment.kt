package me.luwenjie.lazytv.main.profile

import android.os.Bundle
import android.view.View
import com.airbnb.epoxy.EpoxyController
import com.airbnb.mvrx.fragmentViewModel
import me.luwenjie.lazytv.R
import me.luwenjie.lazytv.common.BaseListFragment
import me.luwenjie.lazytv.common.simpleController

/**
 * @author luwenjie on 2019/3/15 14:59:33
 */
class ProfileFragment : BaseListFragment() {
  private val viewModel: ProfileViewModel by fragmentViewModel()
  override val layoutRes: Int
    get() = R.layout.fragment_profile

  override fun initView(view: View) {

  }

  override fun initData(savedInstanceState: Bundle?) {
  }

  override fun onRefresh() {
  }

  override fun epoxyController(): EpoxyController = simpleController(viewModel) {


  }


  companion object {
    private const val TAG = "ChannelFragment"
  }
}