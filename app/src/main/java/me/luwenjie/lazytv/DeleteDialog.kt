package me.luwenjie.lazytv

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog.Builder
import androidx.fragment.app.DialogFragment

class DeleteDialog : DialogFragment() {
  private var onClickListener: DialogInterface.OnClickListener? = null
  fun setOnClickListener(onClickListener: DialogInterface.OnClickListener?) {
    this.onClickListener = onClickListener
  }

  companion object {
    private const val TAG = "DeleteCommentDialog"

    private val ITEMS = arrayOf("确定", "取消")
  }

  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    val dialog = Builder(requireContext())
        .setTitle("确认删除吗？")
        .setItems(ITEMS, onClickListener)
        .create()

    dialog.setOnShowListener {
    }
    return dialog
  }

  override fun onDestroy() {
    super.onDestroy()
    onClickListener = null
  }
}