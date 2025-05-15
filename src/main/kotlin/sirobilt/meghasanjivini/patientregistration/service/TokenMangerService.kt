package sirobilt.meghasanjivini.patientregistration.service

import jakarta.inject.Inject
import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional
import org.jboss.logging.Logger
import sirobilt.meghasanjivini.patientregistration.controller.PatientController
import sirobilt.meghasanjivini.patientregistration.model.Patient
import sirobilt.meghasanjivini.patientregistration.model.PatientToken
import sirobilt.meghasanjivini.patientregistration.model.TokenSequence
import sirobilt.meghasanjivini.patientregistration.model.TokenStatus
import sirobilt.meghasanjivini.patientregistration.repository.PatientTokenRepository
import sirobilt.meghasanjivini.patientregistration.repository.TokenSequenceRepository
import java.time.LocalDate
import java.time.OffsetDateTime

@ApplicationScoped
class TokenManagerService @Inject constructor(
    private val tokenSequenceRepo: TokenSequenceRepository
) {



    @Transactional
    fun generateNewTokenForPatient(patient: Patient): PatientToken {
        val today = LocalDate.now()
        val sequence = tokenSequenceRepo.findByDate(today)

        val nextTokenNumber = if (sequence == null) {
            val newSeq = TokenSequence(dateOfIssue = today, lastTokenNumber = 1)
            tokenSequenceRepo.persist(newSeq)
            1
        } else {
            sequence.lastTokenNumber += 1
            sequence.lastTokenNumber
        }

        val formattedToken = String.format("TKN-%04d", nextTokenNumber)

        return PatientToken(
            patient = patient,
            tokenNumber = formattedToken,
            issueDate = OffsetDateTime.now(),
            expiryDate = OffsetDateTime.now().plusDays(1),
            status = TokenStatus.Active,
            isRegistered = true,
            allocatedTo = "Front Desk"
        )
    }
}

@ApplicationScoped
class TokenService @Inject constructor(
    private val tokenRepo: PatientTokenRepository
) {

    fun listTodayActiveTokens(): List<PatientToken> {
        val today = OffsetDateTime.now().toLocalDate()
        return tokenRepo.find("DATE(issueDate) = ?1 and status = ?2", today, TokenStatus.Active).list()
    }

    @Transactional
    fun expireTokens() {
        val now = OffsetDateTime.now()
        val tokensToExpire = tokenRepo.find("status = ?1 and expiryDate < ?2", TokenStatus.Active, now).list()

        tokensToExpire.forEach {
            it.status = TokenStatus.Expired
        }
    }

    @Transactional
    fun cancelToken(tokenNumber: String) {
        val token = tokenRepo.find("tokenNumber", tokenNumber).firstResult()
            ?: throw IllegalArgumentException("Token not found")
        token.status = TokenStatus.Cancelled
    }
}