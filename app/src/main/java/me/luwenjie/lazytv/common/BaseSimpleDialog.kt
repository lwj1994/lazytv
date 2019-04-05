package me.luwenjie.lazytv.common

import android.annotation.TargetApi
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.airbnb.mvrx.MvRxView
import com.airbnb.mvrx.MvRxViewModelStore
import java.util.UUID

abstract class BaseSimpleDialog : DialogFragment(), MvRxView {
    override fun invalidate() {
    }

    override val mvrxViewModelStore by lazy { MvRxViewModelStore(viewModelStore) }
    private lateinit var mvrxPersistedViewId: String
    final override val mvrxViewId: String by lazy { mvrxPersistedViewId }


    override fun onCreate(savedInstanceState: Bundle?) {
        mvrxViewModelStore.restoreViewModels(this, savedInstanceState)
        mvrxPersistedViewId = "${savedInstanceState?.getString(PERSISTED_VIEW_ID_KEY)
            ?: this::class.java.simpleName}_${UUID.randomUUID()}"

        super.onCreate(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mvrxViewModelStore.saveViewModels(outState)
    }

    @TargetApi(Build.VERSION_CODES.M)
    protected fun setLightStatusBar(window: Window?) {
        if (window == null) return
        if (supportsImmersion) {
            val flags = window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.decorView.systemUiVisibility = flags
            window.statusBarColor = Color.WHITE
        }
    }

    val supportsImmersion: Boolean
        get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M

}

private const val PERSISTED_VIEW_ID_KEY = "mvrx:persisted_view_id_BaseSimpleDialog"