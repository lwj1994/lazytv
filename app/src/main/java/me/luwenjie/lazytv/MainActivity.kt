package me.luwenjie.lazytv

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.transaction
import kotlinx.android.synthetic.main.activity_main.*
import me.luwenjie.lazytv.common.BaseActivity
import me.luwenjie.lazytv.common.bottomview.BottomTab
import me.luwenjie.lazytv.main.MainFragment
import me.luwenjie.lazytv.main.profile.ProfileFragment
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
            val profile = BottomTab.Builder(context)
                .iconSelected(R.drawable.ic_profile_solid)
                .iconNormal(R.drawable.ic_profile_border)
                .text(getString(R.string.profile))
                .tabPadding(padding, padding)
                .build()
            setupTab(home, profile)

            setOnSelectedListener { prePosition, position ->
                toggleFragment(prePosition, position)
            }

            toggleFragment(-1, 0)
            select(0)
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
        return if (position == CHANNEL) MainFragment()
        else ProfileFragment()
    }

    companion object {
        private const val TAG = "MainActivity"
        const val CHANNEL = 0
        const val PROFILE = 1
    }
}
