package ar.com.wolox.android.mvvmexample.util.binding_adapters

import androidx.databinding.BindingAdapter
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout

@BindingAdapter("bind:handler")
fun bindViewPagerAdapter(view: ViewPager, adapter: FragmentPagerAdapter) {
    view.adapter = adapter
}

@BindingAdapter("bind:pager")
fun bindViewPagerTabs(view: TabLayout, pagerView: ViewPager) {
    view.setupWithViewPager(pagerView, true)
}