package com.jescoding.pixel.jjappandroid.features.inventory.item.presentation.component

import android.icu.text.NumberFormat
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jescoding.pixel.jjappandroid.R
import com.jescoding.pixel.jjappandroid.features.inventory.item.domain.model.InventoryItem
import com.jescoding.pixel.jjappandroid.features.inventory.item.presentation.data.FakeInventoryData
import com.jescoding.pixel.jjappandroid.shared.theme.JjappAndroidTheme

@Composable
fun InventoryBody(
    modifier: Modifier = Modifier,
    data: InventoryItem
) {
    val labelTextStyle = MaterialTheme.typography.bodyMedium.copy(
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onSurface
    )
    val valueTextStyle = MaterialTheme.typography.bodyMedium.copy(
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )

    val numberFormatter = remember { createNumberFormatter() }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 12.dp, end = 12.dp, top = 12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(all = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            InfoRow(
                labelRes = R.string.inventory_on_hand,
                value = data.onHandStock.toString(),
                labelStyle = labelTextStyle,
                valueStyle = valueTextStyle
            )
            Spacer(modifier = Modifier.height(12.dp))
            InfoRow(
                labelRes = R.string.inventory_available_stock,
                value = data.availableStock.toString(),
                labelStyle = labelTextStyle,
                valueStyle = valueTextStyle
            )
            Spacer(modifier = Modifier.height(12.dp))
            InfoRow(
                labelRes = R.string.inventory_on_the_way,
                value = data.onTheWayStock.toString(),
                labelStyle = labelTextStyle,
                valueStyle = valueTextStyle
            )
            Spacer(modifier = Modifier.height(12.dp))

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 4.dp),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)
            )

            Spacer(modifier = Modifier.height(12.dp))
            InfoRow(
                labelRes = R.string.inventory_selling_price,
                value = numberFormatter.format(data.itemSellingPrice),
                labelStyle = labelTextStyle,
                valueStyle = valueTextStyle
            )
            Spacer(modifier = Modifier.height(12.dp))
            InfoRow(
                labelRes = R.string.inventory_cost_price,
                value = numberFormatter.format(data.itemCostPrice),
                labelStyle = labelTextStyle,
                valueStyle = valueTextStyle
            )
        }
    }
}

@Composable
private fun InfoRow(
    @StringRes labelRes: Int,
    value: String,
    modifier: Modifier = Modifier,
    labelStyle: androidx.compose.ui.text.TextStyle = MaterialTheme.typography.bodyMedium,
    valueStyle: androidx.compose.ui.text.TextStyle = MaterialTheme.typography.bodyMedium
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(id = labelRes),
            style = labelStyle,
        )

        Text(
            text = value,
            style = valueStyle,
        )
    }
}

private fun createNumberFormatter(): NumberFormat {
    return NumberFormat.getInstance().apply {
        maximumFractionDigits = 2
        minimumFractionDigits = 0
    }
}


@Preview(showBackground = true)
@Composable
private fun InventoryItemHeaderPreview() {
    JjappAndroidTheme {
        InventoryBody(
            modifier = Modifier.padding(16.dp),
            data = FakeInventoryData.items.first()
        )
    }
}