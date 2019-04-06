package me.luwenjie.lazytv.util

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

object SharedPreferencesRegistry {
  private const val PREF_USER = "user"

  private fun getUserRegistry(context: Context): SharedPreferences =
      context.getSharedPreferences(PREF_USER, Context.MODE_PRIVATE)

  internal fun getIsFirstIntoApp(context: Context): Boolean {
    val userRegistry = getUserRegistry(context)
    return userRegistry.getBoolean("isFirstIntoApp", true)
  }

  internal fun setIsFirstIntoApp(context: Context, isFirst: Boolean) {
    val userRegistry = getUserRegistry(context)
    userRegistry.edit {
      putBoolean("isFirstIntoApp", isFirst)
    }
  }
}