package ar.com.wolox.android.mvvmexample.ui.home

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import ar.com.wolox.android.mvvmexample.R
import ar.com.wolox.android.mvvmexample.ui.home.news.NewsFragment
import ar.com.wolox.android.mvvmexample.ui.home.profile.ProfileFragment


class TabSelectionAdapter(private val context: Context, fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment {
        return when(position) {
            NEWS_TAB -> NewsFragment()
            PROFILE_TAB -> ProfileFragment()
            else -> NewsFragment()
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position) {
            NEWS_TAB -> { context.resources.getString(TITLE_NEWS) }
            PROFILE_TAB -> context.resources.getString(TITLE_PROFILE)
            else -> context.resources.getString(TITLE_NEWS)
        }
    }

    override fun getCount(): Int = TABS.size

    companion object {
        private const val NEWS_TAB = 0
        private const val PROFILE_TAB = 1
        private const val TITLE_NEWS = R.string.home_tab_news
        private const val TITLE_PROFILE = R.string.home_tab_profile
        private val TABS = listOf(NEWS_TAB, PROFILE_TAB)
    }
}