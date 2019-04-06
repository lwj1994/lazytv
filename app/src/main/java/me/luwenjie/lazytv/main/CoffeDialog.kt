package me.luwenjie.lazytv.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.dialog_coffee.dialog_coffee_ImageView
import me.luwenjie.lazytv.R
import me.luwenjie.lazytv.common.BaseSimpleDialog

/**
 * @author luwenjie on 2019/4/6 22:33:49
 *
 */
class CoffeeDialog:BaseSimpleDialog() {


  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.dialog_coffee,container,false).apply {
    }
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    Glide.with(dialog_coffee_ImageView).load("https://gitee.com/m3u8list/m3u8list/raw/master/wx_payme.png").into(dialog_coffee_ImageView)

  }
  companion object {
    private const val TAG = "CoffeDialog"
  }
}