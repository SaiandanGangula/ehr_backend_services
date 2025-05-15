package sirobilt.meghasanjivini.patientregistration.controller

import jakarta.inject.Inject
import jakarta.transaction.Transactional
import jakarta.validation.Valid
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.tags.Tag
import org.jboss.logging.Logger
import sirobilt.meghasanjivini.patientregistration.dto.*
import sirobilt.meghasanjivini.patientregistration.service.PatientService
import java.net.URI
import java.time.LocalDate
import java.util.*

@Tag(name = "Patient-Registration")
@Path("/api/patients")                   // <— keep /api prefix once
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class PatientController @Inject constructor(

    private val patientSvc: PatientService
) {



    private val logger: Logger = Logger.getLogger(PatientController::class.java)

    @POST
    fun register(dto: PatientRegistrationDto): Response {
        try {
            logger.info("Incoming Register Patient Request: $dto")

            val response = patientSvc.register(dto)

            logger.info("Patient created successfully: patientId=${response.patientId}")
            return Response.status(Response.Status.CREATED).entity(response).build()

        } catch (e: Exception) {
            logger.error("Error occurred during patient registration", e)
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(mapOf("error" to e.localizedMessage))
                .build()
        }
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


    @DELETE
    @Path("/{id}")
    @Operation(
        summary = "Delete a patient",
        description = "Deletes a patient by UUID including all associated data.",

    )
    fun deletePatient(@PathParam("id") id: UUID): Response {
        try {
            patientSvc.delete(id)
            return Response.noContent().header("API-Response", "Patient Deleted").build() // 204 No Content on successful deletion
        } catch (e: NotFoundException) {
            return Response.status(Response.Status.NOT_FOUND)
                .entity(mapOf("error" to "Patient not found with id $id"))
                .build()
        } catch (e: Exception) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(mapOf("error" to "Internal Server Error"))
                .build()
        }
    }}
