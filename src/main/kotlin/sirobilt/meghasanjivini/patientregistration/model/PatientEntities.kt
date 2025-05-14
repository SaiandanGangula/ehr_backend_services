package sirobilt.meghasanjivini.patientregistration.model

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDate
import java.time.OffsetDateTime
import java.util.UUID

/* ------------------------------------------------------------------ */
/* 1. Patient (defaults added so a no-arg ctor exists)                */
/* ------------------------------------------------------------------ */
@Entity @Table(name = "patients")
data class Patient(
    @Id @Column(name = "patient_id")
    var id: UUID = UUID.randomUUID(),

    var facilityId: UUID = UUID.randomUUID(),
    @Enumerated(EnumType.STRING) var identifierType: IdentifierType = IdentifierType.ABHA,
    var identifierNumber: String = "",

    @Enumerated(EnumType.STRING) var title: Title? = null,
    var firstName: String? = null,
    var middleName: String? = null,
    var lastName: String? = null,
    var dateOfBirth: LocalDate? = null,
    var age: Int? = null,
    @Enumerated(EnumType.STRING) var gender: Gender? = null,
    @Enumerated(EnumType.STRING) var bloodGroup: BloodGroup? = null,
    @Enumerated(EnumType.STRING) var maritalStatus: MaritalStatus? = null,
    var citizenship: String? = null,
    var religion: String? = null,
    var caste: String? = null,
    var occupation: String? = null,
    var education: String? = null,
    var annualIncome: BigDecimal? = null,
    var registrationDate: OffsetDateTime = OffsetDateTime.now(),
    var isActive: Boolean = true,
    var isDeceased: Boolean = false,
    @OneToMany(mappedBy = "patient",
cascade = [CascadeType.ALL],
orphanRemoval = true,
fetch = FetchType.LAZY)
var contacts: MutableList<PatientContact> = mutableListOf()
)

/* ------------------------------------------------------------------ */
/* 2. PatientContact                                                  */
/* ------------------------------------------------------------------ */
@Entity @Table(name = "patient_contacts")
data class PatientContact(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var contactId: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "patient_id")
    var patient: Patient = Patient(),
    var mobileNumber: String = "",
    var phoneNumber: String = "",
    var email: String? = null,
    @Enumerated(EnumType.STRING) var preferredContactMode: ContactMode? = null,
    @Enumerated(EnumType.STRING) var phoneContactPreference: PhonePref? = null,
    var consentToShare: Boolean = false
)

/* ------------------------------------------------------------------ */
/* 3. EmergencyContact                                                */
/* ------------------------------------------------------------------ */
@Entity
@Table(name = "emergency_contacts")
data class EmergencyContact(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var emergencyContactId: Long   = 0,

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "patient_id")
    var patient: Patient           = Patient(),

    var contactName: String?       = null,
    var relationship: String?      = null,
    var phoneNumber: String?       = null
)

/* ------------------------------------------------------------------ */
/* 4. Dynamic-form metadata                                           */
/* ------------------------------------------------------------------ */
@Entity
@Table(name = "registration_field_config")
data class FieldConfig(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var configId: Long?            = null,

    var fieldName: String          = "",
    var label: String              = "",
    @Enumerated(EnumType.STRING)
    var fieldType: FieldType       = FieldType.TEXT,
    var required: Boolean          = false,
    var visible: Boolean           = true,
    var sortOrder: Int             = 0
)

@Entity
@Table(name = "registration_field_option")
data class FieldOption(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var optionId: Long?            = null,

    var fieldName: String?         = null,
    var value: String?             = null,
    var display: String?           = null,
    var sortOrder: Int?             = 0
)

@Entity @Table(name = "patient_addresses")
data class PatientAddress(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var addressId: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "patient_id")
    var patient: Patient = Patient(),

    @Enumerated(EnumType.STRING) var addressType: AddressType = AddressType.Present,
    var houseNoOrFlatNo: String? = null,
    var localityOrSector: String? = null,
    var cityOrVillage: String? = null,
    var pincode: String? = null,
    var districtId: String? = null,
    var stateId: String? = null,
    var country: String = "India"
)

@Entity @Table(name = "patient_abha")
data class PatientAbha(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var abhaId: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "patient_id")
    var patient: Patient = Patient(),

    var abhaNumber: String? = null,
    var abhaAddress: String? = null
)

@Entity @Table(name = "billing_referral")
data class BillingReferral(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var billingId: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "patient_id")
    var patient: Patient = Patient(),

    @Enumerated(EnumType.STRING) var billingType: BillingType = BillingType.General,
    var referredBy: String? = null
)



/* 14 ────────────────────────────────────────────────────────── */
@Entity @Table(name = "information_sharing")
data class InformationSharing(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var shareId: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "patient_id")
    var patient: Patient = Patient(),

    var shareWithSpouse: Boolean = false,
    var shareWithChildren: Boolean = false,
    var shareWithCaregiver: Boolean = false,
    var shareWithOther: Boolean = false
)

/* 15 ────────────────────────────────────────────────────────── */
@Entity @Table(name = "patient_insurance")
data class PatientInsurance(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var insuranceId: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "patient_id")
    var patient: Patient = Patient(),

    var insuranceProvider: String = "",
    var policyNumber: String = "",
    var policyStartDate: LocalDate = LocalDate.now(),
    var policyEndDate: LocalDate = LocalDate.now(),
    var coverageAmount: BigDecimal = BigDecimal.ZERO
)

/* 16 ────────────────────────────────────────────────────────── */
@Entity @Table(name = "referrals")
data class Referral(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var referralId: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "patient_id")
    var patient: Patient = Patient(),

    var fromFacilityId: UUID = UUID.randomUUID(),
    var toFacilityId: UUID = UUID.randomUUID(),
    var referralDate: LocalDate = LocalDate.now(),
    var reason: String? = null
)

/* 17 ────────────────────────────────────────────────────────── */
@Entity @Table(name = "patient_relationships")
data class PatientRelationship(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var relationshipId: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "patient_id")
    var patient: Patient = Patient(),

    var relativeId: UUID = UUID.randomUUID(),
    @Enumerated(EnumType.STRING) var relationshipType: RelationType = RelationType.Other
)

/* 18 ────────────────────────────────────────────────────────── */
@Entity @Table(name = "patient_tokens")
data class PatientToken(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var tokenId: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "patient_id")
    var patient: Patient = Patient(),

    var tokenNumber: String = "",
    var issueDate: OffsetDateTime = OffsetDateTime.now(),
    var expiryDate: OffsetDateTime = OffsetDateTime.now().plusDays(1),
    @Enumerated(EnumType.STRING) var status: TokenStatus = TokenStatus.Active,
    var isRegistered: Boolean = false,
    var allocatedTo: String = ""
)

/* ─── Dynamic-form metadata (unchanged) ────────────────────── */

