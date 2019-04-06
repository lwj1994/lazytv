package me.luwenjie.lazytv.main

import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.EpoxyModelClass
import com.xiachufang.lanfan.common.core.BaseHolder
import me.luwenjie.lazytv.R
import me.luwenjie.lazytv.common.BaseModelWithHolder
import me.luwenjie.lazytv.main.EmptyView.Holder


/**
 * @author luwenjie on 2019/4/6 14:43:03
 */
@EpoxyModelClass(layout = R.layout.view_empty)
abstract class EmptyView : BaseModelWithHolder<Holder>() {


  override fun bind(holder: Holder) {
    super.bind(holder)
  }

  override fun bind(holder: Holder, previouslyBoundModel: EpoxyModel<*>) {
    super.bind(holder, previouslyBoundModel)
  }

  override fun unbind(holder: Holder) {
    super.unbind(holder)
  }

  class Holder : BaseHolder() {
  }

  companion object {
    private const val TAG = "EmptyView"

  }
}