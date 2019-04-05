package me.luwenjie.lazytv

import androidx.room.*
import androidx.room.ForeignKey.CASCADE

data class ChannelContent(
    val channels: List<ChannelX>,
    val title: String,
    val uuid: String
) {
    data class ChannelX(
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


data class SubscribeModel(
    val url: String,
    val name: String,
    val channels: ChannelModel
)

@Entity(tableName = "ChannelGroups")
data class GroupModel(
    @PrimaryKey
    val id: String,
    val name: String,
    val oderId: Int = Int.MAX_VALUE,
    @Ignore
    val childChannels: List<ChannelModel> = emptyList()
) {
    constructor(
        id: String,
        name: String,
        oderId: Int = Int.MAX_VALUE
    ) : this(id, name, oderId, emptyList())

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
