package shuttle.payments.presentation.mapper

import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.QueryProductDetailsParams
import shuttle.payments.domain.model.Product

internal class QueryProductDetailsParamsMapper {

    fun toQueryProductDetailsParams(product: Product) = QueryProductDetailsParams.newBuilder()
        .setProductList(
            listOf(
                QueryProductDetailsParams.Product.newBuilder()
                    .setProductId(product.id)
                    .setProductType(product.getBillingType())
                    .build()
            )
        )
        .build()

    private fun Product.getBillingType(): String =
        when (type) {
            Product.Type.InApp -> BillingClient.ProductType.INAPP
        }
}
