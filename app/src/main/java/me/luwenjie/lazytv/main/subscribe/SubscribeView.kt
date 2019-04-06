package me.luwenjie.lazytv.main.subscribe

import android.view.View
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyAttribute.Option.DoNotHash
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.EpoxyModelClass
import com.xiachufang.lanfan.common.core.BaseHolder
import me.luwenjie.lazytv.GroupModel
import me.luwenjie.lazytv.R
import me.luwenjie.lazytv.common.BaseModelWithHolder
import me.luwenjie.lazytv.common.bindItemView
import me.luwenjie.lazytv.main.subscribe.SubscribeView.Holder


/**
 * @author luwenjie on 2019/4/6 14:43:03
 */
@EpoxyModelClass(layout = R.layout.view_subscribe)
abstract class SubscribeView : BaseModelWithHolder<Holder>() {

  @EpoxyAttribute
  lateinit var model: GroupModel
  @EpoxyAttribute(DoNotHash)
  lateinit var onClickItem: View.OnClickListener

  @EpoxyAttribute(DoNotHash)
  lateinit var onLongClickItem: View.OnLongClickListener

  override fun bind(holder: Holder) {
    super.bind(holder)
    holder.textView.text = model.name
    holder.textView.setOnClickListener(onClickItem)
    holder.textView.setOnLongClickListener(onLongClickItem)
  }

  override fun bind(holder: Holder, previouslyBoundModel: EpoxyModel<*>) {
    super.bind(holder, previouslyBoundModel)
  }

  override fun unbind(holder: Holder) {
    super.unbind(holder)
    holder.textView.setOnClickListener(null)
    holder.textView.setOnLongClickListener(null)
  }

  class Holder : BaseHolder() {
    val textView by bindItemView<TextView>(R.id.view_subscribe_TextView)
  }

  companion object {
    private const val TAG = "SubscribeView"

  }
}