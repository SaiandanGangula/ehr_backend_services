package sirobilt.meghasanjivini.masterdata.repository

import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepositoryBase
import jakarta.enterprise.context.ApplicationScoped
import sirobilt.meghasanjivini.masterdata.dto.LookupDto
import sirobilt.meghasanjivini.masterdata.model.*
import java.util.UUID

@ApplicationScoped
class LookupValueRepository : PanacheRepositoryBase<LookupValue, UUID>{
    fun findByCategory(cat: String) : List<LookupValue> =
        list("category = ?1 AND active = true ORDER BY sortOrder", cat)

    fun findActiveByCategoryAndValue(cat: String, value: String): LookupValue? =
        find(
            "category = ?1 AND value = ?2 AND active = true",
            cat, value
        ).firstResult()
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
