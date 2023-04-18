package ee.ksr.template.ui.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import ee.ksr.template.R
import ee.ksr.template.databinding.FragmentUserCreationBinding

@AndroidEntryPoint
class UserCreationFragment : Fragment() {

    private var _binding: FragmentUserCreationBinding? = null
    private val binding get() = _binding!!
    private val args: UserCreationFragmentArgs by navArgs()
    private val viewModel: SignUpViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentUserCreationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        if (args.isEmail) {
            binding.identifierTitle.text = getString(R.string.title_email)
        }
        else {
            binding.identifierTitle.text = getString(R.string.title_username)
        }
        binding.continueButton.setOnClickListener {
            val userIdentifier = binding.editTextIdentifier.text.toString().trim()
            viewModel.saveUserName(userIdentifier)
            val action = UserCreationFragmentDirections.actionUserCreationFragmentToPasswordFragment()
            findNavController().navigate(action)
        }
    }

    companion object {

    }
}