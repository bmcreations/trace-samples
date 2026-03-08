import Foundation

enum AppRoute: Hashable {
    case product(id: String)
    case invite(code: String)
    case checkout(promo: String?)
}
