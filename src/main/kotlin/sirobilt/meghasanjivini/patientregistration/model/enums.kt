package sirobilt.meghasanjivini.patientregistration.model

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

enum class Title { Mr, Mrs, Ms, Dr, Master, Miss, Other }
enum class IdentifierType { ABHA, Aadhar, Passport, Driving_License, PAN }
enum class Gender { Male, Female, Other }
enum class BloodGroup(@get:JsonValue val symbol: String) {
    A_PLUS  ("A+"),
    A_MINUS ("A-"),
    B_PLUS  ("B+"),
    B_MINUS ("B-"),
    AB_PLUS ("AB+"),
    AB_MINUS("AB-"),
    O_PLUS  ("O+"),
    O_MINUS ("O-");

    companion object {
        @JvmStatic
        @JsonCreator
        fun from(value: String?): BloodGroup? =
            if (value == null) null
            else entries.firstOrNull { bg ->
                bg.symbol.equals(value, true) ||      // "B+"
                        bg.name.equals(value,  true) ||       // "B_PLUS"
                        bg.name.replace("_", "").equals(value.replace("+","PLUS").replace("-","MINUS"), true)
            }
    }
}enum class MaritalStatus { Single, Married, Divorced, Widowed }

enum class ContactMode { Phone, Email, None }
enum class PhonePref { Call, SMS, WhatsApp }
enum class AddressType { Present, Permanent }

enum class BillingType { General, Insurance, Corporate, Cash }
enum class RelationType { Spouse, Parent, Child, Sibling, Guardian, Other }
enum class TokenStatus { Active, Expired, Cancelled }

enum class FieldType { TEXT, DATE, SELECT, NUMBER, BOOLEAN }
