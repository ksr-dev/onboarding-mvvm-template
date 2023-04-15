package ee.ksr.template.data

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor() : UserRepository {
    override fun finalizeLogin(userName: String, isGoogle: Boolean): LoginData {
        return LoginData(userName = userName, isGoogle = isGoogle)
    }

    override fun createUser(userName: String, password: String): LoginData? {
        return LoginData(userName = userName, isGoogle = false)
    }
}