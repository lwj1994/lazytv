package me.luwenjie.lazytv.main.subscribe.channel

import android.view.View
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.EpoxyModelClass
import com.xiachufang.lanfan.common.core.BaseHolder
import me.luwenjie.lazytv.R
import me.luwenjie.lazytv.SubscribeModel.SubscribeChannel
import me.luwenjie.lazytv.common.BaseModelWithHolder
import me.luwenjie.lazytv.common.bindItemView
import me.luwenjie.lazytv.main.subscribe.channel.SubscribeChannelView.Holder


/**
 * @author luwenjie on 2019/3/17 12:35:19
 */
@EpoxyModelClass(layout = R.layout.view_channel)
abstract class SubscribeChannelView : BaseModelWithHolder<Holder>() {

  @EpoxyAttribute
  lateinit var model: SubscribeChannel
  @EpoxyAttribute
  lateinit var onClickListener: View.OnClickListener


  override fun bind(holder: Holder) {
    super.bind(holder)
    holder.name.text = model.name
    holder.itemView.setOnClickListener(onClickListener)
  }

  override fun bind(holder: Holder, previouslyBoundModel: EpoxyModel<*>) {
    super.bind(holder, previouslyBoundModel)
  }

  override fun unbind(holder: Holder) {
    super.unbind(holder)
    holder.itemView.setOnClickListener(null)
  }

  class Holder : BaseHolder() {
    val name: TextView by bindItemView(R.id.view_channel_name)
  }

  companion object {
    private const val TAG = "ChannelView"

  }
}