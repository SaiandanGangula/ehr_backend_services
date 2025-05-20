package sirobilt.meghasanjivini.masterdata.model


import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntityBase
import jakarta.persistence.*
import java.time.Instant
import java.util.*


@Entity
@Table(name = "facility")
class Facility : PanacheEntityBase {

    @Id
    @Column(name = "hospital_id")
    lateinit var hospitalId: String

    @Column(name = "facility_name")
    lateinit var facilityName: String

    @Column(name = "facility_type")
    lateinit var facilityType: String

    lateinit var block: String

    @Column(name = "phc_chc_name")
    lateinit var phcChcName: String

    @Column(name = "location")
    lateinit var location: String

    @Column(name = "officer_in_charge")
    lateinit var officerInCharge: String

    lateinit var designation: String

    @Column(name = "contact_number")
    lateinit var contactNumber: String

    @Column(name = "official_email")
    lateinit var officialEmail: String

    @Column(name = "network_id")
    lateinit var networkId: String

    @Column(name = "bed_strength")
    var bedStrength: Int = 0

    @Column(name = "patient_types")
    lateinit var patientTypes: String

    @Column(columnDefinition = "text")
    var notes: String? = null

    @Column(columnDefinition = "text")
    var equipments: String? = null

    @Column(name = "created_at")
    var createdAt: Instant = Instant.now()

    @Column(name = "updated_at")
    var updatedAt: Instant = Instant.now()
}