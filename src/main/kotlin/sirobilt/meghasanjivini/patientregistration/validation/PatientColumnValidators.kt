package sirobilt.meghasanjivini.patientregistration.validation

import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import sirobilt.meghasanjivini.patientregistration.dto.PatientInsuranceDto
import java.time.LocalDate
import kotlin.reflect.KClass

/* IndianMobile.kt (+91XXXXXXXXXX) */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [IndianMobileValidator::class])
annotation class IndianMobile(
    val message: String = "must start with “+91” and contain exactly 10 digits",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)

class IndianMobileValidator : ConstraintValidator<IndianMobile, String> {
    private val re = Regex("^\\+91[1-9]\\d{9}\$")
    override fun isValid(v: String?, c: ConstraintValidatorContext) =
        v == null || re.matches(v)
}

/* AbhaNumber.kt → 99-9999-9999-9999 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [AbhaNumberValidator::class])
annotation class AbhaNumber(
    val message: String = "invalid ABHA format (##-####-####-####)",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)

class AbhaNumberValidator : ConstraintValidator<AbhaNumber, String> {
    private val re = Regex("^\\d{2}-\\d{4}-\\d{4}-\\d{4}\$")
    override fun isValid(v: String?, c: ConstraintValidatorContext) =
        v == null || re.matches(v)
}

/* PostalCode.kt – 6-digit Indian PIN */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [PostalCodeValidator::class])
annotation class PostalCode(
    val message: String = "postal code must be 6 digits",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)

class PostalCodeValidator : ConstraintValidator<PostalCode, String> {
    private val re = Regex("^\\d{6}\$")
    override fun isValid(v: String?, c: ConstraintValidatorContext) =
        v == null || re.matches(v)
}

/* PastDate.kt – dateOfBirth must be before today */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [PastDateValidator::class])
annotation class PastDate(
    val message: String = "date must be in the past",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)

class PastDateValidator : ConstraintValidator<PastDate, LocalDate> {
    override fun isValid(d: LocalDate?, ctx: ConstraintValidatorContext) =
        d == null || d.isBefore(LocalDate.now())
}

/* InsuranceDateRange.kt – end ≥ start */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [InsuranceDateRangeValidator::class])
annotation class InsuranceDateRange(
    val message: String = "policy_end_date must be on/after policy_start_date",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)

class InsuranceDateRangeValidator
    : ConstraintValidator<InsuranceDateRange, PatientInsuranceDto> {

    override fun isValid(dto: PatientInsuranceDto, ctx: ConstraintValidatorContext): Boolean {
        return !dto.policyEndDate.isBefore(dto.policyStartDate)
    }
}

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [AadhaarValidator::class])
annotation class Aadhaar(
    val message: String = "invalid Aadhaar format (12 digits)",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)

class AadhaarValidator : ConstraintValidator<Aadhaar, String> {
    private val re = Regex("^\\d{4}\\s?\\d{4}\\s?\\d{4}\$")

    override fun isValid(v: String?, ctx: ConstraintValidatorContext): Boolean =
        v == null || re.matches(v)
}

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [PANValidator::class])
annotation class PAN(
    val message: String = "invalid PAN format (AAAAA9999A)",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)

class PANValidator : ConstraintValidator<PAN, String> {
    private val re = Regex("^[A-Z]{5}[0-9]{4}[A-Z]\$")

    override fun isValid(v: String?, ctx: ConstraintValidatorContext): Boolean =
        v == null || re.matches(v)
}
