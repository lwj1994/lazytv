package me.luwenjie.lazytv.util

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import me.luwenjie.lazytv.App

object KeyboardUtil {
  fun showKeyboard(view: View) {
    try {
      view.requestFocus()
      val inputManager = view.context.getSystemService(
          Context.INPUT_METHOD_SERVICE) as InputMethodManager
      inputManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)

    } catch (e: Exception) {
    }
  }

  fun hideKeyboard(view: View) {
    try {
      val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
      if (!imm.isActive) {
        return
      }
      imm.hideSoftInputFromWindow(view.windowToken, 0)
    } catch (e: Exception) {
    }
  }


  fun isKeyboardShowed(view: View): Boolean {
    try {
      val inputManager = view.context.getSystemService(
          Context.INPUT_METHOD_SERVICE) as InputMethodManager
      return inputManager.isActive(view)
    } catch (e: Exception) {
    }
    return false
  }

  fun toggleSoftInput() {
    try {
      val imm = App.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
      if (!imm.isActive) {
        return
      }
      imm.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0)
    } catch (e: Exception) {
    }
  }
}