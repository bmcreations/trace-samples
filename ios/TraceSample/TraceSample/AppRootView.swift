import SwiftUI
import TraceSDK

struct AppRootView: View {

    @EnvironmentObject var auth: AuthViewModel
    @State private var path: [AppRoute] = []

    private let mapper = TraceRouteMapper<AppRoute> { m in
        m.route("/product/{id}")  { .product(id: $0.require("id")) }
        m.route("/invite/{code}") { .invite(code: $0.require("code")) }
        m.route("/checkout")      { .checkout(promo: $0["promo"]) }
        // Action deep link — no navigation, just a side-effect
        m.action("/claim/promo/{amount}") { _ in /* claimReward($0.require("amount")) */ }
    }

    var body: some View {
        Group {
            if auth.isLoggedIn {
                NavigationStack(path: $path) {
                    HomeView(path: $path)
                        .navigationDestination(for: AppRoute.self) { route in
                            switch route {
                            case .product(let id):     ProductView(id: id)
                            case .invite(let code):    InviteView(code: code)
                            case .checkout(let promo): CheckoutView(promo: promo)
                            }
                        }
                }
            } else {
                LoginView()
            }
        }
        .onDeepLink(
            path: $path,
            mapper: mapper,
            authGate: { auth.isLoggedIn }
        )
        .animation(.easeInOut(duration: 0.2), value: auth.isLoggedIn)
    }
}
