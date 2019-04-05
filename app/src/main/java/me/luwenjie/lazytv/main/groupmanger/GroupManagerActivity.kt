package me.luwenjie.lazytv.main.groupmanger

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.airbnb.mvrx.viewModel
import kotlinx.android.synthetic.main.activity_groupmanager.*
import me.luwenjie.lazytv.R
import me.luwenjie.lazytv.common.BaseActivity

/**
 * @author luwenjie on 2019/3/24 15:53:01
 */
class GroupManagerActivity : BaseActivity() {
  private val viewModel: GroupManagerViewModel by viewModel()
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_groupmanager)
    initView()
  }

  private fun initView() {
    onBackButton.setOnClickListener {
      onBackPressed()
    }
    addGroupButton.setOnClickListener {
      AddGroupDialog().show(supportFragmentManager, "")
    }

  }

  companion object {
    private const val TAG = "GroupManagerActivity"
    fun newIntent(context: Context) = Intent(context, GroupManagerActivity::class.java)
  }
}