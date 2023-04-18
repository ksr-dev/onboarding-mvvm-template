package ee.ksr.template.ui.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ee.ksr.template.R
import ee.ksr.template.data.LoginData
import ee.ksr.template.usecases.SignUpUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(private val signUpUseCase: SignUpUseCase) :
    ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()
    private val _userIdentifier = MutableStateFlow<String?>(null)

    private fun createUser(userName: String, password: String) {
        _uiState.update {
            it.copy(isLoading = true)
        }
        viewModelScope.launch {
            signUpUseCase.createUser(userName, password).let { loginData ->
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

    fun saveUserName(userName: String) {
        _userIdentifier.value = userName
    }

    fun startCreateUser(firstPassword: String, secondPassword: String) {
        val userName = _userIdentifier.value
        if (firstPassword.isNotBlank() && secondPassword.isNotBlank() && firstPassword == secondPassword && userName != null) {
            createUser(userName, secondPassword)
        }
    }
}

data class UiState(
    val isLoading: Boolean = false,
    val toastMessageResId: Int? = null,
    val loginData: LoginData? = null
)