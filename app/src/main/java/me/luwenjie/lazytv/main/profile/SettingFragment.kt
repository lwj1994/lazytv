package me.luwenjie.lazytv.main.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_setting.*
import me.luwenjie.lazytv.common.BaseFragment
import me.luwenjie.lazytv.common.webview.WebActivity
import me.luwenjie.lazytv.main.CoffeeDialog
import me.luwenjie.lazytv.main.PolicyDialog
import me.luwenjie.lazytv.util.ToastUtil


/**
 * @author luwenjie on 2019/3/15 14:59:33
 */
class SettingFragment : BaseFragment() {
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    return inflater.inflate(me.luwenjie.lazytv.R.layout.fragment_setting, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    fragment_setting_help.setOnClickListener {
      startActivity(WebActivity.newIntent(requireContext(),
          "https://gitee.com/m3u8list/m3u8list/raw/master/strawberry_help.html", "帮助"))
    }

    fragment_setting_coffee.setOnClickListener {
      CoffeeDialog().show(childFragmentManager, "")
    }

    fragment_setting_policy.setOnClickListener {
      PolicyDialog().show(childFragmentManager, "")
    }

    fragment_setting_appraise.setOnClickListener {
      try {
        val uri = Uri.parse("market://details?id=" + requireActivity().packageName)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
      } catch (e: Exception) {
        ToastUtil.show("您的手机没有安装Androi应用市场")
        e.printStackTrace()
      }

    }
    val html = "<a href='https://t.me/strawberryplayer'>加入 telegram 和我交流</a>"
    fragment_setting_telegram.text = Html.fromHtml(html)
    fragment_setting_telegram.movementMethod = LinkMovementMethod.getInstance()

  }
}