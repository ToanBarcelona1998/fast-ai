package com.example.repository

import com.example.database.PackageTable
import com.example.domain.models.entity.Package
import com.example.domain.models.requests.PackageAddRequest
import com.example.domain.models.requests.PackageGetAllRequest
import com.example.repository.interfaces.IPackageRepository
import kotlinx.datetime.Clock
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

final class PackageRepository : IPackageRepository {
    override suspend fun add(request: PackageAddRequest): Package {
        val createdDate = Clock.System.now()
        return transaction {
            val id = PackageTable.insert {
                it[name] = request.name
                it[credits] = request.credits
                it[description] = request.description
                it[basePrice] = request.basePrice
                it[isActive] = true
                it[createdAt] = createdDate
            }[PackageTable.id]

            Package(
                id = id,
                name = request.name,
                credits = request.credits,
                description = request.description,
                basePrice = request.basePrice,
                promoPrice = null,
                createdAt = createdDate.toString(),
                isActive = true,
                promoEndTime = null,
                updatedAt = null
            )
        }
    }

    override suspend fun getAll(): List<Package> {
        return transaction {
            PackageTable.selectAll().orderBy(PackageTable.basePrice, SortOrder.ASC).map {
                Package(
                    id = it[PackageTable.id],
                    name = it[PackageTable.name],
                    credits = it[PackageTable.credits],
                    description = it[PackageTable.description],
                    basePrice = it[PackageTable.basePrice],
                    promoPrice = it[PackageTable.promoPrice],
                    isActive = it[PackageTable.isActive],
                    createdAt = it[PackageTable.createdAt].toString(),
                    updatedAt = it[PackageTable.updatedAt]?.toString(),
                    promoEndTime = it[PackageTable.promoEndTime]?.toString(),
                )
            }
        }
    }

    override suspend fun getAll(request: PackageGetAllRequest): List<Package> {
        TODO("Not yet implemented")
    }

    override suspend fun get(id: Int): Package? {
        return transaction {
            PackageTable.selectAll().where(PackageTable.id eq id).limit(1).map {
                Package(
                    id = it[PackageTable.id],
                    name = it[PackageTable.name],
                    credits = it[PackageTable.credits],
                    description = it[PackageTable.description],
                    basePrice = it[PackageTable.basePrice],
                    promoPrice = it[PackageTable.promoPrice],
                    isActive = it[PackageTable.isActive],
                    createdAt = it[PackageTable.createdAt].toString(),
                    updatedAt = it[PackageTable.updatedAt]?.toString(),
                    promoEndTime = it[PackageTable.promoEndTime]?.toString(),
                )
            }.firstOrNull()
        }
    }
}