package ee.ksr.template.ui.signup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ee.ksr.template.databinding.FragmentPasswordBinding
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PasswordFragment : Fragment() {

    private var _binding: FragmentPasswordBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SignUpViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    when {
                        it.isLoading -> binding.progressIndicator.visibility = View.VISIBLE
                        else -> binding.progressIndicator.visibility = View.GONE
                    }

                    if (it.toastMessageResId != null) {
                        Toast.makeText(requireContext(), it.toastMessageResId, Toast.LENGTH_SHORT).show()
                    }
                    if (it.loginData != null) {
                        Toast.makeText(requireContext(), it.loginData.userName, Toast.LENGTH_SHORT).show()
                        navigateToHome()
                    }
                }
            }
        }
    }

    private fun navigateToHome() {
        val action = PasswordFragmentDirections.actionPasswordFragmentToHomeFragment()
        findNavController().navigate(action)
    }

    private fun initViews() {
        binding.continueButton.setOnClickListener {
            val firstPassword = binding.editTextPassword.text.toString().trim()
            val secondPassword = binding.editTextPasswordConfirm.text.toString().trim()
            viewModel.startCreateUser(firstPassword, secondPassword)
        }
    }
}