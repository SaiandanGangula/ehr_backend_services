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
                    phoneNumber = dtoContact.phoneNumber.orEmpty(),
                    email = dtoContact.email,
                    preferredContactMode = dtoContact.preferredContactMode,
                    phoneContactPreference = dtoContact.phoneContactPreference,
                    consentToShare = dtoContact.consentToShare ?: false
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
        val abha = if (identifierType == IdentifierType.ABHA) identifierNumber else null
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


fun Patient.toDto(): PatientResponseDto {
    val firstContact = contacts?.firstOrNull()

    return PatientResponseDto(
        patientId = id!!,
        facilityId = facilityId!!,
        identifierType = identifierType!!,
        identifierNumber = identifierNumber!!,
        title = title,
        firstName = firstName,
        middleName = middleName,
        lastName = lastName,
        fullName = listOfNotNull(firstName, middleName, lastName).joinToString(" "),
        dateOfBirth = dateOfBirth,
        age = age,
        gender = gender,
        bloodGroup = bloodGroup,
        maritalStatus = maritalStatus,
        citizenship = citizenship,
        religion = religion,
        caste = caste,
        occupation = occupation,
        education = education,
        annualIncome = annualIncome,
        registrationDate = registrationDate!!,
        isActive = isActive!!,
        isDeceased = isDeceased!!,
        phone = firstContact?.phoneNumber,
        email = firstContact?.email,

        contacts = contacts.map { it.toDto() },
        addresses = addresses?.map { it.toDto() },
        emergencyContacts = emergencyContacts?.map { it.toDto() },
        billingReferral = billingReferral?.toDto(),
        insurance = insurance?.toDto(),
        abha = abha?.toDto(),
        informationSharing = informationSharing?.toDto(),
        referrals = referrals?.map { it.toDto() },
        relationships = relationships?.map { it.toDto() },
        tokens = tokens?.map { it.toDto() }
    )
}

fun PatientContact.toDto() = ContactDto(
    mobileNumber = this.mobileNumber,
    phoneNumber = this.phoneNumber,
    email = this.email,
    preferredContactMode = this.preferredContactMode,
    phoneContactPreference = this.phoneContactPreference,
    consentToShare = this.consentToShare
)

fun PatientAddress.toDto() = AddressDto(
    addressType = this.addressType,
    houseNoOrFlatNo = this.houseNoOrFlatNo,
    localityOrSector = this.localityOrSector,
    cityOrVillage = this.cityOrVillage,
    pincode = this.pincode,
    districtId = this.districtId,
    stateId = this.stateId,
    country = this.country
)

fun EmergencyContact.toDto() = EmergencyContactDto(
    contactName = this.contactName,
    relationship = this.relationship,
    phoneNumber = this.phoneNumber
)

fun BillingReferral.toDto() = BillingReferralDto(
    billingType = this.billingType,
    referredBy = this.referredBy
)

fun PatientInsurance.toDto() = PatientInsuranceDto(
    insuranceProvider = this.insuranceProvider,
    policyNumber = this.policyNumber,
    policyStartDate = this.policyStartDate,
    policyEndDate = this.policyEndDate,
    coverageAmount = this.coverageAmount
)

fun PatientAbha.toDto() = AbhaDto(
    abhaNumber = this.abhaNumber,
    abhaAddress = this.abhaAddress
)

fun InformationSharing.toDto() = InformationSharingDto(
    shareWithSpouse = this.shareWithSpouse,
    shareWithChildren = this.shareWithChildren,
    shareWithCaregiver = this.shareWithCaregiver,
    shareWithOther = this.shareWithOther
)

fun Referral.toDto() = ReferralDto(
    fromFacilityId = this.fromFacilityId,
    toFacilityId = this.toFacilityId,
    referralDate = this.referralDate,
    reason = this.reason
)

fun PatientRelationship.toDto() = PatientRelationshipDto(
    relativeId = this.relativeId,
    relationshipType = this.relationshipType
)

fun PatientToken.toDto() = TokenDto(
    tokenNumber = this.tokenNumber,
    issueDate = this.issueDate?.toLocalDate(),
    expiryDate = this.expiryDate?.toLocalDate(),
    status = this.status,
    isRegistered = this.isRegistered,
    allocatedTo = this.allocatedTo
)