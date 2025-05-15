package sirobilt.meghasanjivini.masterdata.repository

import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped
import sirobilt.meghasanjivini.masterdata.model.*
import java.util.UUID

@ApplicationScoped
class LookupRepository: PanacheRepository<LookupValue> {
    fun findByCategory(cat: String) =
        list("category = ?1 AND active = true ORDER BY sortOrder", cat)
}

@ApplicationScoped
class CountryRepository: PanacheRepository<Country>

@ApplicationScoped
class StateRepository: PanacheRepository<State> {
    fun findByCountry(countryId: UUID) =
        list("country.id = ?1 ORDER BY name", countryId)
}

@ApplicationScoped
class DistrictRepository: PanacheRepository<District> {
    fun findByState(stateId: UUID) =
        list("state.id = ?1 ORDER BY name", stateId)
}
