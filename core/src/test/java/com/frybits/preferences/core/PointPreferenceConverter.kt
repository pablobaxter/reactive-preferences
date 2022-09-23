package com.frybits.preferences.core

import com.frybits.preferences.core.Preference

/*
 *  Copyright 2022 Pablo Baxter
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 * Created by Pablo Baxter (Github: pablobaxter)
 * https://github.com/pablobaxter/rx-preferences
 *
 * Re-implementation of https://github.com/f2prateek/rx-preferences/blob/master/rx-preferences/src/test/java/com/f2prateek/rx/preferences2/PointPreferenceConverter.java
 */

data class Point(val x: Int, val y: Int)

abstract class PointPreferenceConverter: Preference.Converter<Point> {

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
