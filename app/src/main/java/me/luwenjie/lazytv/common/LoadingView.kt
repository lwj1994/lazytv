package me.luwenjie.lazytv.common

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.airbnb.epoxy.ModelView
import me.luwenjie.lazytv.R
import kotlin.LazyThreadSafetyMode.NONE

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_WRAP_HEIGHT)
class LoadingView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
  private val progressBar: ProgressBar by lazy(NONE) {
    findViewById<ProgressBar>(R.id.view_loading_ProgressBar)
  }
  private val textView: TextView by lazy(NONE) {
    findViewById<TextView>(R.id.view_loading_TextView)
  }

  init {
    inflate(context, R.layout.view_loading, this)
  }

}