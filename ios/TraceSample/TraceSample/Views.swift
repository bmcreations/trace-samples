import SwiftUI

// ─── Login ───────────────────────────────────────────────────────────────────

struct LoginView: View {
    @EnvironmentObject var auth: AuthViewModel

    var body: some View {
        VStack(spacing: 24) {
            Spacer()
            Image(systemName: "link.circle.fill")
                .font(.system(size: 64))
                .foregroundStyle(.tint)
            Text("Trace Sample")
                .font(.largeTitle.bold())
            Text("Sign in to continue.\nDeferred deep links resume automatically after login.")
                .multilineTextAlignment(.center)
                .foregroundStyle(.secondary)
            Spacer()
            Button {
                auth.login()
            } label: {
                Text("Continue")
                    .frame(maxWidth: .infinity)
            }
            .buttonStyle(.borderedProminent)
            .controlSize(.large)
        }
        .padding(32)
    }
}

// ─── Home ────────────────────────────────────────────────────────────────────

struct HomeView: View {
    @EnvironmentObject var auth: AuthViewModel
    @Binding var path: [AppRoute]

    var body: some View {
        List {
            Section("Deep link destinations") {
                Button("Product abc123") { path.append(.product(id: "abc123")) }
                Button("Product xyz789") { path.append(.product(id: "xyz789")) }
                Button("Invite CODE42")  { path.append(.invite(code: "CODE42")) }
                Button("Checkout")       { path.append(.checkout(promo: nil)) }
                Button("Checkout + promo SUMMER20") {
                    path.append(.checkout(promo: "SUMMER20"))
                }
            }
            Section("Test mode") {
                Text("Set TRACE_TEST=1 in scheme env vars to simulate a deferred deep link to /product/demo-123 on launch without a real server.")
                    .font(.caption)
                    .foregroundStyle(.secondary)
            }
            Section {
                Button("Log out", role: .destructive) {
                    auth.logout()
                    path = []
                }
            }
        }
        .navigationTitle("Home")
    }
}

// ─── Product ─────────────────────────────────────────────────────────────────

struct ProductView: View {
    let id: String

    var body: some View {
        List {
            Section("Deep link payload") {
                LabeledContent("Product ID", value: id)
            }
            Section("Route") {
                Text("/product/\(id)")
                    .font(.system(.body, design: .monospaced))
                    .foregroundStyle(.secondary)
            }
        }
        .navigationTitle("Product")
    }
}

// ─── Invite ──────────────────────────────────────────────────────────────────

struct InviteView: View {
    let code: String

    var body: some View {
        List {
            Section("Deep link payload") {
                LabeledContent("Invite code", value: code)
            }
            Section("Route") {
                Text("/invite/\(code)")
                    .font(.system(.body, design: .monospaced))
                    .foregroundStyle(.secondary)
            }
        }
        .navigationTitle("Invite")
    }
}

// ─── Checkout ────────────────────────────────────────────────────────────────

struct CheckoutView: View {
    let promo: String?

    var body: some View {
        List {
            Section("Deep link payload") {
                if let promo {
                    LabeledContent("Promo code", value: promo)
                } else {
                    Text("No promo code")
                        .foregroundStyle(.secondary)
                }
            }
            Section("Route") {
                Text(promo.map { "/checkout?promo=\($0)" } ?? "/checkout")
                    .font(.system(.body, design: .monospaced))
                    .foregroundStyle(.secondary)
            }
        }
        .navigationTitle("Checkout")
    }
}
