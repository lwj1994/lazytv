package me.luwenjie.lazytv.player

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.fragment.app.transaction
import me.luwenjie.lazytv.R
import me.luwenjie.lazytv.common.BaseActivity

/**
 * @author luwenjie on 2019/3/14 14:33:46
 */
class PlayerActivity : BaseActivity() {
  companion object {
    private const val URL = "url"
    fun newIntent(context: Context, url: String): Intent {
      return Intent(context, PlayerActivity::class.java).putExtra(URL, url)
    }

    private const val TAG = "PlayerActivity"
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN)
    setContentView(R.layout.view_container)
    supportFragmentManager.transaction {
      add(R.id.container, PlayerFragment.newInstance(intent.getStringExtra(URL)), TAG)
    }
  }
}