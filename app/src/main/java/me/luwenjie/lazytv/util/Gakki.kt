package me.luwenjie.lazytv.util

import com.orhanobut.logger.Logger

class Gakki {
  companion object {
    @JvmStatic
    fun debug(tag: String, text: String) {
      Logger.t(tag).d(text)
    }
  }
}