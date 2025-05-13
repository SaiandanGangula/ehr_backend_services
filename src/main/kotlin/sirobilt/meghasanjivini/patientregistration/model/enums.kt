package sirobilt.meghasanjivini.patientregistration.model

enum class Title { Mr, Mrs, Ms, Dr, Master, Miss, Other }
enum class IdentifierType { ABHA, Aadhar, Passport, Driving_License, PAN }
enum class Gender { Male, Female, Other }
enum class BloodGroup { A_PLUS, A_MINUS, B_PLUS, B_MINUS, AB_PLUS, AB_MINUS, O_PLUS, O_MINUS }
enum class MaritalStatus { Single, Married, Divorced, Widowed }

enum class ContactMode { Phone, Email, None }
enum class PhonePref { Call, SMS, WhatsApp }
enum class AddressType { Present, Permanent }

enum class BillingType { General, Insurance, Corporate, Cash }
enum class RelationType { Spouse, Parent, Child, Sibling, Guardian, Other }
enum class TokenStatus { Active, Expired, Cancelled }

enum class FieldType { TEXT, DATE, SELECT, NUMBER, BOOLEAN }
