package sirobilt.meghasanjivini.masterdata.repository


import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped
import sirobilt.meghasanjivini.masterdata.model.Facility
import java.util.*

@ApplicationScoped
class FacilityRepository : PanacheRepository<Facility> {
    fun findByHospitalId(id: UUID) = find("hospitalId", id).firstResult()
}
