package me.luwenjie.lazytv.common.bottomview

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.Config
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.Px

/**
 * @author Wenchieh.Lu  2018/5/8
 *
 * A child View for [BottomBar].
 * rewrite in kotlin from:
 *                    https://github.com/yingLanNull/AlphaTabsIndicator/blob/6ccff9e1a226755226559c61b593f1a41230813e/library/src/main/java/com/yinglan/alphatabs/BottomTab.java
 *
 */
class BottomTab @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    var text: String = "",
    var iconNormal: Int = 0,
    var iconSelected: Int = 0,
    var padding: Float = 0f,
    var textSize: Float = 0f,
    var textColorNormal: Int = Color.BLACK,
    var textColorSelected: Int = Color.RED,
    var badgeBackgroundColor: Int = Color.RED,
    var badgeNumber: Int = 0,
    var isShowPoint: Boolean = false,
    var iconNormalBt: Bitmap? = null,
    var iconSelectedBt: Bitmap? = null,
    var tabPaddingTop: Int = 0,
    var tabPaddingBottom: Int = 0,
    var badgeTextColor: Int = Color.WHITE) : View(context, attrs, defStyleAttr) {

  private var mAlpha = 0
  private lateinit var mIconPaint: Paint
  private lateinit var mTextPaint: Paint
  private lateinit var mIconAvailableRect: Rect
  private lateinit var mIconDrawRect: Rect
  private lateinit var mTextBound: Rect


  private lateinit var badgeBgPaint: Paint
  private lateinit var badgeTextPaint: Paint
  private lateinit var badgeCanvas: Canvas
  private lateinit var badgeRF: RectF

  init {
    Log.d(TAG, "init")
    if (iconNormalBt == null) {
      iconNormalBt = if (iconNormal == 0) null else getDrawable(iconNormal)?.toBitmap()
    }
    if (iconSelectedBt == null) {
      iconSelectedBt = if (iconSelected == 0) null else getDrawable(
          iconSelected)?.toBitmap()
    }
    if (padding == 0f) padding = dp2px(5f)
    if (textSize == 0f) textSize = sp2px(12f)

    initDrawTools()
    setPadding(0, tabPaddingTop, 0, tabPaddingBottom)

    val typeValue = TypedValue()
    context.theme
        .resolveAttribute(android.R.attr.selectableItemBackground, typeValue, true)
    val attribute = intArrayOf(android.R.attr.selectableItemBackground)
    val typedArray = context.theme.obtainStyledAttributes(typeValue.resourceId, attribute);
    background = typedArray.getDrawable(0)
  }

  fun updateTextBounds() {
    mTextPaint.getTextBounds(text, 0, text.length, mTextBound)
  }

  private fun initDrawTools() {
    mIconPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        .apply {
          isFilterBitmap = true
          alpha = this@BottomTab.mAlpha
        }
    mIconAvailableRect = Rect()
    mIconDrawRect = Rect()
    mTextBound = Rect()
    mTextPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        .apply {
          textSize = this@BottomTab.textSize
          color = textColorNormal
          isDither = true
          getTextBounds(text, 0, text.length, mTextBound)
        }

    if (TextUtils.isEmpty(text)) {
      padding = 0f
    }
    badgeBgPaint = Paint().apply {
      color = badgeBackgroundColor
      isAntiAlias = true
    }
    badgeTextPaint = Paint().apply {
      color = Color.WHITE
      textSize = dp2px(textSize, context)
      isAntiAlias = true
      textAlign = Paint.Align.CENTER
      typeface = Typeface.DEFAULT_BOLD
    }
    badgeCanvas = Canvas()
    badgeRF = RectF()
  }

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    Log.d(TAG, "onMeasure")

    val availableWidth = measuredWidth - paddingLeft - paddingRight
    val availableHeight: Int = (measuredHeight - paddingTop - paddingBottom - mTextBound.height() - padding).toInt()


    mIconAvailableRect.set(paddingLeft, paddingTop, paddingLeft + availableWidth,
        paddingTop + availableHeight)

    val textLeft = paddingLeft + (availableWidth - mTextBound.width()) / 2
    val textTop = mIconAvailableRect.bottom + padding.toInt()
    mTextBound.set(textLeft, textTop, textLeft + mTextBound.width(),
        textTop + mTextBound.height())

  }

  override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
    super.onLayout(changed, left, top, right, bottom)
    Log.d(TAG, "onLayout")
  }

  override fun onDraw(canvas: Canvas) {
    super.onDraw(canvas)

    // draw icon
    mIconAvailableRect.availableToDrawRect(
        iconNormalBt ?: throw IllegalArgumentException("you must set iconNormal"))
    mIconPaint.alpha = 255 - mAlpha
    canvas.drawBitmap(iconNormalBt, null, mIconAvailableRect, mIconPaint)

    mIconAvailableRect.availableToDrawRect(
        iconSelectedBt ?: throw IllegalArgumentException("you must set iconSelected"))
    mIconPaint.alpha = mAlpha
    canvas.drawBitmap(iconSelectedBt, null, mIconAvailableRect, mIconPaint)

    // draw text
    // text's real height  = mTextBound.height() + mFmi.bottom
    if (!TextUtils.isEmpty(text)) {
      mTextPaint.apply {
        color = textColorNormal
        alpha = 255 - mAlpha
      }
      canvas.drawText(text, mTextBound.left.toFloat(),
          (mTextBound.bottom - mTextPaint.fontMetricsInt.bottom / 2).toFloat(), mTextPaint)

      mTextPaint.apply {
        color = textColorSelected
        alpha = mAlpha
      }
      canvas.drawText(text, mTextBound.left.toFloat(),
          (mTextBound.bottom - mTextPaint.fontMetricsInt.bottom / 2).toFloat(), mTextPaint)
    }

    // draw badge
    drawBadge(canvas)
  }

  /**
   * draw badge
   */
  private fun drawBadge(canvas: Canvas) {
    var i = measuredWidth / 14
    val j = measuredHeight / 9
    i = if (i >= j) j else i


    val left = measuredWidth / 10 * 6f
    val top = tabPaddingTop.toFloat()
    // if showPoint, don't show number
    if (isShowPoint) {
      i = if (i > 10) 10 else i
      val width = dp2px(i.toFloat())
      badgeRF.set(left, top, left + width, top + width)
      canvas.drawOval(badgeRF, badgeBgPaint)
      return
    }

    if (badgeNumber > 0) {
      badgeTextPaint.apply {
        textSize = dp2px(if (i / 1.5f == 0f) 5f else i / 1.5f)
        color = badgeTextColor
      }
      val number = if (badgeNumber > 99) "99+" else badgeNumber.toString()
      val width: Int
      val height = dp2px(i.toFloat()).toInt()
      val bitmap: Bitmap
      when {
        number.length == 1 -> {
          width = dp2px(i.toFloat()).toInt()
          bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        }
        number.length == 2 -> {
          width = dp2px((i + 5).toFloat()).toInt()
          bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        }
        else -> {
          width = dp2px((i + 8).toFloat()).toInt()
          bitmap = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888)
        }
      }

      badgeRF.set(0f, 0f, width.toFloat(), height.toFloat())
      badgeCanvas.apply {
        setBitmap(bitmap)
        drawRoundRect(badgeRF, 50f, 50f, badgeBgPaint) //画椭圆
      }

      val fontMetrics = badgeTextPaint.fontMetrics
      val x = width / 2f
      val y = height / 2f - fontMetrics.descent + (fontMetrics.descent - fontMetrics.ascent) / 2

      badgeCanvas.drawText(number, x, y, badgeTextPaint)
      canvas.drawBitmap(bitmap, left, top, null)
      bitmap.recycle()
    }
  }

  private fun dp2px(value: Float) =
      dp2px(value, context)

  private fun sp2px(value: Float) =
      sp2px(value, context)

  private fun Rect.availableToDrawRect(bitmap: Bitmap) {
    var dx = 0f
    var dy = 0f
    val wRatio = width() * 1.0f / bitmap.width
    val hRatio = height() * 1.0f / bitmap.height
    if (wRatio > hRatio) {
      dx = (width() - hRatio * bitmap.width) / 2
    } else {
      dy = (height() - wRatio * bitmap.height) / 2
    }
    val left = (left.toFloat() + dx + 0.5f).toInt()
    val top = (top.toFloat() + dy + 0.5f).toInt()
    val right = (right - dx + 0.5f).toInt()
    val bottom = (bottom - dy + 0.5f).toInt()
    set(left, top, right, bottom)
  }

  fun showBadgePoint(show: Boolean) {
    if (show == isShowPoint) {
      return
    }
    isShowPoint = show
    invalidate()
  }

  fun clearBadge() {
    if (!isShowPoint) return
    isShowPoint = false
    badgeNumber = -1
    postInvalidate()
  }

  fun showBadgeNumber(num: Int) {
    if (num <= 0) {
      throw IllegalArgumentException("num must > 0")
    }
    badgeNumber = num
    isShowPoint = false
    postInvalidate()
  }

  override fun setSelected(selected: Boolean) {
    super.setSelected(selected)
    mAlpha = if (selected) 255 else 0
    invalidate()
  }


  override fun onSaveInstanceState(): Parcelable {
    Log.d(TAG, "onSaveInstanceState")
    return SavedState(super.onSaveInstanceState()).apply {
      text = this@BottomTab.text
      padding = this@BottomTab.padding
      textSize = this@BottomTab.textSize
      iconNormal = this@BottomTab.iconNormal
      iconSelected = this@BottomTab.iconSelected
      isSelected = this@BottomTab.isSelected
    }
  }


  override fun onRestoreInstanceState(state: Parcelable?) {
    Log.d(TAG, "onRestoreInstanceState")
    if (state == null || state !is SavedState) {
      super.onRestoreInstanceState(state)
      return
    }
    super.onRestoreInstanceState(state.superState)
    restoreState(state)
  }

  private fun restoreState(state: SavedState) {
    Log.d(TAG, state.text)
    this.text = state.text
    this.padding = state.padding
    this.iconNormal = state.iconNormal
    this.iconSelected = state.iconSelected
    this.textSize = state.textSize
    requestLayout()
    isSelected = state.isSelected
  }

  private class SavedState : BaseSavedState {
    lateinit var text: String
    var padding = 0f
    var iconNormal = 0
    var iconSelected = 0
    var textSize = 0f
    var isSelected = false

    constructor(superState: Parcelable) : super(superState)

    constructor(parcel: Parcel) : super(parcel) {
      text = parcel.readString()
      padding = parcel.readFloat()
      iconNormal = parcel.readInt()
      iconSelected = parcel.readInt()
      textSize = parcel.readFloat()
      isSelected = parcel.readByte() != 0.toByte()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
      super.writeToParcel(parcel, flags)
      parcel.writeString(text)
      parcel.writeFloat(padding)
      parcel.writeInt(iconNormal)
      parcel.writeInt(iconSelected)
      parcel.writeFloat(textSize)
      parcel.writeByte(if (isSelected) 1 else 0)
    }

    override fun describeContents(): Int {
      return 0
    }

    companion object CREATOR : Creator<SavedState> {
      override fun createFromParcel(parcel: Parcel): SavedState {
        return SavedState(parcel)
      }

      override fun newArray(size: Int): Array<SavedState?> {
        return arrayOfNulls(size)
      }
    }
  }

  companion object {
    private const val TAG = "BottomTab"
  }

  private fun Drawable.toBitmap(
      @Px width: Int = intrinsicWidth,
      @Px height: Int = intrinsicHeight,
      config: Config? = null
  ): Bitmap {
    if (this is BitmapDrawable) {
      if (config == null || bitmap.config == config) {
        // Fast-path to return original. Bitmap.createScaledBitmap will do this check, but it
        // involves allocation and two jumps into native code so we perform the check ourselves.
        if (width == intrinsicWidth && height == intrinsicHeight) {
          return bitmap
        }
        return Bitmap.createScaledBitmap(bitmap, width, height, true)
      }
    }

    val oldLeft = bounds.left
    val oldTop = bounds.top
    val oldRight = bounds.right
    val oldBottom = bounds.bottom


    val bitmap = Bitmap.createBitmap(width, height, config ?: Config.ARGB_8888)
    setBounds(0, 0, width, height)
    draw(Canvas(bitmap))

    setBounds(oldLeft, oldTop, oldRight, oldBottom)
    return bitmap
  }


  private fun getDrawable(@DrawableRes drawableRes: Int): Drawable? =
      if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
        context.getDrawable(drawableRes)
      } else {
        context.resources.getDrawable(drawableRes)
      }


  class Builder(private val context: Context) {
    private var text: String = ""
    private var textSize = 0f
    private var iconNormal: Int = 0
    private var iconSelected: Int = 0
    private var iconNormalBt: Bitmap? = null
    private var iconSelectedBt: Bitmap? = null
    private var textColorNormal = 0
    private var textColorSelected = 0
    private var padding = 0f
    private var badgeBackgroundColor = 0
    private var badgeNumber = 0
    private var isShowPoint = false
    private var badgeTextColor = Color.WHITE
    private var tabPaddingTop = 0
    private var tabPaddingBottom = 0


    init {
      if (padding == 0f) padding = dp2px(5f, context)
      if (textSize == 0f) textSize = sp2px(12f, context)
    }

    fun tabPadding(top: Int, bottom: Int) = apply {
      this.tabPaddingTop = top
      this.tabPaddingBottom = bottom
    }

    fun text(text: String) =
        apply {
          this.text = text
        }


    fun textSize(textSize: Float) =
        apply {
          this.textSize = textSize
        }


    fun iconNormal(iconNormal: Int) =
        apply {
          this.iconNormal = iconNormal
          this.iconNormalBt = null
        }


    fun iconSelected(iconSelected: Int) =
        apply {
          this.iconSelected = iconSelected
          this.iconSelectedBt = null;
        }


    fun iconNormalBt(iconNormalBt: Bitmap?) =
        apply {
          this.iconNormalBt = iconNormalBt
          this.iconNormal = 0
        }

    fun iconSelectedBt(iconSelectedBt: Bitmap?) =
        apply {
          this.iconSelectedBt = iconSelectedBt
          this.iconSelected = 0
        }


    fun textColorNormal(textColorNormal: Int) =
        apply {
          this.textColorNormal = textColorNormal
        }


    fun textColorSelected(textColorSelected: Int) =
        apply {
          this.textColorSelected = textColorSelected
        }


    fun padding(padding: Float) =
        apply {
          this.padding = padding
        }

    fun badgeBackgroundColor(badgeBackgroundColor: Int) =
        apply {
          this.badgeBackgroundColor = badgeBackgroundColor
        }


    fun badgeNumber(badgeNumber: Int) =
        apply {
          this.badgeNumber = badgeNumber
        }


    fun isShowPoint(isShowPoint: Boolean) =
        apply {
          this.isShowPoint = isShowPoint
        }

    fun badgeTextColor(color: Int) =
        apply {
          this.badgeTextColor = color
        }

    fun build() = BottomTab(context,
        iconNormal = iconNormal,
        iconSelected = iconSelected,
        iconNormalBt = iconNormalBt,
        iconSelectedBt = iconSelectedBt,
        text = text,
        padding = padding,
        textSize = textSize,
        textColorNormal = textColorNormal,
        textColorSelected = textColorSelected,
        badgeBackgroundColor = badgeBackgroundColor,
        badgeNumber = badgeNumber,
        isShowPoint = isShowPoint,
        badgeTextColor = badgeTextColor,
        tabPaddingTop = tabPaddingTop,
        tabPaddingBottom = tabPaddingBottom)
  }


  fun newBuilder() = Builder(context).apply {
    iconNormal(iconNormal)
    iconSelected(iconSelected)
    iconNormalBt(iconNormalBt)
    iconSelectedBt(iconSelectedBt)
    text(text)
    padding(padding)
    textSize(textSize)
    textColorNormal(textColorNormal)
    textColorSelected(textColorSelected)
    badgeBackgroundColor(badgeBackgroundColor)
    badgeNumber(badgeNumber)
    isShowPoint(isShowPoint)
    tabPadding(tabPaddingTop, tabPaddingBottom)
  }
}

private fun dp2px(value: Float, context: Context) =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, context.resources.displayMetrics)


private fun sp2px(value: Float, context: Context) =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, value, context.resources.displayMetrics)
