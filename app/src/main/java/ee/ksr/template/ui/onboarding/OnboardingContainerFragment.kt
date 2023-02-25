package ee.ksr.template.ui.onboarding

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import ee.ksr.template.R
import ee.ksr.template.databinding.FragmentOnboardingContainerBinding

class OnboardingContainerFragment : Fragment() {

    private var _binding: FragmentOnboardingContainerBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var viewPager: ViewPager2
    private lateinit var pagerAdapter: SlidePagerAdapter

    private lateinit var viewModel: OnboardingViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboardingContainerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this)[OnboardingViewModel::class.java]
        // TODO: Use the ViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewPager()
    }

    private fun initViewPager() {
        viewPager = binding.viewpager
        pagerAdapter = SlidePagerAdapter(this)
        viewPager.adapter = pagerAdapter
        val tabLayout = binding.tablayout
        TabLayoutMediator(tabLayout, viewPager) { _, _ -> }.attach()
    }

    private inner class SlidePagerAdapter(fragment: OnboardingContainerFragment) :
        FragmentStateAdapter(fragment) {
        override fun getItemCount(): Int = NUM_PAGES
        override fun createFragment(position: Int): Fragment {
            val pageTitle = resources.getStringArray(R.array.onboarding_title_array)[position]
            val pageBody = resources.getStringArray(R.array.onboarding_body_array)[position]
            return OnboardingPageFragment.newInstance(
                title = pageTitle,
                body = pageBody
            )
        }
    }

    companion object {
        private const val NUM_PAGES = 3
    }
}