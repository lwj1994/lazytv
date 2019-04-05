package me.luwenjie.lazytv.common

import com.airbnb.epoxy.EpoxyModelWithHolder
import com.xiachufang.lanfan.common.core.BaseHolder

/**
 * @author luwenjie on 2019/3/17 12:22:08
 */
abstract class BaseModelWithHolder<T:BaseHolder>: EpoxyModelWithHolder<T>(){


    companion object {
        private const val TAG = "BaseModelWithHolder"
    }
}