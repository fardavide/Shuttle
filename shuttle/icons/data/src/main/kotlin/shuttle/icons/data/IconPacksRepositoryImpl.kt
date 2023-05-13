package shuttle.icons.data

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Named
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import shuttle.apps.domain.model.AppId
import shuttle.icons.data.model.IconPack
import shuttle.icons.domain.repository.IconPacksRepository
import shuttle.utils.kotlin.IoDispatcher
import java.io.IOException
import java.util.Locale
import java.util.Random

@Factory
@Suppress("DEPRECATION")
@SuppressLint("DiscouragedApi", "UseCompatLoadingForDrawables")
class IconPacksRepositoryImpl(
    private val packageManager: PackageManager,
    @Named(IoDispatcher) private val dispatcher: CoroutineDispatcher
) : IconPacksRepository {

    private var cache = mutableMapOf<AppId, IconPack>()

    override suspend fun getDrawableIcon(
        iconPackId: AppId,
        appId: AppId,
        defaultDrawable: Drawable
    ): Drawable = getDrawableIcon(loadIconPack(iconPackId), appId, defaultDrawable)

    override suspend fun getBitmapIcon(
        iconPackId: AppId,
        appId: AppId,
        defaultBitmap: Bitmap
    ): Bitmap = getBitmapIcon(loadIconPack(iconPackId), appId, defaultBitmap)

    @Suppress("LongMethod", "ComplexMethod", "SwallowedException") // Copied from SO, to be refactored
    private suspend fun loadIconPack(id: AppId): IconPack = withContext(dispatcher) {
        cache[id]?.let { return@withContext it }

        val packageName = id.value

        var iconPackResources: Resources? = null
        val drawables = mutableMapOf<AppId, IconPack.DrawableName>()
        var frontImage: Bitmap? = null
        val backImages: MutableList<Bitmap> = ArrayList()
        var maskImage: Bitmap? = null
        var factor = 1.0f
        var totalIcons = 0

        fun loadBitmap(drawableName: String): Bitmap? {
            val iconPackId = iconPackResources!!.getIdentifier(drawableName, "drawable", packageName)
            if (iconPackId > 0) {
                val bitmap = iconPackResources!!.getDrawable(iconPackId)
                if (bitmap is BitmapDrawable) return bitmap.bitmap
            }
            return null
        }

        // load appfilter.xml from the icon pack package
        try {
            var xpp: XmlPullParser? = null
            iconPackResources = packageManager.getResourcesForApplication(packageName)
            val appFilterId = iconPackResources.getIdentifier("appfilter", "xml", packageName)
            if (appFilterId > 0) {
                xpp = iconPackResources.getXml(appFilterId)
            } else {
                // no resource found, try to open it from assets folder
                try {
                    val appFilterStream = iconPackResources.assets.open("appfilter.xml")
                    val factory = XmlPullParserFactory.newInstance()
                    factory.isNamespaceAware = true
                    xpp = factory.newPullParser()
                    xpp.setInput(appFilterStream, "utf-8")
                } catch (e1: IOException) {
                    // Ln.d("No appfilter.xml file");
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
                                    if (iconback != null) backImages.add(iconback)
                                }
                            }
                        } else if (xpp.name == "iconmask") {
                            if (xpp.attributeCount > 0 && xpp.getAttributeName(0) == "img1") {
                                val drawableName = xpp.getAttributeValue(0)
                                maskImage = loadBitmap(drawableName)
                            }
                        } else if (xpp.name == "iconupon") {
                            if (xpp.attributeCount > 0 && xpp.getAttributeName(0) == "img1") {
                                val drawableName = xpp.getAttributeValue(0)
                                frontImage = loadBitmap(drawableName)
                            }
                        } else if (xpp.name == "scale") {
                            // mFactor
                            if (xpp.attributeCount > 0 && xpp.getAttributeName(0) == "factor") {
                                factor = java.lang.Float.valueOf(xpp.getAttributeValue(0))
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
                            if (!drawables.containsKey(componentName?.id())) {
                                drawables[componentName!!.id()] = drawableName!!.drawableName()
                                totalIcons += 1
                            }
                        }
                    }
                    eventType = xpp.next()
                }
            }
        } catch (e: PackageManager.NameNotFoundException) {
            // Ln.d("Cannot load icon pack");
        } catch (e: XmlPullParserException) {
            // Ln.d("Cannot parse icon pack appfilter.xml");
        } catch (e: IOException) {
            e.printStackTrace()
        }

        IconPack(
            id = id,
            resources = iconPackResources!!,
            drawables = drawables,
            frontImage = frontImage,
            backImages = backImages,
            maskImage = maskImage,
            factor = factor
        ).also { cache[id] = it }
    }

    private suspend fun getDrawableIcon(
        iconPack: IconPack,
        appId: AppId,
        defaultDrawable: Drawable
    ): Drawable = withContext(dispatcher) {
        val appPackageName = appId.value
        val drawables = iconPack.drawables

        val launchIntent = packageManager.getLaunchIntentForPackage(appPackageName)
        var componentName: String? = null
        if (launchIntent != null) componentName =
            packageManager.getLaunchIntentForPackage(appPackageName)!!.component.toString()
        var drawable = drawables[componentName?.id()]?.value
        if (drawable != null) {
            return@withContext loadDrawable(iconPack, drawable) ?: defaultDrawable
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
                    if (iconPack.resources.getIdentifier(drawable, "drawable", iconPack.id.value) > 0) {
                        return@withContext loadDrawable(iconPack, drawable) ?: defaultDrawable
                    }
                }
            }
        }
        return@withContext defaultDrawable
    }

    private suspend fun getBitmapIcon(
        iconPack: IconPack,
        appId: AppId,
        defaultBitmap: Bitmap
    ): Bitmap = withContext(dispatcher) {
        val appPackageName = appId.value
        val resources = iconPack.resources
        val drawables = iconPack.drawables

        val componentName = packageManager.getLaunchIntentForPackage(appPackageName)?.component?.toString()
        var drawable = drawables[componentName?.id()]?.value
        if (drawable != null) {
            return@withContext loadBitmap(iconPack, drawable) ?: generateBitmap(iconPack, defaultBitmap)
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
                    if (resources.getIdentifier(drawable, "drawable", iconPack.id.value) > 0)
                        return@withContext loadBitmap(iconPack, drawable) ?: defaultBitmap
                }
            }
            return@withContext generateBitmap(iconPack, defaultBitmap)
        }
    }

    private fun loadBitmap(iconPack: IconPack, drawableName: String): Bitmap? {
        val resources = iconPack.resources

        val id = resources.getIdentifier(drawableName, "drawable", iconPack.id.value)
        if (id > 0) {
            val bitmap = resources.getDrawable(id)
            if (bitmap is BitmapDrawable) return bitmap.bitmap
        }
        return null
    }

    private fun loadDrawable(iconPack: IconPack, drawableName: String): Drawable? {
        val resources = iconPack.resources

        val id = resources.getIdentifier(drawableName, "drawable", iconPack.id.value)
        return if (id > 0) resources.getDrawable(id) else null
    }

    private fun generateBitmap(iconPack: IconPack, defaultBitmap: Bitmap): Bitmap {
        val frontImage = iconPack.frontImage
        val backImages = iconPack.backImages
        val maskImage = iconPack.maskImage
        val factor = iconPack.factor

        // if no support images in the icon pack return the bitmap itself
        if (backImages.isEmpty()) return defaultBitmap
        val r = Random()
        val backImageInd = r.nextInt(backImages.size)
        val backImage = backImages[backImageInd]
        val w = backImage.width
        val h = backImage.height

        // create a bitmap for the result
        val result = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val mCanvas = Canvas(result)

        // draw the background first
        mCanvas.drawBitmap(backImage, 0f, 0f, null)

        // create a mutable mask bitmap with the same mask
        val scaledBitmap: Bitmap = if (defaultBitmap.width > w || defaultBitmap.height > h) {
            Bitmap.createScaledBitmap(defaultBitmap, (w * factor).toInt(), (h * factor).toInt(), false)
        } else {
            Bitmap.createBitmap(defaultBitmap)
        }
        if (maskImage != null) {
            // draw the scaled bitmap with mask
            val mutableMask = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
            val maskCanvas = Canvas(mutableMask)
            maskCanvas.drawBitmap(maskImage, 0f, 0f, Paint())

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
        } else // draw the scaled bitmap with the back image as mask
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
        if (frontImage != null) {
            mCanvas.drawBitmap(frontImage, 0f, 0f, null)
        }

        // store the bitmap in cache
//            BitmapCache.getInstance(mContext).putBitmap(key, result);

        // return it
        return result
    }

    private fun String.id() = AppId(this)
    private fun String.drawableName() = IconPack.DrawableName(this)
}
