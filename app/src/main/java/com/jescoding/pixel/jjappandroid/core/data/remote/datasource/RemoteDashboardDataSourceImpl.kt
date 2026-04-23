package com.jescoding.pixel.jjappandroid.core.data.remote.datasource

import com.jescoding.pixel.jjappandroid.core.data.remote.dto.DashboardItemDto
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDashboardDataSourceImpl @Inject constructor(
    private val supabaseClient: SupabaseClient
) : RemoteDashboardDataSource {

    private val table get() = supabaseClient.postgrest["dashboard_items"]

    override suspend fun upsertItem(dto: DashboardItemDto): Result<Unit> {
        return runCatching {
            table.upsert(dto)
            Unit
        }
    }

    override suspend fun softDeleteItem(itemSku: String, userId: String): Result<Unit> {
        return runCatching {
            table.update(
                {
                    set("is_deleted", true)
                }
            ) {
                filter {
                    eq("item_sku", itemSku)
                    eq("user_id", userId)
                }
            }
            Unit
        }
    }

    override suspend fun getItemsModifiedAfter(
        timestamp: String,
        userId: String
    ): Result<List<DashboardItemDto>> {
        return runCatching {
            table.select {
                filter {
                    eq("user_id", userId)
                    gt("updated_at", timestamp)
                }
            }.decodeList<DashboardItemDto>()
        }
    }
}
