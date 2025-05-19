package sirobilt.meghasanjivini.patientregistration.model

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDate
import java.time.OffsetDateTime
import java.util.UUID

@Entity
@Table(name = "patients")
class Patient(
    @Id @Column(name = "patient_id")
    var id: UUID = UUID.randomUUID(),

    var facilityId: UUID = UUID.randomUUID(),

    @Enumerated(EnumType.STRING)
    var identifierType: IdentifierType = IdentifierType.ABHA,
    var identifierNumber: String = "",

    @Enumerated(EnumType.STRING)
    var title: Title? = null,
    var firstName: String? = null,
    var middleName: String? = null,
    var lastName: String? = null,
    var dateOfBirth: LocalDate? = null,
    var age: Int? = null,
    @Enumerated(EnumType.STRING)
    var gender: Gender? = null,
    @Enumerated(EnumType.STRING)
    var bloodGroup: BloodGroup? = null,
    @Enumerated(EnumType.STRING)
    var maritalStatus: MaritalStatus? = null,
    var citizenship: String? = null,
    var religion: String? = null,
    var caste: String? = null,
    var occupation: String? = null,
    var education: String? = null,
    var annualIncome: String? = null,
    var registrationDate: OffsetDateTime = OffsetDateTime.now(),
    var isActive: Boolean = true,
    var isDeceased: Boolean = false,

    @OneToMany(mappedBy = "patient", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    var contacts: MutableList<PatientContact> = mutableListOf(),

    @OneToMany(mappedBy = "patient", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    var addresses: MutableList<PatientAddress> = mutableListOf(),

    @OneToMany(mappedBy = "patient", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    var emergencyContacts: MutableList<EmergencyContact> = mutableListOf(),

    @OneToOne(mappedBy = "patient", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    var billingReferral: BillingReferral? = null,

    @OneToOne(mappedBy = "patient", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    var insurance: PatientInsurance? = null,

    @OneToOne(mappedBy = "patient", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    var abha: PatientAbha? = null,

    @OneToOne(mappedBy = "patient", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    var informationSharing: InformationSharing? = null,

    @OneToMany(mappedBy = "patient", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    var referrals: MutableList<Referral> = mutableListOf(),

    @OneToMany(mappedBy = "patient", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    var relationships: MutableList<PatientRelationship> = mutableListOf(),

    @OneToMany(mappedBy = "patient", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    var tokens: MutableList<PatientToken> = mutableListOf()
) {
    override fun toString(): String {
        return "Patient(id=$id, name=$firstName $lastName)"
    }
}

@Entity
@Table(name = "patient_contacts")
class PatientContact(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var contactId: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    @JsonBackReference
    var patient: Patient = Patient(),

    var mobileNumber: String? = "",
    var phoneNumber: String = "",
    var email: String? = null,
    @Enumerated(EnumType.STRING)
    var preferredContactMode: ContactMode? = null,
    @Enumerated(EnumType.STRING)
    var phoneContactPreference: PhonePref? = null,
    var consentToShare: Boolean = false
) {
    override fun toString(): String {
        return "PatientContact(contactId=$contactId, mobileNumber=$mobileNumber)"
    }
}

@Entity
@Table(name = "emergency_contacts")
class EmergencyContact(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var emergencyContactId: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    @JsonBackReference
    var patient: Patient = Patient(),

    var contactName: String? = null,
    var relationship: String? = null,
    var phoneNumber: String? = null
) {
    override fun toString(): String {
        return "EmergencyContact(id=$emergencyContactId, name=$contactName)"
    }
}

@Entity
@Table(name = "patient_addresses")
class PatientAddress(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var addressId: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    @JsonBackReference
    var patient: Patient = Patient(),

    @Enumerated(EnumType.STRING)
    var addressType: AddressType = AddressType.Present,
    var houseNoOrFlatNo: String? = null,
    var localityOrSector: String? = null,
    var cityOrVillage: String? = null,
    var pincode: String? = null,
    var districtId: String? = null,
    var stateId: String? = null,
    var country: String = "India"
) {
    override fun toString(): String {
        return "PatientAddress(id=$addressId, city=$cityOrVillage)"
    }
}

@Entity
@Table(name = "patient_abha")
class PatientAbha(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var abhaId: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    @JsonBackReference
    var patient: Patient = Patient(),

    var abhaNumber: String? = null,
    var abhaAddress: String? = null
) {
    override fun toString(): String {
        return "PatientAbha(id=$abhaId, number=$abhaNumber)"
    }
}

@Entity
@Table(name = "billing_referral")
class BillingReferral(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var billingId: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    @JsonBackReference
    var patient: Patient = Patient(),

    @Enumerated(EnumType.STRING)
    var billingType: BillingType = BillingType.General,
    var referredBy: String? = null
) {
    override fun toString(): String {
        return "BillingReferral(id=$billingId, type=$billingType)"
    }
}

@Entity
@Table(name = "information_sharing")
class InformationSharing(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var shareId: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    @JsonBackReference
    var patient: Patient = Patient(),

    var shareWithSpouse: Boolean = false,
    var shareWithChildren: Boolean = false,
    var shareWithCaregiver: Boolean = false,
    var shareWithOther: Boolean = false
) {
    override fun toString(): String {
        return "InformationSharing(id=$shareId)"
    }
}

@Entity
@Table(name = "patient_insurance")
class PatientInsurance(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var insuranceId: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    @JsonBackReference
    var patient: Patient = Patient(),

    var insuranceProvider: String = "",
    var policyNumber: String = "",
    var policyStartDate: LocalDate = LocalDate.now(),
    var policyEndDate: LocalDate = LocalDate.now(),
    var coverageAmount: BigDecimal = BigDecimal.ZERO
) {
    override fun toString(): String {
        return "PatientInsurance(id=$insuranceId, provider=$insuranceProvider)"
    }
}

@Entity
@Table(name = "referrals")
class Referral(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var referralId: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    @JsonBackReference
    var patient: Patient = Patient(),

    var fromFacilityId: UUID = UUID.randomUUID(),
    var toFacilityId: UUID = UUID.randomUUID(),
    var referralDate: LocalDate = LocalDate.now(),
    var reason: String? = null
) {
    override fun toString(): String {
        return "Referral(id=$referralId, from=$fromFacilityId, to=$toFacilityId)"
    }
}

@Entity
@Table(name = "patient_relationships")
class PatientRelationship(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var relationshipId: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    @JsonBackReference
    var patient: Patient = Patient(),

    var relativeId: UUID = UUID.randomUUID(),
    @Enumerated(EnumType.STRING)
    var relationshipType: RelationType = RelationType.Other
) {
    override fun toString(): String {
        return "PatientRelationship(id=$relationshipId, type=$relationshipType)"
    }
}

@Entity
@Table(name = "patient_tokens")
class PatientToken(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var tokenId: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    @JsonBackReference
    var patient: Patient = Patient(),

    var tokenNumber: String = "",
    var issueDate: OffsetDateTime = OffsetDateTime.now(),
    var expiryDate: OffsetDateTime = OffsetDateTime.now().plusDays(1),
    @Enumerated(EnumType.STRING)
    var status: TokenStatus = TokenStatus.Active,
    var isRegistered: Boolean = false,
    var allocatedTo: String = ""
) {
    override fun toString(): String {
        return "PatientToken(id=$tokenId, number=$tokenNumber)"
    }
}

@Entity
@Table(name = "token_sequence")
class TokenSequence(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var sequenceId: Long? = null,

    @Column(name = "date_of_issue", unique = true)
    var dateOfIssue: LocalDate = LocalDate.now(),

    @Column(name = "last_token_number")
    var lastTokenNumber: Int = 0
) {
    override fun toString(): String {
        return "TokenSequence(id=$sequenceId, date=$dateOfIssue, lastNumber=$lastTokenNumber)"
    }
}