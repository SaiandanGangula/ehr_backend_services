package sirobilt.meghasanjivini.patientregistration.dto

import jakarta.validation.constraints.Email
import sirobilt.meghasanjivini.patientregistration.model.AddressType
import sirobilt.meghasanjivini.patientregistration.model.BillingType
import sirobilt.meghasanjivini.patientregistration.model.BloodGroup
import sirobilt.meghasanjivini.patientregistration.model.ContactMode
import sirobilt.meghasanjivini.patientregistration.model.Gender
import sirobilt.meghasanjivini.patientregistration.model.IdentifierType
import sirobilt.meghasanjivini.patientregistration.model.MaritalStatus
import sirobilt.meghasanjivini.patientregistration.model.PhonePref
import sirobilt.meghasanjivini.patientregistration.model.RelationType
import sirobilt.meghasanjivini.patientregistration.model.Title
import sirobilt.meghasanjivini.patientregistration.model.TokenStatus
import sirobilt.meghasanjivini.patientregistration.validation.AbhaNumber
import sirobilt.meghasanjivini.patientregistration.validation.IndianMobile
import sirobilt.meghasanjivini.patientregistration.validation.PastDate
import sirobilt.meghasanjivini.patientregistration.validation.PostalCode
import java.math.BigDecimal
import java.time.LocalDate
import java.time.OffsetDateTime
import java.util.UUID

/* ──────────────────────────────────────────────────────────────── *
 *  1. Patient registration (REQUEST)                               *
 * ──────────────────────────────────────────────────────────────── */
data class PatientRegistrationDto(

    // --- mandatory identifying fields ---
    val facilityId: UUID,
    val identifierType: IdentifierType,
    val identifierNumber: String,

    // --- patient.basic columns (all nullable so validator can enforce) ---
    val title: Title?            = null,
    val firstName: String?       = null,
    val middleName: String?      = null,
    val lastName: String?        = null,

    @field:PastDate
    val dateOfBirth: LocalDate?  = null,
    val age: Int?                = null,
    val gender: Gender?          = null,
    val bloodGroup: BloodGroup?  = null,
    val maritalStatus: MaritalStatus? = null,
    val citizenship: String?     = null,
    val religion: String?        = null,
    val caste: String?           = null,
    val occupation: String?      = null,
    val education: String?       = null,
    val annualIncome: BigDecimal?= null,

    val dynamic:           Map<String,Any?>? = null,

    // --- child collections / aggregates --------------------------
    val contacts:           List<ContactDto>?            = null,
    val addresses:          List<AddressDto>?            = null,
    val abha:               AbhaDto?                     = null,
    val billingReferral:    BillingReferralDto?          = null,
    val emergencyContacts:  List<EmergencyContactDto>?   = null,
    val informationSharing: InformationSharingDto?       = null,
    val insurance:          PatientInsuranceDto?         = null,
    val referrals:          List<ReferralDto>?           = null,
    val relationships:      List<PatientRelationshipDto>?= null,
    val tokens:             List<TokenDto>?              = null
)
data class UpdatePatientDto(

    // --- mandatory identifying fields ---
    val facilityId: UUID,
    val identifierType: IdentifierType,
    val identifierNumber: String,

    // --- patient.basic columns (all nullable so validator can enforce) ---
    val title: Title?            = null,
    val firstName: String?       = null,
    val middleName: String?      = null,
    val lastName: String?        = null,
    val dateOfBirth: LocalDate?  = null,
    val age: Int?                = null,
    val gender: Gender?          = null,
    val bloodGroup: BloodGroup?  = null,
    val maritalStatus: MaritalStatus? = null,
    val citizenship: String?     = null,
    val religion: String?        = null,
    val caste: String?           = null,
    val occupation: String?      = null,
    val education: String?       = null,
    val annualIncome: BigDecimal?= null,

    // --- child collections / aggregates --------------------------
    val contacts:           List<ContactDto>?            = null,
    val addresses:          List<AddressDto>?            = null,
    val abha:               AbhaDto?                     = null,
    val billingReferral:    BillingReferralDto?          = null,
    val emergencyContacts:  List<EmergencyContactDto>?   = null,
    val informationSharing: InformationSharingDto?       = null,
    val insurance:          PatientInsuranceDto?         = null,
    val referrals:          List<ReferralDto>?           = null,
    val relationships:      List<PatientRelationshipDto>?= null,
    val tokens:             List<TokenDto>?              = null
)

