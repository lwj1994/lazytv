package me.luwenjie.lazytv

import androidx.test.core.app.ApplicationProvider
import org.junit.Assert
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class DbTest {
    val lcDataBase by lazy { AppDatabase.getInstance(ApplicationProvider.getApplicationContext()) }


    @Test
    fun testGetDAOs() {
        val lcDataBase = AppDatabase.getInstance(ApplicationProvider.getApplicationContext())
        Assert.assertNotNull(lcDataBase.channelDao())
    }

    @Test
    fun getGroups() {
        val list = lcDataBase.channelDao().getGroups()
        println(list.size)
        list.forEach {
            println(it.name)
        }

    }


    @Test
    fun getChannelByGroupId(){
        val list = lcDataBase.channelDao().getChannelByGroupId("2",1)
        println(list)
    }

    @Test
    fun getChannel(){
        val list = lcDataBase.channelDao().getChannel("2131")
        println(list)
    }

    @Test
    fun getGroupAndChildren() {
        val groupAndChildren = lcDataBase.channelDao().getGroupAndChildren("1")
        println(groupAndChildren.toString())
    }

    @Test
    fun insert() {
        lcDataBase.channelDao().insertGroup(
            GroupModel(
                "1", "asia", 1, arrayListOf(
                    ChannelModel(
                        "2131", "1", "weqeqweqwe", "啊而且二tv", "asia", "wqeqweq", 1
                    )
                )
            )
        )
    }
}