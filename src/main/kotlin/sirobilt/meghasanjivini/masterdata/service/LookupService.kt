package sirobilt.meghasanjivini.masterdata.service

import jakarta.enterprise.context.ApplicationScoped
import sirobilt.meghasanjivini.masterdata.dto.*
import sirobilt.meghasanjivini.masterdata.repository.*
import sirobilt.meghasanjivini.masterdata.model.*
import java.util.UUID

@ApplicationScoped
class LookupService(private val lookupRepo: LookupRepository) {
    fun get(category: String): List<LookupDto> =
        lookupRepo.findByCategory(category)
            .map { LookupDto(it.code, it.displayName, it.sortOrder) }
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