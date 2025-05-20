package sirobilt.meghasanjivini.masterdata.service

import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional
import sirobilt.meghasanjivini.masterdata.dto.*
import sirobilt.meghasanjivini.masterdata.repository.*
import sirobilt.meghasanjivini.masterdata.model.*
import java.time.Instant
import java.util.UUID

@ApplicationScoped
class LookupService(private val lookupRepo: LookupValueRepository) {
    fun getByCategory(category: String): List<LookupDto> =
        lookupRepo.findByCategory(category)
            .map { LookupDto(it.code, it.displayName, it.sortOrder) }

    @Transactional
    fun create(dto: LookupValueCreateDTO): LookupValue {
        val entity = LookupValue(
            category = dto.category,
            code = dto.code,
            displayName = dto.displayName,
            sortOrder = dto.sortOrder ?: 0
        )
        lookupRepo.persist(entity)
        return entity
    }

    @Transactional
    fun update(id: UUID, dto: LookupValueUpdateDTO): LookupValue {
        val entity = lookupRepo.findById(id) ?: throw IllegalArgumentException("Not found")

        dto.code?.let { entity.code = it }
        dto.displayName?.let { entity.displayName = it }
        dto.sortOrder?.let { entity.sortOrder = it }
        dto.active?.let { entity.active = it }
        entity.updatedAt = Instant.now()

        return entity
    }

    @Transactional
    fun deactivate(category: String, value: String): LookupValue {
        val entity = lookupRepo
            .findActiveByCategoryAndValue(category, value)
            ?: throw IllegalArgumentException(
                "No active lookup value '$value' found in category '$category'"
            )

        entity.active    = false
        entity.updatedAt = Instant.now()
        return entity
    }

    fun getAll(): List<LookupValue> = lookupRepo.listAll()

}

@ApplicationScoped
class CountryService(private val countryRepo: CountryRepository) {
    fun list(): List<CountryDto> =
        countryRepo.listAll().map { CountryDto(it.id, it.name) }
}

@ApplicationScoped
class StateService(private val stateRepo: StateRepository) {
    fun listByCountry(countryId: UUID): List<StateDto> =
        stateRepo.findByCountry(countryId).map { StateDto(it.id, it.name) }
}

@ApplicationScoped
class DistrictService(private val districtRepo: DistrictRepository) {
    fun listByState(stateId: UUID): List<DistrictDto> =
        districtRepo.findByState(stateId).map { DistrictDto(it.id, it.name) }
}