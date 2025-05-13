package sirobilt.meghasanjivini.patientregistration.validation

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.validation.ConstraintViolationException
import jakarta.validation.Validator
import sirobilt.meghasanjivini.patientregistration.dto.*
import sirobilt.meghasanjivini.patientregistration.model.FieldType
import sirobilt.meghasanjivini.patientregistration.service.FieldConfigService

/**
 * Single entry-point for every validation rule related to patient registration.
 *
 * Mixes:
 *   1. Bean-Validation annotations on DTOs (validated by `jakarta.validation.Validator`)
 *   2. Custom programmatic checks such as dynamic-field requirements
 *   3. Regex rule for Indian phone numbers (+91XXXXXXXXXX)
 */
@ApplicationScoped
class PatientValidator @Inject constructor(
    private val beanValidator: Validator,
    private val cfgSvc: FieldConfigService
) {

    /* ---------------- PUBLIC API ---------------- */

    /**
     * Throws [ConstraintViolationException] if *any* rule is violated.
     */
    fun validate(dto: PatientRegistrationDto) {
        // 1. standard Bean Validation on the full graph
        beanValidator.validate(dto).also { if (it.isNotEmpty()) throw ConstraintViolationException(it) }

        // 2. dynamic – required fields configured in registration_field_config
        validateDynamicRequired(dto)

        // 3. custom business rules (phone format, duplicates, etc.)
        validateContacts(dto.contacts)
    }

    /* ---------------- PRIVATE HELPERS ---------------- */

    private fun validateDynamicRequired(dto: PatientRegistrationDto) {
        val requiredFields = cfgSvc.visible()
            .filter { it.required }
            .map { it.name }
            .toSet()

        val missing = mutableListOf<String>()

        fun missingIfEmpty(name: String, value: Any?) {
            if (name in requiredFields && (value == null ||
                        (value is String && value.isBlank())))
                missing += name
        }

        missingIfEmpty("firstName",     dto.firstName)
        missingIfEmpty("lastName",      dto.lastName)
        missingIfEmpty("dateOfBirth",   dto.dateOfBirth)
        missingIfEmpty("gender",        dto.gender)
        // add others if you surface them on PatientRegistrationDto

        if (missing.isNotEmpty())
            throw IllegalArgumentException("Missing required fields: ${missing.joinToString()}")
    }

    /** +91 followed by 10 digits, first digit ≠ 0 */
    private val phoneRegex = Regex("""^\+91[1-9]\d{9}$""")

    private fun validateContacts(contacts: List<ContactDto>?) {
        contacts?.forEachIndexed { idx, c ->
            if (!phoneRegex.matches(c.phoneNumber))
                throw IllegalArgumentException(
                    "contacts[$idx].phoneNumber must start with “+91” and contain exactly 10 digits"
                )
        }
    }
}
