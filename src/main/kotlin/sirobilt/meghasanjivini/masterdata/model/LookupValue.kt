package sirobilt.meghasanjivini.masterdata.model

import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntityBase
import jakarta.persistence.*
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "lookup_values")
class LookupValue(
    @Id
    var id: UUID = UUID.randomUUID(),
    var category: String = "",
    var code: String = "",
    var displayName: String = "",
    var sortOrder: Int = 0,
    var active: Boolean = true,
    var createdAt: Instant = Instant.now(),
    var updatedAt: Instant = Instant.now()
) : PanacheEntityBase

@Entity
@Table(name = "countries")
class Country(
    @Id
    var id: UUID = UUID.randomUUID(),
    var name: String = ""
) : PanacheEntityBase

@Entity
@Table(name = "states")
class State(
    @Id
    var id: UUID = UUID.randomUUID(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id")
    var country: Country = Country(),

    var name: String = ""
) : PanacheEntityBase

@Entity
@Table(name = "districts")
class District(
    @Id
    var id: UUID = UUID.randomUUID(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "state_id")
    var state: State = State(),

    var name: String = ""
) : PanacheEntityBase
