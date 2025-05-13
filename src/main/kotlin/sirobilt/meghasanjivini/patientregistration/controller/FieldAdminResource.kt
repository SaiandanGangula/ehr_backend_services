package sirobilt.meghasanjivini.patientregistration.controller

import jakarta.inject.Inject
import jakarta.transaction.Transactional
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.tags.Tag
import sirobilt.meghasanjivini.patientregistration.dto.FieldConfigDto
import sirobilt.meghasanjivini.patientregistration.dto.FieldOptionDto
import sirobilt.meghasanjivini.patientregistration.service.FieldConfigService
import sirobilt.meghasanjivini.patientregistration.service.FieldOptionService

@Tag(name = "Field-Admin")
@Path("/admin/fields")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class FieldConfigController @Inject constructor(
    private val svc: FieldConfigService
) {
    @GET
    @Operation(summary = "List every field-config row")
    fun list() = svc.all()

    @PUT
    @Transactional
    @Operation(summary = "Create or update one field-config row")
    fun upsert(dto: FieldConfigDto) = svc.upsert(dto)
}

@Tag(name = "Field-Admin")
@Path("/admin/field-options")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class FieldOptionController @Inject constructor(
    private val svc: FieldOptionService
) {
    @POST
    @Transactional
    @Operation(summary = "Replace all options for the given fieldName")
    fun replace(list: List<FieldOptionDto>) = svc.upsert(list)
}
