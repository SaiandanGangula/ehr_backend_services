package sirobilt.meghasanjivini.patientregistration.service

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import jakarta.ws.rs.NotFoundException
import sirobilt.meghasanjivini.patientregistration.dto.*
import sirobilt.meghasanjivini.patientregistration.model.*
import sirobilt.meghasanjivini.patientregistration.repository.*
import sirobilt.meghasanjivini.patientregistration.validation.PatientValidator
import java.time.LocalDate
import java.util.*
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

/* ------------------------------------------------------------------ */
/* 1. Field-config (dynamic-form metadata) service                    */
/* ------------------------------------------------------------------ */
@ApplicationScoped
class FieldConfigService @Inject constructor(
    private val cfgRepo: FieldConfigRepository,
    private val optRepo: FieldOptionRepository
) {

    fun visible(): List<FieldConfigDto> =
        cfgRepo.listVisibleOrdered().map { cfg ->
            val opts = if (cfg.fieldType == FieldType.SELECT)
                optRepo.listByFieldNameOrdered(cfg.fieldName)
                    .map { FieldOptionDto(it.fieldName!!, it.value!!, it.display!!, it.sortOrder) }
            else emptyList()

            FieldConfigDto(
                name      = cfg.fieldName,
                label     = cfg.label,
                type      = cfg.fieldType,
                required  = cfg.required,
                options   = opts,
                sortOrder = cfg.sortOrder
            )
        }

    fun all(): List<FieldConfig> = cfgRepo.find("ORDER BY sortOrder").list()

    @Transactional
    fun upsert(dto: FieldConfigDto): FieldConfig =
        (cfgRepo.find("fieldName", dto.name).firstResult()
            ?.apply {
                label     = dto.label
                fieldType = dto.type
                required  = dto.required
                visible   = true
                sortOrder = dto.sortOrder ?: sortOrder
            }
            ?: cfgRepo.persist(
                FieldConfig(
                    fieldName = dto.name,
                    label     = dto.label,
                    fieldType = dto.type,
                    required  = dto.required,
                    visible   = true,
                    sortOrder = dto.sortOrder ?: 0
                )
            )) as FieldConfig
}

/* ------------------------------------------------------------------ */
/* 2. Patient aggregate service                                       */
/* ------------------------------------------------------------------ */
@ApplicationScoped
class PatientService @Inject constructor(
    private val patientRepo: PatientRepository,
    private val contactRepo: PatientContactRepository,
    private val addressRepo: PatientAddressRepository,
    private val emergencyRepo: EmergencyContactRepository,
    private val insuranceRepo: PatientInsuranceRepository,
    private val validator: PatientValidator,
    private val cfgSvc: FieldConfigService
) {

    /* ---------- CREATE ---------- */
    @Transactional
    fun register(dto: PatientRegistrationDto): PatientResponseDto {
        validator.validate(dto)

        val patient = dto.toEntity()
        patientRepo.persist(patient)

        dto.contacts          ?.map { it.toEntity(patient) }?.let { contactRepo.persist(it) }
        dto.addresses         ?.map { it.toEntity(patient) }?.let { addressRepo.persist(it) }
        dto.emergencyContacts ?.map { it.toEntity(patient) }?.let { emergencyRepo.persist(it) }
        dto.insurance         ?.toEntity(patient)          ?.let { insuranceRepo.persist(it) }

        return patient.toDto(cfgSvc)
    }

    @Transactional
    fun update(id: UUID, dto: UpdatePatientDto): PatientResponseDto {
        val p = patientRepo.findById(id) ?: throw NotFoundException()

        // simple property copy – ignore identifierType / identifierNumber entirely
        dto.firstName ?.let { p.firstName  = it }
        dto.middleName?.let { p.middleName = it }
        dto.lastName  ?.let { p.lastName   = it }
        dto.dateOfBirth?.let { p.dateOfBirth = it }
        dto.gender    ?.let { p.gender     = it }
        dto.title     ?.let { p.title      = it }
        dto.age       ?.let { p.age        = it }
        dto.bloodGroup?.let { p.bloodGroup = it }
        dto.maritalStatus?.let { p.maritalStatus = it }
        dto.occupation?.let { p.occupation = it }
        // … add any other mutable columns …

        return p.toDto(cfgSvc)
    }

    /* ---------- READ ---------- */
    fun listAll(): List<PatientResponseDto> =
        patientRepo.findAll().list().map { it.toDto(cfgSvc) }

    @Transactional
    fun delete(id: UUID) {
        if (!patientRepo.deleteById(id)) throw NotFoundException()
    }

    fun search(
        id: UUID?,
        first: String?, last: String?,
        mobile: String?, email: String?,
        dobFrom: LocalDate?, dobTo: LocalDate?
    ): List<PatientResponseDto> =
        patientRepo.search(id, first, last, mobile, email, dobFrom, dobTo)
            .map { it.toDto(cfgSvc) }

    fun searchByCityOrName(city: String?, name: String?): List<PatientResponseDto> =
        patientRepo.searchByCityOrName(city, name)
            .map { it.toDto(cfgSvc) }
}

/* ------------------------------------------------------------------ */
/* 3. Entity → Response-DTO helper                                    */
/* ------------------------------------------------------------------ */
private fun Patient.toDto(cfgSvc: FieldConfigService): PatientResponseDto {
    val visible = cfgSvc.visible().map { it.name }.toSet()

    val dynamic: Map<String, Any?> = this::class.memberProperties
        .filterIsInstance<KProperty1<Patient, *>>()
        .filter { it.name in visible }
        .associate { it.name to it.get(this) }
    val firstContact = contacts.firstOrNull()
    return PatientResponseDto(
        patientId        = id,
        fullName         = listOfNotNull(firstName, middleName, lastName).joinToString(" "),
        facilityId       = facilityId,
        identifierType   = identifierType,
        identifierNumber = identifierNumber,
        phone            = firstContact?.phoneNumber,
        email            = firstContact?.email,
        dynamic          = dynamic
    )
}
