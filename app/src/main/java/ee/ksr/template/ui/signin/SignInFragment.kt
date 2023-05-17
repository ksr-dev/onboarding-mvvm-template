package ee.ksr.template.ui.signin

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
import ee.ksr.template.databinding.FragmentSignInBinding
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignInFragment : Fragment() {
    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SignInViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
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
                    if (it.isLoading) {
                        binding.progressIndicator.visibility = View.VISIBLE
                    } else {
                        binding.progressIndicator.visibility = View.GONE
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
        val action = SignInFragmentDirections.actionSignInFragmentToHomeFragment()
        findNavController().navigate(action)
    }

    private fun initViews() {
        binding.continueButton.setOnClickListener {
            handleLogin()
        }
    }

    private fun handleLogin() {
        val identifier = binding.editTextIdentifier.text.toString().trim()
        val password = binding.editTextPassword.text.toString().trim()
        viewModel.startLogin(identifier, password)
    }
}