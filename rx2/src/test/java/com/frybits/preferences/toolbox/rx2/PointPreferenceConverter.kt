package com.frybits.preferences.toolbox.rx2

import com.frybits.preferences.toolbox.core.Preference

class PointPreferenceConverter: Preference.Converter<Point> {

    override fun deserialize(serialized: String): Point {
        val parts = serialized.split(",")
        if (parts.size != 2) {
            throw IllegalStateException("Malformed point value: '$serialized'")
        }
        return Point(parts[0].toInt(), parts[1].toInt())
    }

    override fun serialize(value: Point): String {
        return "${value.x},${value.y}"
    }
}
