package ee.ksr.template.ui.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import dagger.hilt.android.AndroidEntryPoint
import ee.ksr.template.R
import ee.ksr.template.databinding.FragmentOnboardingContainerBinding

@AndroidEntryPoint
class OnboardingContainerFragment : Fragment() {

    private var _binding: FragmentOnboardingContainerBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var viewPager: ViewPager2
    private lateinit var pagerAdapter: SlidePagerAdapter

    private val args: OnboardingContainerFragmentArgs by navArgs()
    private val viewModel: OnboardingViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboardingContainerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        val hasSkipButton = args.hasSkipButton
        val hasBackButton = args.hasBackButton
        val hasContinueButton = args.hasContinueButton
        binding.viewpager.isUserInputEnabled = args.isSwipable
        if (!hasContinueButton) {
            binding.continueButton.visibility = View.GONE
        }
        if (hasSkipButton) {
            binding.skipButton.setOnClickListener {
                navigateToHomeFragment()
            }
        }
        createViewPager(hasSkipButton, hasBackButton, hasContinueButton)
    }

    private fun createViewPager(
        hasSkipButton: Boolean,
        hasBackButton: Boolean,
        hasContinueButton: Boolean
    ) {
        pagerAdapter = SlidePagerAdapter(this)
        viewPager = binding.viewpager
        viewPager.adapter = pagerAdapter

        viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (hasSkipButton) {
                    handleSkipButtonForPosition(position)
                }
                if (hasBackButton) {
                    handleBackButtonForPosition(position)
                }
                if (hasContinueButton) {
                    handleContinueButtonForPosition(position)
                }
            }
        })

        binding.dotsIndicator.attachTo(viewPager)
    }

    private fun handleContinueButtonForPosition(position: Int) {
        if (position == NUM_PAGES - 1) {
            binding.continueButton.setOnClickListener {
                navigateToHomeFragment()
            }
        } else {
            binding.continueButton.setOnClickListener {
                showNextPage()
            }
        }
    }

    private fun handleBackButtonForPosition(position: Int) {
        if (position != 0) {
            binding.backButton.visibility = View.VISIBLE
            binding.backButton.setOnClickListener {
                showPreviousPage()
            }
        } else {
            binding.backButton.visibility = View.GONE
        }
    }

    private fun handleSkipButtonForPosition(position: Int) {
        if (position == NUM_PAGES - 1) {
            binding.skipButton.visibility = View.GONE
        } else {
            binding.skipButton.visibility = View.VISIBLE
        }
    }

    private fun navigateToHomeFragment() {
        val action = OnboardingContainerFragmentDirections.actionOnboardingContainerFragmentToHomeFragment()
        findNavController().navigate(action)
    }

    private fun showPreviousPage() {
        if (viewPager.currentItem != 0) { // has not reached end
            viewPager.setCurrentItem(viewPager.currentItem - 1, true)
        } else { // has reached end
            //viewModel.onOpenEnterAppPage()
        }
    }
    private inner class SlidePagerAdapter(fragment: OnboardingContainerFragment) :
        FragmentStateAdapter(fragment) {
        override fun getItemCount(): Int = NUM_PAGES
        override fun createFragment(position: Int): Fragment {
            val pageTitle = resources.getStringArray(R.array.onboarding_title_array)[position]
            val pageBody = resources.getStringArray(R.array.onboarding_body_array)[position]
            val imageArray = resources.obtainTypedArray(R.array.onboarding_image_array)
            val pageImage = imageArray.getResourceId(position, 0)
            imageArray.recycle()
            return OnboardingPageFragment.newInstance(
                title = pageTitle,
                body = pageBody,
                imageResource = pageImage
            )
        }
    }

    private fun showNextPage() {
        if (viewPager.currentItem != NUM_PAGES - 1) { // has not reached end
            viewPager.setCurrentItem(viewPager.currentItem + 1, true)
        } else { // has reached end
            //viewModel.onOpenEnterAppPage()
        }
    }

    companion object {
        private const val NUM_PAGES = 4
    }
}