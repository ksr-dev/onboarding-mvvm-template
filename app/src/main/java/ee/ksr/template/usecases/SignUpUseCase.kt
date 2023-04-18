package ee.ksr.template.usecases

import ee.ksr.template.data.LoginData
import ee.ksr.template.data.UserRepository
import kotlinx.coroutines.delay
import javax.inject.Inject

class SignUpUseCase @Inject constructor(private val userRepository: UserRepository) {

    suspend fun createUser(userName: String, password: String): LoginData? {
        delay(1500) // to simulate loading times for fetching data from BE
        return userRepository.createUser(userName, password)
    }
}