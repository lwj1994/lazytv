package me.luwenjie.lazytv

import android.content.Context
import android.util.Log
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Transaction
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.WorkerParameters
import com.google.common.io.ByteStreams
import com.google.gson.Gson
import io.reactivex.Observable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import java.util.UUID


const val DATABASE_NAME = "ChannelDb"

@Database(entities = [GroupModel::class, ChannelModel::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
  abstract fun channelDao(): ChannelDao

  companion object {
    // For Singleton instantiation
    @Volatile
    private var instance: AppDatabase? = null

    fun getInstance(context: Context = App.context): AppDatabase {
      return instance ?: synchronized(this) {
        instance ?: buildDatabase(context).also { instance = it }
      }
    }

    // Create and pre-populate the database. See this article for more details:
    // https://medium.com/google-developers/7-pro-tips-for-room-fbadea4bfbd1#4785
    private fun buildDatabase(context: Context): AppDatabase {
      return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
          .addCallback(object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
              super.onCreate(db)
//                        val request = OneTimeWorkRequest.Builder(SeedDatabaseWorker::class.java).build()
//                        WorkManager.getInstance().enqueue(request)
            }
          })
          .build()
    }
  }
}


@Dao
interface ChannelDao {
  @Query("SELECT * FROM ChannelGroups WHERE name = :name")
  fun getGroup(name: String): GroupModel?

  @Query("SELECT * FROM ChannelGroups ORDER BY oderId")
  fun getGroups(): List<GroupModel>

  @Query(
      "SELECT * FROM channel WHERE group_id = :groupId ORDER BY createTime LIMIT :size OFFSET :page * :size")
  fun getChannelByGroupId(groupId: String, page: Int,
      size: Int = 15): Observable<List<ChannelModel>>

  @Query("SELECT * FROM channel WHERE id = :channelId")
  fun getChannel(channelId: String): ChannelModel?

  @Transaction
  @Query("SELECT * FROM ChannelGroups WHERE id = :groupId")
  fun getGroupAndChildren(groupId: String): List<GroupModelAndChildChannels>

  @Transaction
  @Query("SELECT * FROM ChannelGroups")
  fun loadAll(): Observable<List<GroupModelAndChildChannels>>

  @Query("SELECT COUNT(*) FROM ChannelGroups")
  fun getGroupCount(): Int

  @Transaction
  fun insertGroup(model: GroupModel) {
    insertGroupModel(model)
    insertChannelModel(model.childChannels)
  }

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insertGroupModel(model: GroupModel)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  fun insertChannelModel(models: List<ChannelModel>)
}


class SeedDatabaseWorker(
    context: Context = App.context,
    workerParams: WorkerParameters
) : androidx.work.CoroutineWorker(context, workerParams) {
  companion object {
    private val TAG by lazy { SeedDatabaseWorker::class.java.simpleName }

  }

  override val coroutineContext = Dispatchers.IO
  private val gson by lazy { Gson() }
  override suspend fun doWork(): Result = coroutineScope {

    try {
      applicationContext.assets.open("asia.json").use { inputStream ->
        val str = String(ByteStreams.toByteArray(inputStream))
        val content = gson.fromJson(str, ChannelContent::class.java)
        val groupId = UUID.fromString("${content.title}${content.uuid}").toString()

        AppDatabase.getInstance(applicationContext).channelDao().insertGroup(
            GroupModel(groupId, name = content.title, oderId = 0,
                childChannels = content.channels.map {
                  ChannelModel(
                      id = UUID.fromString("${it.name}${it.url}").toString(),
                      groupId = groupId,
                      url = it.url,
                      name = it.name,
                      groupName = "asia"
                  )
                })
        )
        Result.success()
      }
    } catch (ex: Exception) {
      Log.e(TAG, "Error seeding database", ex)
      Result.failure()
    }
  }
}