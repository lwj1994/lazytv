package com.xiachufang.lanfan.common.core

import android.content.Context
import android.view.View
import androidx.annotation.CallSuper
import com.airbnb.epoxy.EpoxyHolder

abstract class BaseHolder : EpoxyHolder() {
  protected open lateinit var context: Context
  open lateinit var itemView: View
  @CallSuper
  override fun bindView(itemView: View) {
    context = itemView.context
    this.itemView = itemView
  }
}