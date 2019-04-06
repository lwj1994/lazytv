package me.luwenjie.lazytv.main.subscribe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.airbnb.mvrx.existingViewModel
import me.luwenjie.lazytv.LazytvResult
import me.luwenjie.lazytv.R
import me.luwenjie.lazytv.common.BaseSimpleDialog
import me.luwenjie.lazytv.util.ToastUtil
import org.koin.core.KoinApplication

/**
 * @author luwenjie on 2019/3/20 19:31:09
 */
class AddSubscribeDialog : BaseSimpleDialog() {
  private val viewModel: SubscribeViewModel by existingViewModel()
  private lateinit var urlEditext: EditText
  private lateinit var confirm: Button

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_DeviceDefault_Dialog_MinWidth)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.dialog_addsubscribe, container, false).apply {
      urlEditext = findViewById(R.id.dialog_addsubscribe_url)
      confirm = findViewById(R.id.dialog_addsubscribe_confirm)

      KoinApplication.logger.debug("${viewModel}")

      confirm.setOnClickListener {
        val text = urlEditext.text.toString()
        if (text.isEmpty()) {
          ToastUtil.show("请输入url地址")
          return@setOnClickListener
        }
        viewModel.parseSubscribeJson(text){
          urlEditext.post {
            ToastUtil.show(it.msg)
            if (it.code == LazytvResult.SUCCESS){
              viewModel.fetchFeeds()
              urlEditext.setText("")
              dismiss()
            }
          }

        }
      }
    }
  }

  companion object {
    private const val TAG = "AddSubscribeDialog"
  }
}