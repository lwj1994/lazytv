package me.luwenjie.lazytv

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.util.UUID

data class SubscribeModel(
    val channels: List<SubscribeChannel>,
    val title: String,
    val uuid: String
) {
  data class SubscribeChannel(
      val name: String,
      val url: String
  )
}

@Entity(
    tableName = "Channel",
    foreignKeys = [ForeignKey(
        entity = GroupModel::class, parentColumns = ["id"],
        childColumns = ["group_id"], onUpdate = CASCADE, onDelete = CASCADE
    )]
)
data class ChannelModel(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "group_id", index = true)
    val groupId: String,
    val url: String,
    val name: String,
    val groupName: String,
    val image: String = "",
    val createTime: Long = System.currentTimeMillis()
)


@Entity(tableName = "ChannelGroups")
data class GroupModel(
    @PrimaryKey
    val id: String,
    val name: String,
    val url: String,
    val oderId: Int = Int.MAX_VALUE,
    @Ignore
    val childChannels: List<ChannelModel> = emptyList()
) {
  constructor(
      id: String,
      name: String,
      url: String,
      oderId: Int = Int.MAX_VALUE
  ) : this(id, name, url, oderId, emptyList())

}

data class GroupModelAndChildChannels(
    @Embedded
    val groupModel: GroupModel,
    @Relation(
        parentColumn = "id",
        entityColumn = "group_id", entity = ChannelModel::class
    )
    val childChannels: List<ChannelModel>
)


data class LazytvResult(val code: Int = SUCCESS, val msg: String) {
  companion object {
    const val SUCCESS = 200
    const val FAIL = 400
  }
}

fun generateUUID(vararg texts: String): String {
  val sb = StringBuilder(UUID.randomUUID().toString())
  texts.forEach {
    sb.append("-")
    sb.append(it)
    sb.append("-")
  }
  return sb.toString()
}

