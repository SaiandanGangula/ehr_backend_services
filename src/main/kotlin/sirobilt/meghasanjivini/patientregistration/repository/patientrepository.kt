package sirobilt.meghasanjivini.patientregistration.repository

import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepositoryBase
import jakarta.enterprise.context.ApplicationScoped
import java.util.*
import sirobilt.meghasanjivini.patientregistration.model.*
import java.time.LocalDate

@ApplicationScoped class PatientRepository            : PanacheRepositoryBase<Patient, UUID>{
    fun search(
        id: UUID?, fn: String?, ln: String?,
        mobile: String?, mail: String?,
        from: LocalDate?, to: LocalDate?
    ) = find(
        """
    SELECT DISTINCT p
      FROM Patient p
      LEFT JOIN PatientContact c ON c.patient = p
     WHERE (:id   IS NULL OR p.id = :id)
       AND (:fn   IS NULL OR p.firstName ILIKE :fn)
       AND (:ln   IS NULL OR p.lastName  ILIKE :ln)
       AND (:mob  IS NULL OR c.phoneNumber LIKE :mob)
       AND (:mail IS NULL OR c.email      ILIKE :mail)
       AND (:from IS NULL OR p.dateOfBirth >= :from)
       AND (:to   IS NULL OR p.dateOfBirth <= :to)
    """.trimIndent(),
        mapOf(
            "id"   to id,
            "fn"   to fn   ?.let { "%$it%" },
            "ln"   to ln   ?.let { "%$it%" },
            "mob"  to mobile?.let { "%$it%" },
            "mail" to mail ?.let { "%$it%" },
            "from" to from,
            "to"   to to
        )
    ).list()


    fun searchByCityOrName(city: String?, name: String?) = find(
        """
    SELECT DISTINCT p
      FROM Patient p
      JOIN PatientAddress a ON a.patient = p
     WHERE (:city IS NULL OR LOWER(a.cityOrVillage) LIKE :city)
       AND (:name IS NULL OR LOWER(p.firstName)      LIKE :name)
    """.trimIndent(),
        mapOf(
            "city" to city?.lowercase()?.let { "%$it%" },
            "name" to name?.lowercase()?.let { "%$it%" }
        )
    ).list()

    fun findDuplicate(first: String,
                      abhaNumber: String?,
                      email: String?): Patient? = find(
        /*language=JPQL*/
        """
        SELECT DISTINCT p
          FROM Patient p
          LEFT JOIN PatientContact c ON c.patient = p
         WHERE lower(p.firstName) = lower(:fn)
           AND ( (:abha IS NOT NULL AND p.identifierType = 'ABHA'
                                 AND p.identifierNumber = :abha)
                 OR
                 (:mail IS NOT NULL AND lower(c.email) = lower(:mail)) )
        """.trimIndent(),
        mapOf("fn" to first,
            "abha" to abhaNumber,
            "mail" to email)
    ).firstResult()


}
@ApplicationScoped class PatientContactRepository     : PanacheRepositoryBase<PatientContact, Long>
@ApplicationScoped class PatientAddressRepository     : PanacheRepositoryBase<PatientAddress, Long>
@ApplicationScoped class PatientAbhaRepository        : PanacheRepositoryBase<PatientAbha, Long>
@ApplicationScoped class BillingReferralRepository    : PanacheRepositoryBase<BillingReferral, Long>
@ApplicationScoped class EmergencyContactRepository   : PanacheRepositoryBase<EmergencyContact, Long>
@ApplicationScoped class InformationSharingRepository : PanacheRepositoryBase<InformationSharing, Long>
@ApplicationScoped class PatientInsuranceRepository  : PanacheRepositoryBase<PatientInsurance, Long>
@ApplicationScoped class ReferralRepository           : PanacheRepositoryBase<Referral, Long>
@ApplicationScoped class PatientRelationshipRepository: PanacheRepositoryBase<PatientRelationship, Long>
@ApplicationScoped class PatientTokenRepository       : PanacheRepositoryBase<PatientToken, Long>

@ApplicationScoped
class FieldConfigRepository : PanacheRepositoryBase<FieldConfig, Long> {
    fun listVisibleOrdered() = find("visible = true ORDER BY sortOrder").list()
}

@ApplicationScoped
class FieldOptionRepository : PanacheRepositoryBase<FieldOption, Long> {
    fun listByFieldNameOrdered(name: String) =
        find("fieldName = ?1 ORDER BY sortOrder", name).list()
}

