package ee.ksr.template.ui.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat.ThemeCompat
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ee.ksr.template.databinding.FragmentOnboardingPageBinding

@AndroidEntryPoint
class OnboardingPageFragment : Fragment() {

    private var _binding: FragmentOnboardingPageBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val viewModel: OnboardingViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboardingPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            it.getString(ARG_TITLE)?.let { title ->
                binding.title.text = title
            }
            it.getInt(ARG_BG_COLOR).let { resourceId ->
                binding.root.setBackgroundColor(resources.getColor(resourceId, requireContext().theme))
            }
        }
    }

    companion object {
        private const val ARG_TITLE = "ARG_TITLE"
        private const val ARG_BG_COLOR = "ARG_BG_COLOR"
        fun newInstance(title: String, pageColor: Int): OnboardingPageFragment {
            val args = Bundle()
            args.putString(ARG_TITLE, title)
            args.putInt(ARG_BG_COLOR, pageColor)
            val fragment = OnboardingPageFragment()
            fragment.arguments = args
            return fragment
        }
    }
}