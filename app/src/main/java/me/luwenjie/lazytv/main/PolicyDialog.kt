package me.luwenjie.lazytv.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.luwenjie.lazytv.R
import me.luwenjie.lazytv.common.BaseSimpleDialog

/**
 * @author luwenjie on 2019/4/6 22:22:53
 */
class PolicyDialog:BaseSimpleDialog(){

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.dialog_policy,container,false)
  }

  companion object {
    private const val TAG = "PolicyDialog"
  }
}