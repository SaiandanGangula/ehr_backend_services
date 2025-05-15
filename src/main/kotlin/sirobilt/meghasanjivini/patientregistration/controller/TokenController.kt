package sirobilt.meghasanjivini.patientregistration.controller

import jakarta.ws.rs.Consumes
import jakarta.ws.rs.GET
import jakarta.ws.rs.*
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import sirobilt.meghasanjivini.patientregistration.service.TokenService

@Path("/api/tokens")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
class TokenController(
    private val tokenService: TokenService
) {

    @GET
    @Path("/today")
    fun listTodayTokens(): Response {
        val tokens = tokenService.listTodayActiveTokens()
        return Response.ok(tokens).build()
    }

    @POST
    @Path("/cancel/{tokenNumber}")
    fun cancelToken(@PathParam("tokenNumber") tokenNumber: String): Response {
        tokenService.cancelToken(tokenNumber)
        return Response.noContent().build()
    }
}