/* ──────────────────────────────────────────────────────────────── *
 *  2. Patient registration (RESPONSE – static + dynamic)          *
 * ──────────────────────────────────────────────────────────────── */
data class PatientResponseDto(
    val patientId: UUID,
    val facilityId: UUID,
    val identifierType: IdentifierType,
    val identifierNumber: String,
    val title: Title? = null,
    val firstName: String? = null,
    val middleName: String? = null,
    val lastName: String? = null,
    val fullName: String,
    val dateOfBirth: LocalDate? = null,
    val age: Int? = null,
    val gender: Gender? = null,
    val bloodGroup: BloodGroup? = null,
    val maritalStatus: MaritalStatus? = null,
    val citizenship: String? = null,
    val religion: String? = null,
    val caste: String? = null,
    val occupation: String? = null,
    val education: String? = null,
    val annualIncome: BigDecimal? = null,
    val registrationDate: OffsetDateTime,
    val isActive: Boolean,
    val isDeceased: Boolean,
    val phone: String? = null,
    val email: String? = null,

    // ✅ Correct child DTOs
    val contacts: List<ContactDto>? = null,
    val addresses: List<AddressDto>? = null,
    val emergencyContacts: List<EmergencyContactDto>? = null,
    val billingReferral: BillingReferralDto? = null,
    val insurance: PatientInsuranceDto? = null,
    val abha: AbhaDto? = null,
    val informationSharing: InformationSharingDto? = null,
    val referrals: List<ReferralDto>? = null,
    val relationships: List<PatientRelationshipDto>? = null,
    val tokens: List<TokenDto>? = null
)

/* ──────────────────────────────────────────────────────────────── *
 *  3. Child-table DTOs                                            *
 * ──────────────────────────────────────────────────────────────── */
data class ContactDto(


    @field:IndianMobile
    val mobileNumber: String?,
    @field:IndianMobile
    val phoneNumber: String,
    @field:Email
    val email: String? = null,
    val preferredContactMode: ContactMode? = null,
    val phoneContactPreference: PhonePref? = null,
    val consentToShare: Boolean = false
)

data class AddressDto(
    val addressType: AddressType,
    val houseNoOrFlatNo: String? = null,
    val localityOrSector: String? = null,
    val cityOrVillage: String? = null,

    @field:PostalCode
    val pincode: String? = null,
    val districtId: String? = null,
    val stateId: String? = null,
    val country: String = "India"
)

data class AbhaDto(

    @field:AbhaNumber
    val abhaNumber: String?,
    val abhaAddress: String?
)

data class BillingReferralDto(
    val billingType: BillingType,
    val referredBy: String?
)

data class EmergencyContactDto(
    val contactName: String?,
    val relationship: String?,
    val phoneNumber: String?
)

data class InformationSharingDto(
    val shareWithSpouse: Boolean = false,
    val shareWithChildren: Boolean = false,
    val shareWithCaregiver: Boolean = false,
    val shareWithOther: Boolean = false
)

data class PatientInsuranceDto(
    val insuranceProvider: String,
    val policyNumber: String,
    val policyStartDate: LocalDate,
    val policyEndDate: LocalDate,
    val coverageAmount: BigDecimal
)

data class ReferralDto(
    val fromFacilityId: UUID,
    val toFacilityId: UUID,
    val referralDate: LocalDate,
    val reason: String?
)

data class PatientRelationshipDto(
    val relativeId: UUID,
    val relationshipType: RelationType
)

data class TokenDto(
    val tokenNumber: String,
    val issueDate: LocalDate? = null,          // optional override
    val expiryDate: LocalDate? = null,
    val status: TokenStatus = TokenStatus.Active,
    val isRegistered: Boolean = false,
    val allocatedTo: String
)

