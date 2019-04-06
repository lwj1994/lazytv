package me.luwenjie.lazytv

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.transaction
import kotlinx.android.synthetic.main.activity_main.bottomBar
import me.luwenjie.lazytv.common.BaseActivity
import me.luwenjie.lazytv.common.bottomview.BottomTab
import me.luwenjie.lazytv.main.PolicyDialog
import me.luwenjie.lazytv.main.channel.ChannelFragment
import me.luwenjie.lazytv.main.profile.SettingFragment
import me.luwenjie.lazytv.main.subscribe.SubscribeFragment
import me.luwenjie.lazytv.util.SharedPreferencesRegistry
import org.jetbrains.anko.dip

class MainActivity : BaseActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    bottomBar.run {
      val padding = dip(4)
      val home = BottomTab.Builder(context)
          .iconSelected(R.drawable.ic_home_solid)
          .iconNormal(R.drawable.ic_home_border)
          .text(getString(R.string.home))
          .tabPadding(padding, padding)
          .build()
      val susbcribe = BottomTab.Builder(context)
          .iconSelected(R.drawable.ic_subscribe_selected)
          .iconNormal(R.drawable.ic_subscribe_unselected)
          .text(getString(R.string.subscribe))
          .tabPadding(padding, padding)
          .build()
      val profile = BottomTab.Builder(context)
          .iconSelected(R.drawable.ic_setting_selected)
          .iconNormal(R.drawable.ic_setting_unselected)
          .text(getString(R.string.setting))
          .tabPadding(padding, padding)
          .build()
      setupTab(home, susbcribe, profile)

      setOnSelectedListener { prePosition, position ->
        toggleFragment(prePosition, position)
      }

      toggleFragment(-1, 0)
      select(0)

    }
    if (SharedPreferencesRegistry.getIsFirstIntoApp(this)) {
      PolicyDialog().show(supportFragmentManager, "")
      SharedPreferencesRegistry.setIsFirstIntoApp(this, false)
    }
  }

  private fun toggleFragment(prePosition: Int, position: Int) {
    val toShowFragment = supportFragmentManager.findFragmentByTag("$position")
        ?: newFragmentInstance(position)
    val toHideFragment = supportFragmentManager.findFragmentByTag("$prePosition")

    supportFragmentManager.transaction(allowStateLoss = true) {
      if (!toShowFragment.isAdded) {
        add(R.id.container, toShowFragment, "$position")
      }
      if (toHideFragment != null) {
        hide(toHideFragment).show(toShowFragment)
      } else {
        show(toShowFragment)
      }
    }
  }

  private fun newFragmentInstance(position: Int): Fragment {
    return when (position) {
      CHANNEL -> ChannelFragment.newInstance()
      SUBSCRIBE -> SubscribeFragment.newInstance()
      else -> SettingFragment()
    }
  }

  companion object {
    private const val TAG = "MainActivity"
    const val CHANNEL = 0
    const val SUBSCRIBE = 1
    const val PROFILE = 2
  }
}
