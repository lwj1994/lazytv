package me.luwenjie.lazytv.main

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.airbnb.mvrx.MvRx
import com.airbnb.mvrx.activityViewModel
import com.airbnb.mvrx.args
import kotlinx.android.parcel.Parcelize
import me.luwenjie.lazytv.R
import me.luwenjie.lazytv.common.BaseSimpleDialog
import me.luwenjie.lazytv.main.GroupDialog.GroupDialogAdapter.Holder

/**
 * @author luwenjie on 2019/3/21 00:18:23
 */
class GroupDialog : BaseSimpleDialog() {
  private val viewModel: MainViewModel by activityViewModel()
  private val argss by args<GroupDialogArgs>()
  private lateinit var recyclerView: RecyclerView
  private lateinit var adapter: GroupDialogAdapter
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.dialog_channel_group, container, false).apply {
      recyclerView = findViewById(R.id.dialog_channel_group_RecyclerView)
      adapter = GroupDialogAdapter(mutableListOf())
      recyclerView.layoutManager = LinearLayoutManager(requireContext())
      recyclerView.adapter = adapter
      viewModel.groupLive.observe(this@GroupDialog, Observer {
        adapter.list.clear()
        adapter.list.addAll(it.map { g ->
          GroupDialogModel(g.name, argss.selectId == g.id)
        })
        adapter.notifyDataSetChanged()
      })
      viewModel.getGroups()
    }
  }

  companion object {
    private const val TAG = "GroupDialog"
    fun newInstance(selectId: String) = GroupDialog().apply {
      arguments = Bundle().apply {
        putParcelable(MvRx.KEY_ARG, GroupDialogArgs(selectId))
      }
    }
  }

  private class GroupDialogAdapter(
      val list: MutableList<GroupDialogModel>) : RecyclerView.Adapter<Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
      return Holder(View.inflate(parent.context, R.layout.view_channel_group, null))
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
      holder.checkBox.isChecked = list[position].checked
      holder.checkBox.text = list[position].name
    }

    private class Holder(view: View) : ViewHolder(view) {
      val checkBox: CheckBox = view.findViewById(R.id.view_channel_group_CheckBox)
    }


  }
}


data class GroupDialogModel(val name: String, val checked: Boolean)

@Parcelize
class GroupDialogArgs(val selectId: String) : Parcelable
