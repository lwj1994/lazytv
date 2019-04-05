package me.luwenjie.lazytv.util

import android.widget.Toast
import me.luwenjie.lazytv.App

object ToastUtil {

  fun show(s:String){
    Toast.makeText(App.context,s,Toast.LENGTH_SHORT).show()
  }
}