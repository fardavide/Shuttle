package shuttle.payments.domain.model

enum class Product(val id: String, val type: Type) {

    Small(id = "small", type = Type.InApp),
    Large(id = "large", type = Type.InApp);

    enum class Type {

        InApp
    }
}
