package sirobilt.meghasanjivini.masterdata.controller

import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import sirobilt.meghasanjivini.masterdata.dto.*
import sirobilt.meghasanjivini.masterdata.service.*
import java.util.UUID

@Path("/api/lookups")
@Produces(MediaType.APPLICATION_JSON)
class LookupResource(private val service: LookupService) {
    @GET
    @Path("/{category}")
    fun list(@PathParam("category") category: String): List<LookupDto> =
        service.get(category)
}

@Path("/api/geo")
@Produces(MediaType.APPLICATION_JSON)
class GeographyResource(
    private val countryService: CountryService,
    private val stateService: StateService,
    private val districtService: DistrictService
) {
    @GET @Path("/countries")
    fun countries(): List<CountryDto> = countryService.list()

    @GET @Path("/countries/{id}/states")
    fun states(@PathParam("id") countryId: UUID): List<StateDto> =
        stateService.listByCountry(countryId)

    @GET @Path("/states/{id}/districts")
    fun districts(@PathParam("id") stateId: UUID): List<DistrictDto> =
        districtService.listByState(stateId)
}
