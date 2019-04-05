package me.luwenjie.lazytv.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import android.text.TextUtils;
import java.io.File;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import me.luwenjie.lazytv.App;
import org.jetbrains.annotations.NotNull;

public final class DeviceUtils {

  private DeviceUtils() {
    throw new UnsupportedOperationException("u can't instantiate me...");
  }

  /**
   * Return whether device is rooted.
   *
   * @return {@code true}: yes<br>{@code false}: no
   */
  public static boolean isDeviceRooted() {
    String su = "su";
    String[] locations = {
        "/system/bin/", "/system/xbin/", "/sbin/", "/system/sd/xbin/", "/system/bin/failsafe/",
        "/data/local/xbin/", "/data/local/bin/", "/data/local/"
    };
    for (String location : locations) {
      if (new File(location + su).exists()) {
        return true;
      }
    }
    return false;
  }

  /**
   * Return the version name of device's system.
   *
   * @return the version name of device's system
   */
  public static String getSDKVersionName() {
    return Build.VERSION.RELEASE;
  }

  /**
   * Return version code of device's system.
   *
   * @return version code of device's system
   */
  public static int getSDKVersionCode() {
    return Build.VERSION.SDK_INT;
  }

  /**
   * Return the android id of device.
   *
   * @return the android id of device
   */
  @SuppressLint("HardwareIds") public static String getAndroidID() {
    String id =
        Settings.Secure.getString(App.context.getContentResolver(), Settings.Secure.ANDROID_ID);
    return id == null ? "" : id;
  }

  private static boolean isAddressNotInExcepts(final String address, final String... excepts) {
    if (excepts == null || excepts.length == 0) {
      return !"02:00:00:00:00:00".equals(address);
    }
    for (String filter : excepts) {
      if (address.equals(filter)) {
        return false;
      }
    }
    return true;
  }

  @SuppressLint({ "HardwareIds", "MissingPermission" })
  private static String getMacAddressByWifiInfo() {
    try {
      Context context = App.context.getApplicationContext();
      WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
      if (wifi != null) {
        WifiInfo info = wifi.getConnectionInfo();
        if (info != null) return info.getMacAddress();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "";
  }

  private static String getMacAddressByNetworkInterface() {
    try {
      Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
      while (nis.hasMoreElements()) {
        NetworkInterface ni = nis.nextElement();
        if (ni == null || !ni.getName().equalsIgnoreCase("wlan0")) continue;
        byte[] macBytes = ni.getHardwareAddress();
        if (macBytes != null && macBytes.length > 0) {
          StringBuilder sb = new StringBuilder();
          for (byte b : macBytes) {
            sb.append(String.format("%02x:", b));
          }
          return sb.substring(0, sb.length() - 1);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "";
  }

  private static String getMacAddressByInetAddress() {
    try {
      InetAddress inetAddress = getInetAddress();
      if (inetAddress != null) {
        NetworkInterface ni = NetworkInterface.getByInetAddress(inetAddress);
        if (ni != null) {
          byte[] macBytes = ni.getHardwareAddress();
          if (macBytes != null && macBytes.length > 0) {
            StringBuilder sb = new StringBuilder();
            for (byte b : macBytes) {
              sb.append(String.format("%02x:", b));
            }
            return sb.substring(0, sb.length() - 1);
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "";
  }

  private static InetAddress getInetAddress() {
    try {
      Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
      while (nis.hasMoreElements()) {
        NetworkInterface ni = nis.nextElement();
        // To prevent phone of xiaomi return "10.0.2.15"
        if (!ni.isUp()) continue;
        Enumeration<InetAddress> addresses = ni.getInetAddresses();
        while (addresses.hasMoreElements()) {
          InetAddress inetAddress = addresses.nextElement();
          if (!inetAddress.isLoopbackAddress()) {
            String hostAddress = inetAddress.getHostAddress();
            if (hostAddress.indexOf(':') < 0) return inetAddress;
          }
        }
      }
    } catch (SocketException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Return the manufacturer of the product/hardware.
   * <p>e.g. Xiaomi</p>
   *
   * @return the manufacturer of the product/hardware
   */
  public static String getManufacturer() {
    return Build.MANUFACTURER;
  }

  /**
   * Return the model of device.
   * <p>e.g. MI2SC</p>
   *
   * @return the model of device
   */
  public static String getModel() {
    String model = Build.MODEL;
    if (model != null) {
      model = model.trim().replaceAll("\\s*", "");
    } else {
      model = "";
    }
    return model;
  }

  /**
   * Return an ordered list of ABIs supported by this device. The most preferred ABI is the first
   * element in the list.
   *
   * @return an ordered list of ABIs supported by this device
   */
  public static String[] getABIs() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      return Build.SUPPORTED_ABIS;
    } else {
      if (!TextUtils.isEmpty(Build.CPU_ABI2)) {
        return new String[] { Build.CPU_ABI, Build.CPU_ABI2 };
      }
      return new String[] { Build.CPU_ABI };
    }
  }

  /**
   * Reboot the device.
   * <p>Requires root permission
   * or hold {@code android:sharedUserId="android.uid.system"},
   * {@code <uses-permission android:name="android.permission.REBOOT" />}</p>
   *
   * @param reason code to pass to the kernel (e.g., "recovery") to
   * request special boot modes, or null.
   */
  public static void reboot(final String reason) {
    PowerManager mPowerManager = (PowerManager) App.context.getSystemService(Context.POWER_SERVICE);
    try {
      if (mPowerManager == null) return;
      mPowerManager.reboot(reason);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 手机厂商
   */
  public static String manufacturers() {
    return Build.MANUFACTURER;
  }

  /**
   * 获取应用程序的版本号
   */
  public static String appVersion() {
    // 得到包管理器
    PackageManager pm = App.context.getPackageManager();
    try {
      PackageInfo packageInfo = pm.getPackageInfo(App.context.getPackageName(), 0);
      return packageInfo.versionName;
    } catch (Exception e) {
      // can't reach
      e.printStackTrace();
      return null;
    }
  }

  /**
   * 获取设备的唯一标识符，优先通过获取 mac 地址，因为硬件的唯一性比较靠谱
   * 暂时没有使用电话权限。
   */
  @NotNull public static String getDeviceId() {

    String wifiMac = getMacAddressByWifiInfo();
    if (!TextUtils.isEmpty(wifiMac) && !wifiMac.equals("02:00:00:00:00:00")) {
      return LCryptoUtil.md5(wifiMac).toUpperCase();
    }
    String networkMac = getMacAddressByNetworkInterface();
    if (!TextUtils.isEmpty(networkMac) && !networkMac.equals("02:00:00:00:00:00")) {
      return LCryptoUtil.md5(networkMac).toUpperCase();
    }
    String iNetMac = getMacAddressByInetAddress();
    if (!TextUtils.isEmpty(iNetMac) && !iNetMac.equals("02:00:00:00:00:00")) {
      return LCryptoUtil.md5(iNetMac).toUpperCase();
    }

    // 并不靠谱的 Android ID，手机升级或者 wipe 后会重新生成
    String androidID = getAndroidID();
    if (!TextUtils.isEmpty(androidID)) {
      return LCryptoUtil.md5(androidID).toUpperCase();
    }

    return "";
  }
}