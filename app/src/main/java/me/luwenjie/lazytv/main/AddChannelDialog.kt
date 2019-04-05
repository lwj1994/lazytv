package me.luwenjie.lazytv.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.airbnb.mvrx.existingViewModel
import me.luwenjie.lazytv.R
import me.luwenjie.lazytv.common.BaseSimpleDialog
import me.luwenjie.lazytv.util.ToastUtil

/**
 * @author luwenjie on 2019/3/20 19:31:09
 */
class AddChannelDialog : BaseSimpleDialog() {
  private val viewModel: MainViewModel by existingViewModel()
  private lateinit var nameEditext: EditText
  private lateinit var urlEditext: EditText
  private lateinit var confirm: Button
  private lateinit var chooseGroupButton: Button

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_DeviceDefault_Dialog_MinWidth);
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.dialog_addchannel, container, false).apply {
      nameEditext = findViewById(R.id.dialog_addchannel_name)
      urlEditext = findViewById(R.id.dialog_addchannel_url)
      confirm = findViewById(R.id.dialog_addchannel_confirm)
      chooseGroupButton = findViewById(R.id.dialog_addchannel_chooseGroup)

      confirm.setOnClickListener {
        if (nameEditext.text.isEmpty()) {
          ToastUtil.show("请输入名称")
          return@setOnClickListener
        }
        if (urlEditext.text.isEmpty()) {
          ToastUtil.show("请输入url")
          return@setOnClickListener
        }

        viewModel.createGroup(nameEditext.text.toString())
      }
    }
  }

  companion object {
    private const val TAG = "AddChannelDialog"
  }
}