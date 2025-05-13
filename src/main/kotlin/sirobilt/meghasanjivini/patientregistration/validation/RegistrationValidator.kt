package sirobilt.meghasanjivini.patientregistration.validation

import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.WebApplicationException
import jakarta.ws.rs.core.Response
import sirobilt.meghasanjivini.patientregistration.dto.PatientRegistrationDto
import sirobilt.meghasanjivini.patientregistration.service.FieldConfigService
import kotlin.reflect.full.memberProperties

/**
 * Runs at runtime, using the metadata in registration_field_config,
 * to check that all fields marked required AND visible are present.
 */
@ApplicationScoped
class RegistrationValidator(
    private val fieldSvc: FieldConfigService
) {

    /** Throws 400 BAD REQUEST if required fields are missing. */
    fun validate(dto: PatientRegistrationDto) {
        val required = fieldSvc.visible()            // <-- list of FieldConfigDto
            .filter { it.required }
            .map { it.name }
            .toSet()

        val missing = dto::class.memberProperties
            .filter { it.name in required }
            .filter { it.getter.call(dto) == null }
            .map { it.name }

        if (missing.isNotEmpty()) {
            throw WebApplicationException(
                "Missing required fields: ${missing.joinToString()}",
                Response.Status.BAD_REQUEST
            )
        }
    }
}
