package me.luwenjie.lazytv.common.webview

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.webkit.WebSettings
import android.webkit.WebViewClient
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.activity_web.activity_web_Toolbar
import kotlinx.android.synthetic.main.activity_web.webView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import me.luwenjie.lazytv.R
import me.luwenjie.lazytv.common.BaseActivity
import me.luwenjie.lazytv.util.OkHttpUtil

/**
 * @author luwenjie on 2019/4/6 21:39:16
 */
class WebActivity : BaseActivity() {

  private val args: WebActivityArgs by lazy {
    intent.getParcelableExtra(ARGS) as WebActivityArgs
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_web)
    val webSettings = webView.settings
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE
    }
    val lcWebViewClient = WebViewClient()
    webView.webViewClient = lcWebViewClient

    activity_web_Toolbar.title = args.title
    setSupportActionBar(activity_web_Toolbar)
    activity_web_Toolbar.navigationIcon = getDrawable(R.drawable.ic_arrow_back_black_24dp)
    activity_web_Toolbar.setNavigationOnClickListener {
      finish()
    }
  }

  override fun onPostCreate(savedInstanceState: Bundle?) {
    super.onPostCreate(savedInstanceState)
    runBlocking {
      webView.loadData(
          withContext(Dispatchers.Default) {
            OkHttpUtil.get(args.url)?.body()?.string() ?: ""
          }, "text/html", "utf-8")
    }
  }

  companion object {
    private const val TAG = "WebActivity"
    private const val ARGS = "WebActivityargs"

    fun newIntent(context: Context, url: String, title: String = "") = Intent(context,
        WebActivity::class.java).apply {
      putExtra(ARGS, WebActivityArgs(url, title))
    }
  }

}

@Parcelize
class WebActivityArgs(val url: String, val title: String = "") : Parcelable