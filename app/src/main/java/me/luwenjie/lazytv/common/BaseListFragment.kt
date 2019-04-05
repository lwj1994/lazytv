package me.luwenjie.lazytv.common

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.airbnb.epoxy.*
import com.airbnb.mvrx.MvRx
import me.luwenjie.lazytv.R

/**
 * 推荐全部使用 fragment 来构建页面，activity 只作为容器。
 * 基于 epoxy, fragment 内部全部使用 recyclerView 构建页面
 */
abstract class BaseListFragment : BaseFragment() {

  protected lateinit var recyclerView: EpoxyRecyclerView
  protected lateinit var swipeRefreshLayout: SwipeRefreshLayout
  protected val epoxyController by lazy { epoxyController() }
  protected val visibilityTracker by lazy { EpoxyVisibilityTracker() }
  protected open val layoutRes = 0

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    epoxyController.onRestoreInstanceState(savedInstanceState)
  }

  override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(if (layoutRes == 0) R.layout.fragment_base_lanfan else layoutRes,
        container, false).apply {
      recyclerView = findViewById(R.id.fragment_base_lanfan_EpoxyRecyclerView)
      swipeRefreshLayout = findViewById(R.id.fragment_base_lanfan_SwipeRefreshLayout)
      recyclerView.setController(epoxyController)
      swipeRefreshLayout.setOnRefreshListener {
        onRefresh()
      }
      visibilityTracker.attach(recyclerView)
    }
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    initView(view)
    initData(savedInstanceState)
  }
  abstract fun initView(view: View)

  abstract fun initData(savedInstanceState: Bundle?)

  abstract fun onRefresh()

  override fun invalidate() {
    recyclerView.requestModelBuild()
  }

  /**
   * Provide the EpoxyController to use when building models for this Fragment.
   * Basic usages can simply use [simpleController]
   */
  abstract fun epoxyController(): EpoxyController

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    epoxyController.onSaveInstanceState(outState)
  }

  override fun onDestroyView() {
    epoxyController.cancelPendingModelBuild()
    visibilityTracker.detach(recyclerView)
    super.onDestroyView()
  }

  fun generateArgs(arg: Parcelable) =
      Bundle().apply { putParcelable(MvRx.KEY_ARG, arg) }

  protected fun showRefreshing(b: Boolean) {
    swipeRefreshLayout.post {
      swipeRefreshLayout.isRefreshing = b
    }
  }

  fun getCopyModels(): MutableList<EpoxyModel<*>> = (recyclerView.adapter as EpoxyControllerAdapter).copyOfModels
}
