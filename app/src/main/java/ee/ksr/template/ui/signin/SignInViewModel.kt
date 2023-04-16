package ee.ksr.template.ui.signin

import android.app.Activity
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import ee.ksr.template.R
import ee.ksr.template.data.LoginData
import ee.ksr.template.usecases.GoogleLoginUseCase
import ee.ksr.template.usecases.LoginUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val googleLoginUseCase: GoogleLoginUseCase,
    private val loginUseCase: LoginUseCase
) :
    ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private fun loginWithGoogleAccount(userName: String) {
        _uiState.update {
            it.copy(isLoading = true)
        }
        viewModelScope.launch {
            googleLoginUseCase.login(userName).let { loginData ->
                if (loginData == null) {
                    showToast(R.string.user_not_found)
                } else {
                    _uiState.update {
                        it.copy(isLoading = false, loginData = loginData)
                    }
                }
            }
        }
    }

    private fun login(userName: String, password: String) {
        _uiState.update {
            it.copy(isLoading = true)
        }
        viewModelScope.launch {
            loginUseCase.login(userName, password).let { loginData ->
                if (loginData == null) {
                    showToast(R.string.user_not_found)
                } else {
                    _uiState.update {
                        it.copy(isLoading = false, loginData = loginData)
                    }
                }
            }
        }
    }

    private fun showToast(messageResId: Int) {
        _uiState.update {
            it.copy(toastMessageResId = messageResId)
        }
    }

    fun handleGoogleSignInResult(it: ActivityResult?) {
        if (it == null) {
            _uiState.update {
                it.copy(toastMessageResId = R.string.sign_in_google_null)
            }
            return
        }
        when (it.resultCode) {
            Activity.RESULT_OK -> {
                // The Task returned from this call is always completed, no need to attach
                // a listener.
                val task: Task<GoogleSignInAccount> =
                    GoogleSignIn.getSignedInAccountFromIntent(it.data)
                handleSignInResult(task)
            }

            Activity.RESULT_CANCELED -> {
                _uiState.update {
                    it.copy(toastMessageResId = R.string.sign_in_google_cancelled)
                }
            }

            else -> {
                Log.e(GOOGLE_ACTIVITY_TAG, "resultData: $it")
            }
        }
    }


    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount = completedTask.getResult(ApiException::class.java)
            loginWithGoogleAccount(account.email.toString())
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(GOOGLE_ACTIVITY_TAG, "signInResult:failed code=${e.statusCode}")
            _uiState.update {
                it.copy(toastMessageResId = R.string.sign_in_google_failed)
            }
        }
    }

    fun startLogin(identifier: String, password: String) {
        if (identifier.isNotBlank() && password.isNotBlank()) {
            login(identifier, password)
        }
    }

    companion object {
        private const val GOOGLE_ACTIVITY_TAG = "GoogleActivityResultCode"
    }
}

data class UiState(
    val isLoading: Boolean = false,
    val toastMessageResId: Int? = null,
    val loginData: LoginData? = null
)