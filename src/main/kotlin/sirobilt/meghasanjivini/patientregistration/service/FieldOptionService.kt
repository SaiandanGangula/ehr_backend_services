package sirobilt.meghasanjivini.patientregistration.service

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import sirobilt.meghasanjivini.patientregistration.dto.FieldOptionDto
import sirobilt.meghasanjivini.patientregistration.model.FieldOption
import sirobilt.meghasanjivini.patientregistration.repository.FieldOptionRepository

@ApplicationScoped
class FieldOptionService @Inject constructor(
    private val optionRepo: FieldOptionRepository
) {

    /** Replace all options for one fieldName with the supplied list. */
    @Transactional
    fun upsert(list: List<FieldOptionDto>): List<FieldOption> {
        require(list.isNotEmpty()) { "Option list must not be empty" }

        val fieldName = list.first().fieldName
        optionRepo.delete("fieldName", fieldName)            // delete old rows

        // insert & return the new entities
        return list.map { dto ->
            FieldOption(
                fieldName = dto.fieldName,
                value     = dto.value,
                display   = dto.display,
                sortOrder = dto.sortOrder
            ).also { optionRepo.persist(it) }                // persist THEN return
        }
    }
}
