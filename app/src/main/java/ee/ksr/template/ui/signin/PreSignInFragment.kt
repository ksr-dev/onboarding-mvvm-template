package ee.ksr.template.ui.signin

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dagger.hilt.android.AndroidEntryPoint
import ee.ksr.template.databinding.FragmentPreSignInBinding
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PreSignInFragment : Fragment() {

    private var _binding: FragmentPreSignInBinding? = null
    private val binding get() = _binding!!
    private val args: PreSignInFragmentArgs by navArgs()
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private var googleSignInResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            viewModel.handleGoogleSignInResult(it)
        }

    private val viewModel: SignInViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    if (it.isLoading) {
                        binding.progressIndicator.visibility = View.VISIBLE
                    } else {
                        binding.progressIndicator.visibility = View.GONE
                    }
                    if (it.toastMessageResId != null) {
                        Toast.makeText(requireContext(), it.toastMessageResId, Toast.LENGTH_SHORT)
                            .show()
                    }
                    if (it.loginData != null) {
                        Toast.makeText(requireContext(), it.loginData.userName, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentPreSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        val hasGoogleSignIn = args.hasGoogleSignIn
        if (hasGoogleSignIn) {
            binding.signInGoogleButton.setOnClickListener {
                startSignInWithGoogle()
            }
        } else {
            binding.signInGoogleButton.visibility = View.GONE
        }

        binding.signInButton.setOnClickListener {
            val action = PreSignInFragmentDirections.actionPreSignInFragmentToSignInFragment()
            findNavController().navigate(action)
        }
        binding.signUpButton.setOnClickListener {
            val action = PreSignInFragmentDirections.actionPreSignInFragmentToUserCreationFragment()
            findNavController().navigate(action)
        }
    }

    private fun startSignInWithGoogle() {
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        googleSignInResultLauncher.launch(signInIntent)
    }
}