package ee.ksr.template.ui.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ee.ksr.template.databinding.FragmentOnboardingPageBinding

class OnboardingPageFragment : Fragment() {

    private var _binding: FragmentOnboardingPageBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


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
            it.getString(ARG_BODY)?.let { body ->
                binding.body.text = body
            }
        }
    }

    companion object {
        private const val ARG_TITLE = "ARG_TITLE"
        private const val ARG_BODY = "ARG_BODY"

        fun newInstance(title: String, body: String): OnboardingPageFragment {
            val args = Bundle()
            args.putString(ARG_TITLE, title)
            args.putString(ARG_BODY, body)
            val fragment = OnboardingPageFragment()
            fragment.arguments = args
            return fragment
        }
    }
}