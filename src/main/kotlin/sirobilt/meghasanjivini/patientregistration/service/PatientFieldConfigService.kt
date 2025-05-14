package sirobilt.meghasanjivini.patientregistration.service

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import jakarta.ws.rs.NotFoundException
import sirobilt.meghasanjivini.patientregistration.dto.*
import sirobilt.meghasanjivini.patientregistration.model.*
import sirobilt.meghasanjivini.patientregistration.repository.*
import java.time.LocalDate
import java.util.*

@ApplicationScoped
class PatientService @Inject constructor(
    private val patientRepo: PatientRepository,
    private val contactRepo: PatientContactRepository,
    private val addressRepo: PatientAddressRepository,
    private val emergencyRepo: EmergencyContactRepository,
    private val insuranceRepo: PatientInsuranceRepository
) {

    @Transactional
    fun register(dto: PatientRegistrationDto): PatientResponseDto {
        val patient = dto.toEntity()
        patientRepo.persist(patient)

        dto.contacts?.map { it.toEntity(patient) }?.let { contactRepo.persist(it) }
        dto.addresses?.map { it.toEntity(patient) }?.let { addressRepo.persist(it) }
        dto.emergencyContacts?.map { it.toEntity(patient) }?.let { emergencyRepo.persist(it) }
        dto.insurance?.toEntity(patient)?.let { insuranceRepo.persist(it) }

        return patient.toDto()
    }

    @Transactional
    fun update(id: UUID, dto: UpdatePatientDto): PatientResponseDto {
        val patient = patientRepo.findById(id) ?: throw NotFoundException()

        dto.firstName?.let { patient.firstName = it }
        dto.middleName?.let { patient.middleName = it }
        dto.lastName?.let { patient.lastName = it }
        dto.dateOfBirth?.let { patient.dateOfBirth = it }
        dto.gender?.let { patient.gender = it }
        dto.title?.let { patient.title = it }
        dto.age?.let { patient.age = it }
        dto.bloodGroup?.let { patient.bloodGroup = it }
        dto.maritalStatus?.let { patient.maritalStatus = it }
        dto.occupation?.let { patient.occupation = it }

        dto.contacts?.let { contactDtos ->
            patient.contacts = contactDtos.map { dtoContact ->
                PatientContact(
                    patient = patient,
                    mobileNumber = dtoContact.mobileNumber.orEmpty(),
                    phoneNumber = dtoContact.phoneNumber,
                    email = dtoContact.email,
                    preferredContactMode = dtoContact.preferredContactMode,
                    phoneContactPreference = dtoContact.phoneContactPreference,
                    consentToShare = dtoContact.consentToShare
                )
            }.toMutableList()
        }

        patientRepo.persist(patient)

        return patient.toDto()
    }

    fun listAll(): List<PatientResponseDto> =
        patientRepo.findAll().list().map { it.toDto() }

    @Transactional
    fun delete(id: UUID) {
        if (!patientRepo.deleteById(id)) throw NotFoundException()
    }

    fun getById(id: UUID): PatientResponseDto =
        patientRepo.findById(id)?.toDto() ?: throw NotFoundException()


    fun exists(
        firstName: String,
        identifierType: IdentifierType,
        identifierNumber: String?,
        primaryEmail: String?
    ): Boolean {

        val abha = if (identifierType == IdentifierType.ABHA)
            identifierNumber else null

        return patientRepo.findDuplicate(firstName, abha, primaryEmail) != null
    }

    fun search(
        id: UUID?,
        first: String?, last: String?,
        mobile: String?, email: String?,
        dobFrom: LocalDate?, dobTo: LocalDate?
    ): List<PatientResponseDto> =
        patientRepo.search(id, first, last, mobile, email, dobFrom, dobTo)
            .map { it.toDto() }

    fun searchByCityOrName(city: String?, name: String?): List<PatientResponseDto> =
        patientRepo.searchByCityOrName(city, name)
            .map { it.toDto() }


}

// Move this to the bottom or another dedicated mapper file (recommended).
fun Patient.toDto(): PatientResponseDto {
    val firstContact = contacts.firstOrNull()
    return PatientResponseDto(
        patientId = id,
        fullName = listOfNotNull(firstName, middleName, lastName).joinToString(" "),
        facilityId = facilityId,
        identifierType = identifierType,
        identifierNumber = identifierNumber,
        phone = firstContact?.phoneNumber,
        email = firstContact?.email
    )
}
