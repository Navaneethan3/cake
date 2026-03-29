package com.example.constitutionmaker.models

data class ConstitutionData(
    // Question 0
    var countryName: String = "",
    var creatorName: String = "",
    var registerNumber: String = "",

    // CHAPTER ONE: GENERAL PRINCIPLES
    var formOfGovernment: String = "", // Q1.1
    var sourceOfAuthority: String = "", // Q1.2
    var treatyName: String = "", // Q1.2 E input
    var isSocial: Boolean = false, // Q1.3 a
    var statePrinciples: List<String> = emptyList(), // Q1.3 b (Nationalism, Independence, Livelihood, Fraternity, Equality)
    var isSecular: Boolean = true, // Default true
    var officialReligion: Boolean = false,
    var constitutionSupreme: Boolean = true,

    // CHAPTER TWO: FUNDAMENTAL RIGHTS AND DUTIES
    var rightsSource: String = "", // Q2.0: Natural or Constitutional
    
    // Path B: Constitutional (Nullable)
    var constNonNullableRights: List<String> = emptyList(), // Q2.1.1
    var constSuspendableRights: List<String> = emptyList(), // Q2.1.2
    var constVotingAge: String = "", // Q2.1.2 A input
    var constSuspendableFreedoms: List<String> = emptyList(), // Q2.2.1
    var suspensionAuthority: String = "", // Q2.3

    // Path A: Natural (Non-Nullable)
    var naturalRightsNonNullable: List<String> = emptyList(), // Q2.4.1 (at least 6)
    var naturalRightsOptional: List<String> = emptyList(), // Q2.4.1 optional
    var naturalVotingAge: String = "", // Q2.4.1 H input
    var naturalFreedoms: List<String> = emptyList(), // Q2.4.2

    // Part III: Fundamental Duties
    var includeDuties: Boolean = false, // Q2.5
    var fundamentalDuties: List<String> = emptyList(),

    // CHAPTER THREE
    var flagDetermination: String = "", // Q3.1
    var flagDescription: String = "", // Q3.1 A input
    var emblemDetermination: String = "", // Q3.2
    var emblemDescription: String = "", // Q3.2 A input
    var capitalDetermination: String = "", // Q3.3
    var capitalName: String = "", // Q3.3 A input

    // CHAPTER FOUR: DIRECTIVE PRINCIPLES
    var directivePrinciples: List<String> = emptyList(), // Q4.1
    var customDirectivePrinciples: List<String> = emptyList(), // Q4.1 add more

    // CHAPTER FIVE: STRUCTURE OF THE STATE
    // Section I: Legislature
    var legislatureStructure: String = "", // Q5.1 (Unicameral, Bicameral, Tricameral)
    var lowerHouseElection: String = "", // Q5.2
    var legislaturePowers: List<String> = emptyList(), // Q5.3
    var upperHouseElection: String = "", // Q5.4
    var upperHousePowers: List<String> = emptyList(), // Q5.5

    // Section II: Executive
    var presidentSelection: String = "", // Q5.6
    var presidentMaxTerms: String = "", // Q5.7
    var pmAppointment: String = "", // Q5.8
    var termDuration: String = "", // Q5.8 duration: 3, 4, 5 years

    // Section III: Judiciary
    var judgeAppointment: String = "", // Q5.9
    var judicialProtections: List<String> = emptyList(), // Q5.10
    var judicialReviewScope: String = "The judiciary can review all laws, executive actions, and administrative decisions for constitutionality",

    // CHAPTER SIX: MILITARY SYSTEM
    var supremeCommander: String = "", // Q6.1
    var militaryRestrictions: List<String> = emptyList(), // Q6.2

    // CHAPTER SEVEN: ECONOMIC POLICY
    var economicPrinciple: String = "", // Q7.1
    var economicPolicies: List<String> = emptyList(), // Q7.2

    // CHAPTER EIGHT: CULTURAL AND EDUCATIONAL POLICY
    var educationalProvisions: List<String> = emptyList(), // Q8.1
    var educationBudgetMin: String = "", // Q8.2

    // CHAPTER NINE: POLICY TOWARDS NATIONALITIES
    var minorityProtections: List<String> = emptyList(), // Q9.1

    // CHAPTER TEN: AMENDMENT AND INTERPRETATION
    var amendmentProcedure: String = "", // Q10.1
    var interpretationAuthority: String = "" // Q10.2
)

