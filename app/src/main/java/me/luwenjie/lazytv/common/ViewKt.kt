package me.luwenjie.lazytv.common

import android.graphics.Rect
import android.view.TouchDelegate
import android.view.View

fun View.expandTouchRect(padding: Int) {
  (parent as? View)?.let { parent ->
    parent.post {
      val bounds = Rect()
      isEnabled = true
      getHitRect(bounds)
      bounds.top -= padding
      bounds.bottom += padding
      bounds.left -= padding
      bounds.right += padding
      parent.touchDelegate = TouchDelegate(bounds, this)
    }
  }
}
