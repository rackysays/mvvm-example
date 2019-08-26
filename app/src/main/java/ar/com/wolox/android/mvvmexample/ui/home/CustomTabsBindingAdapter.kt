package ar.com.wolox.android.mvvmexample.ui.home

import androidx.core.content.ContextCompat
import com.google.android.material.tabs.TabLayout
import androidx.databinding.BindingAdapter
import ar.com.wolox.android.mvvmexample.R
import ar.com.wolox.android.mvvmexample.ui.home.TABS.NEWS_ACTIVE_TAB
import ar.com.wolox.android.mvvmexample.ui.home.TABS.NEWS_INACTIVE_TAB
import ar.com.wolox.android.mvvmexample.ui.home.TABS.NEWS_TAB
import ar.com.wolox.android.mvvmexample.ui.home.TABS.PROFILE_ACTIVE_TAB
import ar.com.wolox.android.mvvmexample.ui.home.TABS.PROFILE_INACTIVE_TAB
import ar.com.wolox.android.mvvmexample.ui.home.TABS.PROFILE_TAB

@BindingAdapter("bind:setUpHomePageIcons")
fun bindViewPagerTabsIcons(view: TabLayout, setUp: Boolean) {
    if (setUp) {
        view.getTabAt(0)?.setIcon(R.drawable.ic_news_list_on)
        view.getTabAt(1)?.setIcon(R.drawable.ic_profile_off)

        view.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabReselected(p0: TabLayout.Tab?) {
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
                p0?.position.let {
                    p0?.icon = ContextCompat.getDrawable(view.context,
                        getUnSelectIcon(it)
                    )
                }
            }

            override fun onTabSelected(p0: TabLayout.Tab?) {
                p0?.position.let {
                    p0?.icon = ContextCompat.getDrawable(view.context,
                        getSelectIcon(it)
                    )
                }
            }
        })
    }
}

private fun getSelectIcon(position: Int?): Int {
    return when (position) {
        NEWS_TAB -> NEWS_ACTIVE_TAB
        PROFILE_TAB -> PROFILE_ACTIVE_TAB
        else -> NEWS_ACTIVE_TAB
    }
}

private fun getUnSelectIcon(position: Int?): Int {
    return when (position) {
        NEWS_TAB -> NEWS_INACTIVE_TAB
        PROFILE_TAB -> PROFILE_INACTIVE_TAB
        else -> NEWS_INACTIVE_TAB
    }
}

internal object TABS {
    internal const val NEWS_ACTIVE_TAB = R.drawable.ic_news_list_on
    internal const val NEWS_INACTIVE_TAB = R.drawable.ic_news_list_off
    internal const val PROFILE_ACTIVE_TAB = R.drawable.ic_profile_on
    internal const val PROFILE_INACTIVE_TAB = R.drawable.ic_profile_off
    internal const val NEWS_TAB = 0
    internal const val PROFILE_TAB = 1
}