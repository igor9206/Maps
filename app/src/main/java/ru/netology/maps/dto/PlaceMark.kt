package ru.netology.maps.dto

import com.yandex.mapkit.geometry.Point

data class PlaceMark(
    val id: Long,
    val point: Point,
    val name: String
)
