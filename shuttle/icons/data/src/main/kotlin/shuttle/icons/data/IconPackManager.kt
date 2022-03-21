package shuttle.icons.data

import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import shuttle.apps.domain.AppsRepository
import java.io.IOException
import java.util.Locale
import java.util.Random

class IconPackManager(
    private val appContext: Context,
    private val packageManager: PackageManager,
    private val appsRepository: AppsRepository
) {

    inner class IconPack(
        val packageName: String,
        val name: String
    ) {

        private var mLoaded = false
        private val mPackagesDrawables = HashMap<String?, String?>()
        private val mBackImages: MutableList<Bitmap> = ArrayList()
        private var mMaskImage: Bitmap? = null
        private var mFrontImage: Bitmap? = null
        private var mFactor = 1.0f
        var totalIcons = 0
            private set
        var iconPackResources: Resources? = null

        fun load() {
            // load appfilter.xml from the icon pack package
            try {
                var xpp: XmlPullParser? = null
                iconPackResources = packageManager.getResourcesForApplication(packageName)
                val appFilterId = iconPackResources!!.getIdentifier("appfilter", "xml", packageName)
                if (appFilterId > 0) {
                    xpp = iconPackResources!!.getXml(appFilterId)
                } else {
                    // no resource found, try to open it from assets folder
                    try {
                        val appFilterStream = iconPackResources!!.assets.open("appfilter.xml")
                        val factory = XmlPullParserFactory.newInstance()
                        factory.isNamespaceAware = true
                        xpp = factory.newPullParser()
                        xpp.setInput(appFilterStream, "utf-8")
                    } catch (e1: IOException) {
                        //Ln.d("No appfilter.xml file");
                    }
                }
                if (xpp != null) {
                    var eventType = xpp.eventType
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        if (eventType == XmlPullParser.START_TAG) {
                            if (xpp.name == "iconback") {
                                for (i in 0 until xpp.attributeCount) {
                                    if (xpp.getAttributeName(i).startsWith("img")) {
                                        val drawableName = xpp.getAttributeValue(i)
                                        val iconback = loadBitmap(drawableName)
                                        if (iconback != null) mBackImages.add(iconback)
                                    }
                                }
                            } else if (xpp.name == "iconmask") {
                                if (xpp.attributeCount > 0 && xpp.getAttributeName(0) == "img1") {
                                    val drawableName = xpp.getAttributeValue(0)
                                    mMaskImage = loadBitmap(drawableName)
                                }
                            } else if (xpp.name == "iconupon") {
                                if (xpp.attributeCount > 0 && xpp.getAttributeName(0) == "img1") {
                                    val drawableName = xpp.getAttributeValue(0)
                                    mFrontImage = loadBitmap(drawableName)
                                }
                            } else if (xpp.name == "scale") {
                                // mFactor
                                if (xpp.attributeCount > 0 && xpp.getAttributeName(0) == "factor") {
                                    mFactor = java.lang.Float.valueOf(xpp.getAttributeValue(0))
                                }
                            } else if (xpp.name == "item") {
                                var componentName: String? = null
                                var drawableName: String? = null
                                for (i in 0 until xpp.attributeCount) {
                                    if (xpp.getAttributeName(i) == "component") {
                                        componentName = xpp.getAttributeValue(i)
                                    } else if (xpp.getAttributeName(i) == "drawable") {
                                        drawableName = xpp.getAttributeValue(i)
                                    }
                                }
                                if (!mPackagesDrawables.containsKey(componentName)) {
                                    mPackagesDrawables[componentName] = drawableName
                                    totalIcons += 1
                                }
                            }
                        }
                        eventType = xpp.next()
                    }
                }
                mLoaded = true
            } catch (e: PackageManager.NameNotFoundException) {
                //Ln.d("Cannot load icon pack");
            } catch (e: XmlPullParserException) {
                //Ln.d("Cannot parse icon pack appfilter.xml");
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        private fun loadBitmap(drawableName: String): Bitmap? {
            val id = iconPackResources!!.getIdentifier(drawableName, "drawable", packageName)
            if (id > 0) {
                val bitmap = iconPackResources!!.getDrawable(id)
                if (bitmap is BitmapDrawable) return bitmap.bitmap
            }
            return null
        }

        private fun loadDrawable(drawableName: String): Drawable? {
            val id = iconPackResources!!.getIdentifier(drawableName, "drawable", packageName)
            return if (id > 0) {
                iconPackResources!!.getDrawable(id)
            } else null
        }

        fun getDrawableIconForPackage(appPackageName: String, defaultDrawable: Drawable): Drawable? {
            if (!mLoaded) load()
            val pm = this@IconPackManager.appContext!!.packageManager
            val launchIntent = pm.getLaunchIntentForPackage(appPackageName!!)
            var componentName: String? = null
            if (launchIntent != null) componentName =
                pm.getLaunchIntentForPackage(appPackageName)!!.component.toString()
            var drawable = mPackagesDrawables[componentName]
            if (drawable != null) {
                return loadDrawable(drawable)
            } else {
                // try to get a resource with the component filename
                if (componentName != null) {
                    val start = componentName.indexOf("{") + 1
                    val end = componentName.indexOf("}", start)
                    if (end > start) {
                        drawable = componentName
                            .substring(start, end)
                            .lowercase(Locale.getDefault())
                            .replace(".", "_")
                            .replace("/", "_")
                        if (iconPackResources!!.getIdentifier(drawable, "drawable", packageName) > 0) {
                            return loadDrawable(drawable)
                        }
                    }
                }
            }
            return defaultDrawable
        }

        fun getIconForPackage(appPackageName: String, defaultBitmap: Bitmap): Bitmap? {
            if (mLoaded.not()) {
                load()
            }

            val componentName = packageManager.getLaunchIntentForPackage(appPackageName)?.component?.toString()
            var drawable = mPackagesDrawables[componentName]
            if (drawable != null) {
                return loadBitmap(drawable) ?: generateBitmap(appPackageName, defaultBitmap)
            } else {
                // try to get a resource with the component filename
                if (componentName != null) {
                    val start = componentName.indexOf("{") + 1
                    val end = componentName.indexOf("}", start)
                    if (end > start) {
                        drawable = componentName
                            .substring(start, end)
                            .lowercase(Locale.getDefault())
                            .replace(".", "_")
                            .replace("/", "_")
                        if (iconPackResources!!.getIdentifier(drawable, "drawable", packageName) > 0)
                            return loadBitmap(drawable)
                    }
                }
                return generateBitmap(appPackageName, defaultBitmap)
            }
        }

        private fun generateBitmap(appPackageName: String, defaultBitmap: Bitmap): Bitmap {
            // the key for the cache is the icon pack package name and the app package name
            val key = "$packageName:$appPackageName"

            // if generated bitmaps cache already contains the package name return it
//            Bitmap cachedBitmap = BitmapCache.getInstance(mContext).getBitmap(key);
//            if (cachedBitmap != null)
//                return cachedBitmap;

            // if no support images in the icon pack return the bitmap itself
            if (mBackImages.size == 0) return defaultBitmap
            val r = Random()
            val backImageInd = r.nextInt(mBackImages.size)
            val backImage = mBackImages[backImageInd]
            val w = backImage.width
            val h = backImage.height

            // create a bitmap for the result
            val result = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
            val mCanvas = Canvas(result)

            // draw the background first
            mCanvas.drawBitmap(backImage, 0f, 0f, null)

            // create a mutable mask bitmap with the same mask
            val scaledBitmap: Bitmap = if (defaultBitmap.width > w || defaultBitmap.height > h) {
                Bitmap.createScaledBitmap(defaultBitmap, (w * mFactor).toInt(), (h * mFactor).toInt(), false)
            } else {
                Bitmap.createBitmap(defaultBitmap)
            }
            if (mMaskImage != null) {
                // draw the scaled bitmap with mask
                val mutableMask = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
                val maskCanvas = Canvas(mutableMask)
                maskCanvas.drawBitmap(mMaskImage!!, 0f, 0f, Paint())

                // paint the bitmap with mask into the result
                val paint = Paint(Paint.ANTI_ALIAS_FLAG)
                paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
                mCanvas.drawBitmap(
                    scaledBitmap,
                    ((w - scaledBitmap.width) / 2).toFloat(),
                    ((h - scaledBitmap.height) / 2).toFloat(),
                    null
                )
                mCanvas.drawBitmap(mutableMask, 0f, 0f, paint)
                paint.xfermode = null
            } else  // draw the scaled bitmap with the back image as mask
            {
                val mutableMask = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
                val maskCanvas = Canvas(mutableMask)
                maskCanvas.drawBitmap(backImage, 0f, 0f, Paint())

                // paint the bitmap with mask into the result
                val paint = Paint(Paint.ANTI_ALIAS_FLAG)
                paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
                mCanvas.drawBitmap(
                    scaledBitmap,
                    ((w - scaledBitmap.width) / 2).toFloat(),
                    ((h - scaledBitmap.height) / 2).toFloat(),
                    null
                )
                mCanvas.drawBitmap(mutableMask, 0f, 0f, paint)
                paint.xfermode = null
            }

            // paint the front
            if (mFrontImage != null) {
                mCanvas.drawBitmap(mFrontImage!!, 0f, 0f, null)
            }

            // store the bitmap in cache
//            BitmapCache.getInstance(mContext).putBitmap(key, result);

            // return it
            return result
        }
    }

    @Deprecated(
        "Use appsRepository.observeInstalledIconPacks()",
        ReplaceWith("appsRepository.observeInstalledIconPacks()")
    )
    fun getAvailableIconPacks(): Map<String, IconPack> {
        val installedIconPacks = runBlocking { appsRepository.observeInstalledIconPacks().first() }
        return installedIconPacks.associate { it.id.value to IconPack(it.id.value, it.name.value) }
    }
}
