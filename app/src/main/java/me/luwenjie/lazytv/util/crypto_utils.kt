@file:JvmName("LCryptoUtil")

package me.luwenjie.lazytv.util

import java.security.NoSuchAlgorithmException

private const val MD5 = "MD5"
fun md5(s: String): String {
  try {
    // Create MD5 Hash
    val digest = java.security.MessageDigest.getInstance(MD5)
    digest.update(s.toByteArray())
    val messageDigest = digest.digest()

    // Create Hex String
    val hexString = StringBuilder()
    for (aMessageDigest in messageDigest) {
      var h = Integer.toHexString(0xFF and aMessageDigest.toInt())
      while (h.length < 2)
        h = "0$h"
      hexString.append(h)
    }
    return hexString.toString()

  } catch (e: NoSuchAlgorithmException) {
    e.printStackTrace()
  }

  return ""
}
