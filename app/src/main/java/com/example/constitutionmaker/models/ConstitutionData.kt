package com.example.constitutionmaker.models

data class ConstitutionData(
    // General Info
    var countryName: String = "",
    var creatorName: String = "",
    var registerNumber: String = "",

    // Chapter 1: General Principles
    var formOfGovernment: String = "",
    var sourceOfAuthority: String = "",
    var secular: Boolean = false,
    var officialReligion: Boolean = false,
    var social: Boolean = false,
    var constitutionSupreme: Boolean = false,

    // Chapter 2: Fundamental Rights
    var nonNullableRights: List<String> = emptyList(),
    var suspendableFreedoms: List<String> = emptyList(),
    var suspensionAuthority: String = "",
    var includeFundamentalDuties: Boolean = false,
    var fundamentalDuties: List<String> = emptyList(),

    // Chapter 3: National Symbols
    var flagDetermination: String = "",
    var capitalDetermination: String = "",
    var capitalName: String = "",

    // Chapter 4: Directive Principles
    var directivePrinciples: List<String> = emptyList(),

    // Chapter 5: Structure of State
    var legislatureStructure: String = "",
    var electionSystem: String = "",
    var legislativePowers: List<String> = emptyList(),
    var presidentSelection: String = "",
    var presidentTerms: String = "",
    var primeMinisterAppointment: String = "",
    var judgeAppointment: String = "",
    var judicialProtections: List<String> = emptyList(),
    var judicialReviewScope: String = "",

    // Chapter 6: Military
    var supremeCommander: String = "",
    var militaryRestrictions: List<String> = emptyList(),

    // Chapter 7: Economy
    var economicPrinciple: String = "",
    var economicPolicies: List<String> = emptyList(),

    // Chapter 8: Education
    var educationalProvisions: List<String> = emptyList(),
    var educationBudget: String = "",

    // Chapter 9: Minorities
    var minorityProtections: List<String> = emptyList(),

    // Chapter 10: Amendment
    var amendmentProcedure: String = "",
    var interpretationAuthority: String = ""
)