package sirobilt.meghasanjivini.masterdata.dto

import java.util.UUID

data class LookupDto(val code: String, val displayName: String, val sortOrder: Int)

data class CountryDto(val id: UUID, val name: String)

data class StateDto(val id: UUID, val name: String)

data class DistrictDto(val id: UUID, val name: String)