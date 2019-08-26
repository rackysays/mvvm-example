package ar.com.wolox.android.mvvmexample.util

import android.annotation.SuppressLint
import android.content.Context
import ar.com.wolox.android.mvvmexample.R
import org.ocpsoft.prettytime.PrettyTime
import java.text.SimpleDateFormat

@SuppressLint("SimpleDateFormat")
fun formatDateToTime(context: Context, createdAt: String): String {
    val format = SimpleDateFormat(context.getString(R.string.ISO_8601_24h_full_format))
    val prettyTime = PrettyTime()
    return prettyTime.format(format.parse(createdAt))
            .replace(context.getString(R.string.reg_pattern_years).toRegex(), context.getString(R.string.year_replacement))
            .replace(context.getString(R.string.reg_pattern_months).toRegex(), context.getString(R.string.month_replacement))
            .replace(context.getString(R.string.reg_pattern_weeks).toRegex(), context.getString(R.string.week_replacement))
            .replace(context.getString(R.string.reg_pattern_days).toRegex(), context.getString(R.string.day_replacement))
            .replace(context.getString(R.string.reg_pattern_hours).toRegex(), context.getString(R.string.hour_replacement))
            .replace(context.getString(R.string.reg_pattern_minuts).toRegex(), context.getString(R.string.minuts_replacement))
            .replace(context.getString(R.string.reg_pattern_now).toRegex(), context.getString(R.string.now_replacement))
}