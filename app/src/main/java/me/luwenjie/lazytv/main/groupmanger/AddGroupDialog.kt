package me.luwenjie.lazytv.main.groupmanger

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.dialog_addgroup.confirmButton
import kotlinx.android.synthetic.main.dialog_addgroup.groupName
import me.luwenjie.lazytv.R
import me.luwenjie.lazytv.common.BaseSimpleDialog
import me.luwenjie.lazytv.util.ToastUtil

/**
 * @author luwenjie on 2019/3/24 21:55:09
 */
class AddGroupDialog : BaseSimpleDialog() {

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.dialog_addgroup, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    confirmButton.setOnClickListener {
      if (groupName.text.isEmpty()){
        ToastUtil.show("请输入")
        return@setOnClickListener
      }

    }
  }

  companion object {
    private const val TAG = "AddGroupDialog"
  }
}