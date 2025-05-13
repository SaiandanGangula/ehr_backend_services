package sirobilt.meghasanjivini.patientregistration.controller

import jakarta.inject.Inject
import jakarta.transaction.Transactional
import jakarta.validation.Valid
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.tags.Tag
import org.jboss.resteasy.reactive.RestResponse
import sirobilt.meghasanjivini.patientregistration.dto.*
import sirobilt.meghasanjivini.patientregistration.service.FieldConfigService
import sirobilt.meghasanjivini.patientregistration.service.PatientService
import java.net.URI
import java.time.LocalDate
import java.util.*

@Tag(name = "Patient-Registration")
@Path("/api/patients")                   // <— keep /api prefix once
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class PatientController @Inject constructor(
    private val fieldSvc: FieldConfigService,
    private val patientSvc: PatientService
) {

    /* ---------- dynamic form blueprint ---------- */
    @GET @Path("/form-config")
    @Operation(summary = "Return the current form blueprint (visible fields + options).")
    fun formConfig() = fieldSvc.visible()

    /* ---------- create ---------- */
    @POST
    @Transactional
    @Operation(summary = "Register a patient together with contacts and other aggregates.")
    fun register(@Valid dto: PatientRegistrationDto): Response? {
        val saved = patientSvc.register(dto)

        return Response.status(Response.Status.CREATED)   // 201
            .entity(saved)                               // body
            .location(URI.create("/api/patients/${saved.patientId}"))
            .build()
    }

    /* ---------- update (identifiers immutable) ---------- */
    @PUT @Path("/{id}") @Transactional
    @Operation(summary = "Update patient details; identifier* fields are ignored.")
    fun update(
        @PathParam("id") id: UUID,
        @Valid dto: UpdatePatientDto   // a DTO that omits identifier fields
    ): PatientResponseDto =
        patientSvc.update(id, dto)

    /* ---------- search / list ---------- */
    @GET
    @Operation(summary = "List or search patients")
    fun searchOrList(
        @QueryParam("id") id: UUID?,
        @QueryParam("firstName") firstName: String?,
        @QueryParam("lastName") lastName: String?,
        @QueryParam("mobile") mobile: String?,
        @QueryParam("email") email: String?,
        @QueryParam("dobFrom") dobFrom: LocalDate?,
        @QueryParam("dobTo") dobTo: LocalDate?,
        @QueryParam("city") city: String?,
        @QueryParam("name") name: String?
    ): List<PatientResponseDto> =
        when {
            listOf(id, firstName, lastName, mobile, email, dobFrom, dobTo).any { it != null } ->
                patientSvc.search(id, firstName, lastName, mobile, email, dobFrom, dobTo)
            !city.isNullOrBlank() || !name.isNullOrBlank() ->
                patientSvc.searchByCityOrName(city, name)
            else -> patientSvc.listAll()
        }
}
