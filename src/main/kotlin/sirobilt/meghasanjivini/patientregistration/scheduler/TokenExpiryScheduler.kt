package sirobilt.meghasanjivini.patientregistration.scheduler

import io.quarkus.scheduler.Scheduled
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import sirobilt.meghasanjivini.patientregistration.service.TokenService

@ApplicationScoped
class TokenExpiryScheduler @Inject constructor(
    private val tokenService: TokenService
) {

    @Scheduled(cron = "0 */5 * * * ?") // Every 5 minutes
    fun expireOldTokens() {
        tokenService.expireTokens()
    }
}