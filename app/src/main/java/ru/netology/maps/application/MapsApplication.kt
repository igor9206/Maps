package ru.netology.maps.application

import android.app.Application
import com.yandex.mapkit.MapKitFactory

class MapsApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        MapKitFactory.setApiKey("67f7db3d-c978-4b1f-849b-c376ee8ca10b")
    }
}