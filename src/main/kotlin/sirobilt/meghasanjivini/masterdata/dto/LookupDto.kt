package sirobilt.meghasanjivini.masterdata.dto

import java.util.UUID

class LookupValueCreateDTO{
    var category: String = ""
    var code: String = ""
    var displayName: String = ""
    var sortOrder: Int? = 0

    constructor()

    constructor(category: String,code: String, displayName: String, sortOrder: Int) {

        this.category = category
        this.code = code
        this.displayName = displayName
        this.sortOrder = sortOrder

    }

}

class LookupDto {
    var code: String = ""
    var displayName: String = ""
    var sortOrder: Int = 0

    constructor()

    constructor(code: String, displayName: String, sortOrder: Int) {
        this.code = code
        this.displayName = displayName
        this.sortOrder = sortOrder
    }
}


class LookupValueUpdateDTO {
    var code: String = ""
    var displayName: String = ""
    var sortOrder: Int = 0
    var active: Boolean = true

    constructor()

    constructor(code: String, displayName: String, sortOrder: Int, active: Boolean) {
        this.code = code
        this.displayName = displayName
        this.sortOrder = sortOrder
        this.active = active
    }
}

class CountryDto {
    var id: UUID? = null
    var name: String = ""

    constructor()

    constructor(id: UUID, name: String) {
        this.id = id
        this.name = name
    }
}

class StateDto {
    var id: UUID? = null
    var name: String = ""

    constructor()

    constructor(id: UUID, name: String) {
        this.id = id
        this.name = name
    }
}

class DistrictDto {
    var id: UUID? = null
    var name: String = ""

    constructor()

    constructor(id: UUID, name: String) {
        this.id = id
        this.name = name
    }
}
