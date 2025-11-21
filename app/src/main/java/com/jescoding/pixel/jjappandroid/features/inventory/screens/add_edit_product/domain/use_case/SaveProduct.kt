package com.jescoding.pixel.jjappandroid.features.inventory.screens.add_edit_product.domain.use_case

import com.jescoding.pixel.jjappandroid.R
import com.jescoding.pixel.jjappandroid.core.domain.model.DashboardItem
import com.jescoding.pixel.jjappandroid.core.domain.repository.DashboardRepository
import com.jescoding.pixel.jjappandroid.core.utils.ValidationUtils
import com.jescoding.pixel.jjappandroid.features.inventory.screens.add_edit_product.domain.model.NewProductInput
import java.util.UUID
import javax.inject.Inject

class SaveProduct @Inject constructor(
    private val repository: DashboardRepository,
    private val getPermanentImageUri: GetPermanentImageUri
) {
    suspend operator fun invoke(input: NewProductInput) {
        val itemSku = input.itemSku ?: UUID.randomUUID().toString()
        val availableStock = input.availableStock.toIntOrNull() ?: 0
        val onHandStock = input.onHandStock.toIntOrNull() ?: 0
        val onTheWayStock = input.onTheWayStock.toIntOrNull() ?: 0

        val itemCost = ValidationUtils
            .getValidatedCurrency(input.itemCostPrice)
            .toBigDecimal()
        val itemSelling = ValidationUtils
            .getValidatedCurrency(input.itemSellingPrice)
            .toBigDecimal()

        val permanentUri = input.itemUri?.let { getPermanentImageUri(it) }

        val dashboardItem = DashboardItem(
            itemSku = itemSku,
            itemName = input.itemName,
            itemVariant = input.itemVariant,
            availableStock = availableStock,
            onHandStock = onHandStock,
            onTheWayStock = onTheWayStock,
            itemCostPrice = itemCost,
            itemSellingPrice = itemSelling,
            itemImageResId = R.drawable.round_local_convenience_store_24,
            itemUri = permanentUri
        )

        repository.saveDashboardItems(dashboardItem)
    }
}