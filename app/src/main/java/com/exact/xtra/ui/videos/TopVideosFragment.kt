package com.exact.xtra.ui.videos

import android.content.Context
import android.os.Bundle
import android.view.View

import com.exact.xtra.R
import com.exact.xtra.ui.fragment.RadioButtonDialogFragment
import com.exact.xtra.util.C
import com.exact.xtra.util.FragmentUtils
import kotlinx.android.synthetic.main.fragment_videos.*

class TopVideosFragment : BaseVideosFragment(), RadioButtonDialogFragment.OnOptionSelectedListener {

    private companion object {
        val sortOptions = listOf(R.string.this_week, R.string.this_month, R.string.all_time)
        const val DEFAULT_INDEX = 0
        const val TAG = "TopVideos"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sortBar.setOnClickListener { FragmentUtils.showRadioButtonDialogFragment(requireActivity(), childFragmentManager, sortOptions, DEFAULT_INDEX, TAG) }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (isFragmentVisible) {
            if (!viewModel.isInitialized()) {
                viewModel.sort = Sort.VIEWS
                viewModel.period = Period.WEEK
                viewModel.sortText.postValue(getString(sortOptions[requireActivity().getSharedPreferences(C.USER_PREFS, Context.MODE_PRIVATE).getInt(TAG, DEFAULT_INDEX)]))
            }
            initDefaultSortTextObserver()
        }
    }

    override fun loadData(override: Boolean) {
        viewModel.loadVideos(reload = override)
    }

    override fun onSelect(index: Int, text: CharSequence, tag: Int?) {
        if (viewModel.periodText != text) {
            viewModel.period = when(tag) {
                R.string.this_week -> Period.WEEK
                R.string.this_month -> Period.MONTH
                R.string.all_time -> Period.ALL
                else -> null
            }
            viewModel.periodText.postValue(text)
            loadData(true)
        }
    }
}
