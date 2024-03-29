package me.luwenjie.lazytv.main.channel

import android.view.View
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyAttribute.Option.DoNotHash
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.EpoxyModelClass
import com.xiachufang.lanfan.common.core.BaseHolder
import me.luwenjie.lazytv.ChannelModel
import me.luwenjie.lazytv.R
import me.luwenjie.lazytv.common.BaseModelWithHolder
import me.luwenjie.lazytv.common.bindItemView
import me.luwenjie.lazytv.main.channel.ChannelView.Holder


/**
 * @author luwenjie on 2019/3/17 12:35:19
 */
@EpoxyModelClass(layout = R.layout.view_channel)
abstract class ChannelView : BaseModelWithHolder<Holder>() {

  @EpoxyAttribute
  lateinit var model: ChannelModel
  @EpoxyAttribute(DoNotHash)
  lateinit var onClickListener: View.OnClickListener
  @EpoxyAttribute(DoNotHash)
  lateinit var onLongClickListener: View.OnLongClickListener

  override fun bind(holder: Holder) {
    super.bind(holder)
    holder.name.text = model.name
    holder.itemView.setOnClickListener(onClickListener)
    holder.itemView.setOnLongClickListener(onLongClickListener)
  }

  override fun bind(holder: Holder, previouslyBoundModel: EpoxyModel<*>) {
    super.bind(holder, previouslyBoundModel)
  }

  override fun unbind(holder: Holder) {
    super.unbind(holder)
    holder.itemView.setOnClickListener(null)
    holder.itemView.setOnLongClickListener(null)
  }

  class Holder : BaseHolder() {
    val name: TextView by bindItemView(R.id.view_channel_name)
  }

  companion object {
    private const val TAG = "ChannelView"

  }
}