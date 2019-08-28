package ar.com.wolox.android.mvvmexample.util.binding_adapters

import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.fragment.app.FragmentPagerAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager.widget.ViewPager
import ar.com.wolox.android.mvvmexample.R
import ar.com.wolox.android.mvvmexample.util.formatDateToTime
import com.facebook.drawee.view.SimpleDraweeView
import com.google.android.material.tabs.TabLayout

@BindingAdapter("formatDate")
fun setDateToTime(view: TextView, date: String?) {
    view.apply {
        date?.let {
            text = formatDateToTime(context,it)
        }
    }
}

@BindingAdapter("setFrescoImage")
fun setFrescoImage(view: SimpleDraweeView, uri: String?){
    view.apply {
        uri?.let {
            setImageURI((it).replace(
            context.getString(R.string.reg_pattern_protocol).toRegex(),
            context.getString(R.string.allow_protocol)))
        }
    }
}

@BindingAdapter("setRefreshColor")
fun setRefreshColor(view: SwipeRefreshLayout, color: Int){
    view.setColorSchemeColors(color,color,color)
}

@BindingAdapter("bind:handler")
fun bindViewPagerAdapter(view: ViewPager, adapter: FragmentPagerAdapter) {
    view.adapter = adapter
}

@BindingAdapter("bind:pager")
fun bindViewPagerTabs(view: TabLayout, pagerView: ViewPager) {
    view.setupWithViewPager(pagerView, true)
}