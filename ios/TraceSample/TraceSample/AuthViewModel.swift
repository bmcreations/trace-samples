import SwiftUI

class AuthViewModel: ObservableObject {
    @Published var isLoggedIn = false

    func login()  { isLoggedIn = true }
    func logout() { isLoggedIn = false }
}
