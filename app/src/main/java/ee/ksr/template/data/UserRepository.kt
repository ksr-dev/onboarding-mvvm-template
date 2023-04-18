package ee.ksr.template.data

interface UserRepository {
    fun finalizeLogin(userName: String, isGoogle: Boolean): LoginData?
    fun createUser(userName: String, password: String): LoginData?
}