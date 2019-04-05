package me.luwenjie.lazytv.main

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupWindow
import androidx.fragment.app.transaction
import androidx.lifecycle.Observer
import com.airbnb.mvrx.activityViewModel
import kotlinx.android.synthetic.main.fragment_main.adView
import kotlinx.android.synthetic.main.fragment_main.addButton
import kotlinx.android.synthetic.main.fragment_main.groupName
import me.luwenjie.lazytv.R
import me.luwenjie.lazytv.common.BaseFragment
import me.luwenjie.lazytv.common.expandTouchRect
import me.luwenjie.lazytv.common.load
import me.luwenjie.lazytv.main.channel.ChannelFragment
import org.jetbrains.anko.dip

/**
 * @author luwenjie on 2019/3/24 01:34:48
 */
class MainFragment : BaseFragment() {
  private val popupWindow: PopupWindow by lazy { createAddWindow() }
  private val viewModel: MainViewModel by activityViewModel()

  private fun createAddWindow() = PopupWindow(requireContext()).apply {
    width = addButton.dip(80)
    height = ViewGroup.LayoutParams.WRAP_CONTENT
    contentView = View.inflate(context, R.layout.view_pop_add, null).apply {
      val addView: Button = findViewById(R.id.add)
      val subscribeView: Button = findViewById(R.id.subscribe)

      addView.setOnClickListener {
        AddChannelDialog().show(childFragmentManager, "")
        dismiss()
      }
      subscribeView.setOnClickListener {
        dismiss()
      }
    }
    setBackgroundDrawable(ColorDrawable(Color.WHITE))
    isOutsideTouchable = true
    setOnDismissListener {
      isFocusable = false
    }

  }

  override fun onCreateView(
      inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?
  ): View? = inflater.inflate(
      R.layout.fragment_main, container,
      false
  )

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    initData()
  }

  private fun initData() {
    adView.load()
    addButton.expandTouchRect(addButton.dip(10))
    addButton.setOnClickListener {
      if (!popupWindow.isShowing) {
        popupWindow.isFocusable = true
        popupWindow.showAsDropDown(addButton)
      } else
        popupWindow.dismiss()
    }
    groupName.setOnClickListener {
      GroupDialog.newInstance(viewModel.selectGroupId).show(childFragmentManager, "")
    }
    viewModel.selectGroupLive.observe(this, Observer {
      groupName.text = it.name
    })
    viewModel.selectGroupLive.observe(this, Observer {
      toggleFragment(viewModel.preSelectGroupId, viewModel.selectGroupId)
    })
    viewModel.selectFirst()
  }


  private fun toggleFragment(preSelectId: String, selectId: String) {
    if (preSelectId == selectId) return
    val toShowFragment = childFragmentManager.findFragmentByTag(selectId)
        ?: ChannelFragment.newInstance(selectId)
    val toHideFragment = childFragmentManager.findFragmentByTag(preSelectId)

    childFragmentManager.transaction(allowStateLoss = true) {
      if (!toShowFragment.isAdded) {
        add(R.id.container, toShowFragment, selectId)
      }
      if (toHideFragment != null) {
        hide(toHideFragment).show(toShowFragment)
      } else {
        show(toShowFragment)
      }
    }
    viewModel.preSelectGroupId = selectId
  }

  companion object {
    private const val TAG = "MainFragment"
  }
}