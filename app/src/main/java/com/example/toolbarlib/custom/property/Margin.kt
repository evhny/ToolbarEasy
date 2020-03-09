package com.example.toolbarlib.custom.property

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import com.example.toolbarlib.custom.property.consts.MarginSet

/**
 * It is a data for build margin for Components.
 * You must set value as dip
 */
class Margin() {
    var marginTop: Float = MarginSet.SMALL
       /* set(value) {
            field = convertToPix(value)
        }*/
    var marginEnd: Float = MarginSet.SMALL
        /*set(value) {
            field = convertToPix(value)
        }*/
    var marginBottom: Float = MarginSet.SMALL
       /* set(value) {
            field = convertToPix(value)
        }*/
    var marginStart: Float = MarginSet.SMALL
        /*set(value) {
            field = convertToPix(value)
        }*/

//    private fun convertToPix(dip: Float): Float {
//        val r: Resources = context.resources
//        return TypedValue.applyDimension(
//            TypedValue.COMPLEX_UNIT_DIP,
//            dip,
//            r.displayMetrics
//        )
//    }
}