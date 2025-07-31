package ir.mahan.ghabchin.util

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump
import timber.log.Timber

@HiltAndroidApp
class MyApp: Application() {

    override fun onCreate() {
        super.onCreate()
        //Calligraphy
        ViewPump.init(
            ViewPump.builder().addInterceptor(
                CalligraphyInterceptor(
                    CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/Poppins-medium.ttf")
                        .setFontAttrId(io.github.inflationx.calligraphy3.R.attr.fontPath)
                        .build()
                )
            ).build()
        )

        // Timber
        Timber.plant(Timber.DebugTree())
    }
}