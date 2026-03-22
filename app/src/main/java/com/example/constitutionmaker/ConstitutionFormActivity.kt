package com.example.constitutionmaker

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.constitutionmaker.databinding.ActivityConstitutionFormBinding
import com.example.constitutionmaker.database.ConstitutionDatabase
import com.example.constitutionmaker.database.ConstitutionEntity
import com.example.constitutionmaker.models.ConstitutionData
import com.google.gson.Gson
import kotlinx.coroutines.launch

class ConstitutionFormActivity : AppCompatActivity() {
    private lateinit var binding: ActivityConstitutionFormBinding
    private lateinit var constitutionData: ConstitutionData
    private val gson = Gson()
    private val database by lazy { ConstitutionDatabase.getDatabase(this) }
    private var registerNumber: String = ""
    private var username: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConstitutionFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        registerNumber = intent.getStringExtra("register_number") ?: ""
        username = intent.getStringExtra("username") ?: ""

        constitutionData = ConstitutionData()
        constitutionData.creatorName = username
        constitutionData.registerNumber = registerNumber

        setupScrollableViews()
        setupSubmitButton()
    }

    private fun setupScrollableViews() {
        setupGeneralInfo()
        setupChapter1()
        setupChapter2()
        setupChapter3()
        setupChapter4()
        setupChapter5()
        setupChapter6()
        setupChapter7()
        setupChapter8()
        setupChapter9()
        setupChapter10()
    }

    private fun setupGeneralInfo() {
        binding.editCountryName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                constitutionData.countryName = s.toString()
            }
        })
    }

    private fun setupChapter1() {
        binding.radioGroupFormOfGovernment.setOnCheckedChangeListener { _, checkedId ->
            constitutionData.formOfGovernment = when (checkedId) {
                R.id.radioConstitutionalMonarchy -> "Constitutional Monarchy"
                R.id.radioParliamentaryRepublic -> "Parliamentary Republic"
                R.id.radioPresidentialRepublic -> "Presidential Republic"
                R.id.radioSemiPresidential -> "Semi-Presidential Republic"
                else -> ""
            }
        }

        binding.radioGroupSourceOfAuthority.setOnCheckedChangeListener { _, checkedId ->
            constitutionData.sourceOfAuthority = when (checkedId) {
                R.id.radioMonarchDivine -> "The Monarch by divine right"
                R.id.radioPeople -> "The people, through democratic processes"
                R.id.radioSingleParty -> "A single political party"
                R.id.radioReligious -> "Religious authority"
                else -> ""
            }
        }

        binding.checkboxSecular.setOnCheckedChangeListener { _, isChecked -> constitutionData.secular = isChecked }
        binding.checkboxOfficialReligion.setOnCheckedChangeListener { _, isChecked -> constitutionData.officialReligion = isChecked }
        binding.checkboxSocial.setOnCheckedChangeListener { _, isChecked -> constitutionData.social = isChecked }
        binding.checkboxConstitutionSupreme.setOnCheckedChangeListener { _, isChecked -> constitutionData.constitutionSupreme = isChecked }
    }

    private fun setupChapter2() {
        val nonNullableRightsList = mutableListOf<String>()
        binding.checkboxRightToLife.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) nonNullableRightsList.add("Right to life") else nonNullableRightsList.remove("Right to life")
            constitutionData.nonNullableRights = nonNullableRightsList.toList()
        }
        binding.checkboxNoTorture.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) nonNullableRightsList.add("Prohibition of torture") else nonNullableRightsList.remove("Prohibition of torture")
            constitutionData.nonNullableRights = nonNullableRightsList.toList()
        }
        binding.checkboxNoSlavery.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) nonNullableRightsList.add("Prohibition of slavery") else nonNullableRightsList.remove("Prohibition of slavery")
            constitutionData.nonNullableRights = nonNullableRightsList.toList()
        }
        binding.checkboxFairTrial.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) nonNullableRightsList.add("Right to fair trial") else nonNullableRightsList.remove("Right to fair trial")
            constitutionData.nonNullableRights = nonNullableRightsList.toList()
        }
        binding.checkboxNoExPostFacto.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) nonNullableRightsList.add("Non-retroactivity of criminal laws") else nonNullableRightsList.remove("Non-retroactivity of criminal laws")
            constitutionData.nonNullableRights = nonNullableRightsList.toList()
        }

        val suspendableFreedomsList = mutableListOf<String>()
        binding.checkboxFreeSpeech.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) suspendableFreedomsList.add("Freedom of speech") else suspendableFreedomsList.remove("Freedom of speech")
            constitutionData.suspendableFreedoms = suspendableFreedomsList.toList()
        }
        binding.checkboxAssembly.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) suspendableFreedomsList.add("Freedom of assembly") else suspendableFreedomsList.remove("Freedom of assembly")
            constitutionData.suspendableFreedoms = suspendableFreedomsList.toList()
        }
        binding.checkboxAssociation.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) suspendableFreedomsList.add("Freedom of association") else suspendableFreedomsList.remove("Freedom of association")
            constitutionData.suspendableFreedoms = suspendableFreedomsList.toList()
        }
        binding.checkboxMovement.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) suspendableFreedomsList.add("Freedom of movement") else suspendableFreedomsList.remove("Freedom of movement")
            constitutionData.suspendableFreedoms = suspendableFreedomsList.toList()
        }
        binding.checkboxReligion.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) suspendableFreedomsList.add("Freedom of religion") else suspendableFreedomsList.remove("Freedom of religion")
            constitutionData.suspendableFreedoms = suspendableFreedomsList.toList()
        }

        binding.radioGroupSuspensionAuthority.setOnCheckedChangeListener { _, checkedId ->
            constitutionData.suspensionAuthority = when (checkedId) {
                R.id.radioPresidentAlone -> "President alone"
                R.id.radioLegislatureSupermajority -> "Legislature"
                R.id.radioExecutiveWithRatification -> "Executive"
                R.id.radioNoSuspension -> "No suspension"
                else -> ""
            }
        }

        binding.checkboxIncludeDuties.setOnCheckedChangeListener { _, isChecked ->
            constitutionData.includeFundamentalDuties = isChecked
            binding.dutiesContainer.visibility = if (isChecked) View.VISIBLE else View.GONE
        }

        val dutiesList = mutableListOf<String>()
        binding.checkboxDutyAbide.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) dutiesList.add("Abide by constitution") else dutiesList.remove("Abide by constitution")
            constitutionData.fundamentalDuties = dutiesList.toList()
        }
        binding.checkboxDutyDefend.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) dutiesList.add("Defend country") else dutiesList.remove("Defend country")
            constitutionData.fundamentalDuties = dutiesList.toList()
        }
        binding.checkboxDutyTaxes.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) dutiesList.add("Pay taxes") else dutiesList.remove("Pay taxes")
            constitutionData.fundamentalDuties = dutiesList.toList()
        }
        binding.checkboxDutyHarmony.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) dutiesList.add("Promote harmony") else dutiesList.remove("Promote harmony")
            constitutionData.fundamentalDuties = dutiesList.toList()
        }
        binding.checkboxDutyEnvironment.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) dutiesList.add("Protect environment") else dutiesList.remove("Protect environment")
            constitutionData.fundamentalDuties = dutiesList.toList()
        }
    }

    private fun setupChapter3() {
        binding.radioGroupFlagDetermination.setOnCheckedChangeListener { _, checkedId ->
            constitutionData.flagDetermination = when (checkedId) {
                R.id.radioFlagInConstitution -> "Described in constitution"
                R.id.radioFlagByLaw -> "Determined by law"
                R.id.radioFlagTraditional -> "Traditional flag"
                else -> ""
            }
        }

        binding.radioGroupCapitalDetermination.setOnCheckedChangeListener { _, checkedId ->
            constitutionData.capitalDetermination = when (checkedId) {
                R.id.radioCapitalInConstitution -> "Designated in constitution"
                R.id.radioCapitalByLaw -> "Determined by law"
                else -> ""
            }
            binding.capitalNameInput.visibility = if (checkedId == R.id.radioCapitalInConstitution) View.VISIBLE else View.GONE
        }

        binding.editCapitalName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) { constitutionData.capitalName = s.toString() }
        })
    }

    private fun setupChapter4() {
        val principlesList = mutableListOf<String>()
        binding.checkboxWealthDistribution.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) principlesList.add("Equitable wealth distribution") else principlesList.remove("Equitable wealth distribution")
            constitutionData.directivePrinciples = principlesList.toList()
        }
        binding.checkboxFreeEducation.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) principlesList.add("Free education") else principlesList.remove("Free education")
            constitutionData.directivePrinciples = principlesList.toList()
        }
        binding.checkboxLivingWage.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) principlesList.add("Living wage") else principlesList.remove("Living wage")
            constitutionData.directivePrinciples = principlesList.toList()
        }
        binding.checkboxInternationalPeace.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) principlesList.add("International peace") else principlesList.remove("International peace")
            constitutionData.directivePrinciples = principlesList.toList()
        }
        binding.checkboxEnvironment.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) principlesList.add("Protect environment") else principlesList.remove("Protect environment")
            constitutionData.directivePrinciples = principlesList.toList()
        }
        binding.checkboxCottageIndustries.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) principlesList.add("Cottage industries") else principlesList.remove("Cottage industries")
            constitutionData.directivePrinciples = principlesList.toList()
        }
        binding.checkboxSeparateJudiciary.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) principlesList.add("Separate judiciary") else principlesList.remove("Separate judiciary")
            constitutionData.directivePrinciples = principlesList.toList()
        }
    }

    private fun setupChapter5() {
        binding.radioGroupLegislatureStructure.setOnCheckedChangeListener { _, checkedId ->
            constitutionData.legislatureStructure = when (checkedId) {
                R.id.radioUnicameral -> "Unicameral"
                R.id.radioBicameral -> "Bicameral"
                else -> ""
            }
        }
        binding.radioGroupElectionSystem.setOnCheckedChangeListener { _, checkedId ->
            constitutionData.electionSystem = when (checkedId) {
                R.id.radioFPTP -> "First-past-the-post"
                R.id.radioProportional -> "Proportional representation"
                R.id.radioMixed -> "Mixed-member proportional"
                else -> ""
            }
        }

        val legislativePowersList = mutableListOf<String>()
        binding.checkboxPowerDeclareWar.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) legislativePowersList.add("Declare war") else legislativePowersList.remove("Declare war")
            constitutionData.legislativePowers = legislativePowersList.toList()
        }
        binding.checkboxPowerRatifyTreaties.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) legislativePowersList.add("Ratify treaties") else legislativePowersList.remove("Ratify treaties")
            constitutionData.legislativePowers = legislativePowersList.toList()
        }
        binding.checkboxPowerBudget.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) legislativePowersList.add("Approve budget") else legislativePowersList.remove("Approve budget")
            constitutionData.legislativePowers = legislativePowersList.toList()
        }
        binding.checkboxPowerConfirmAppointments.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) legislativePowersList.add("Confirm appointments") else legislativePowersList.remove("Confirm appointments")
            constitutionData.legislativePowers = legislativePowersList.toList()
        }
        binding.checkboxPowerImpeach.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) legislativePowersList.add("Impeach President") else legislativePowersList.remove("Impeach President")
            constitutionData.legislativePowers = legislativePowersList.toList()
        }

        binding.radioGroupPresidentSelection.setOnCheckedChangeListener { _, checkedId ->
            constitutionData.presidentSelection = when (checkedId) {
                R.id.radioDirectElection -> "Direct popular vote"
                R.id.radioElectoralCollege -> "Electoral college"
                R.id.radioLegislativeElection -> "Elected by legislature"
                R.id.radioHereditary -> "Hereditary succession"
                else -> ""
            }
        }
        binding.radioGroupPresidentTerms.setOnCheckedChangeListener { _, checkedId ->
            constitutionData.presidentTerms = when (checkedId) {
                R.id.radioOneTerm -> "One term only"
                R.id.radioTwoTerms -> "Two terms"
                R.id.radioUnlimitedTerms -> "Unlimited terms"
                R.id.radioNoTermLimit -> "No term limit specified"
                else -> ""
            }
        }
        binding.radioGroupPrimeMinisterAppointment.setOnCheckedChangeListener { _, checkedId ->
            constitutionData.primeMinisterAppointment = when (checkedId) {
                R.id.radioPMDirectElection -> "Direct election"
                R.id.radioPMAppointedByPresident -> "Appointed by President"
                R.id.radioPMAppointedWithoutConfirm -> "Appointed by President"
                R.id.radioPMElectedByLegislature -> "Elected by legislature"
                else -> ""
            }
        }
        binding.radioGroupJudgeAppointment.setOnCheckedChangeListener { _, checkedId ->
            constitutionData.judgeAppointment = when (checkedId) {
                R.id.radioJudgePresidentAlone -> "President alone"
                R.id.radioJudgePresidentWithConsent -> "President with consent"
                R.id.radioJudgeCommission -> "Independent Commission"
                R.id.radioJudgeLegislativeElection -> "Elected by legislature"
                else -> ""
            }
        }

        val judicialProtectionsList = mutableListOf<String>()
        binding.checkboxJudicialTenure.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) judicialProtectionsList.add("Life tenure") else judicialProtectionsList.remove("Life tenure")
            constitutionData.judicialProtections = judicialProtectionsList.toList()
        }
        binding.checkboxJudicialSalary.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) judicialProtectionsList.add("Salary protection") else judicialProtectionsList.remove("Salary protection")
            constitutionData.judicialProtections = judicialProtectionsList.toList()
        }
        binding.checkboxJudicialRemoval.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) judicialProtectionsList.add("Removal process") else judicialProtectionsList.remove("Removal process")
            constitutionData.judicialProtections = judicialProtectionsList.toList()
        }
        binding.checkboxJudicialAutonomy.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) judicialProtectionsList.add("Administrative autonomy") else judicialProtectionsList.remove("Administrative autonomy")
            constitutionData.judicialProtections = judicialProtectionsList.toList()
        }

        binding.radioGroupJudicialReview.setOnCheckedChangeListener { _, checkedId ->
            constitutionData.judicialReviewScope = when (checkedId) {
                R.id.radioReviewFull -> "Full review"
                R.id.radioReviewLimited -> "Limited review"
                R.id.radioReviewNotGranted -> "Not granted"
                else -> ""
            }
        }
    }

    private fun setupChapter6() {
        binding.radioGroupSupremeCommander.setOnCheckedChangeListener { _, checkedId ->
            constitutionData.supremeCommander = when (checkedId) {
                R.id.radioCommanderPresident -> "President"
                R.id.radioCommanderPrimeMinister -> "Prime Minister"
                R.id.radioCommanderMinister -> "Minister of Defense"
                R.id.radioCommanderMilitary -> "Military officer"
                else -> ""
            }
        }
        val militaryRestrictionsList = mutableListOf<String>()
        binding.checkboxMilitaryNoCivilOffice.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) militaryRestrictionsList.add("No civil office") else militaryRestrictionsList.remove("No civil office")
            constitutionData.militaryRestrictions = militaryRestrictionsList.toList()
        }
        binding.checkboxMilitaryApolitical.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) militaryRestrictionsList.add("Apolitical") else militaryRestrictionsList.remove("Apolitical")
            constitutionData.militaryRestrictions = militaryRestrictionsList.toList()
        }
        binding.checkboxMilitaryWarPower.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) militaryRestrictionsList.add("War power with legislature") else militaryRestrictionsList.remove("War power with legislature")
            constitutionData.militaryRestrictions = militaryRestrictionsList.toList()
        }
        binding.checkboxMilitaryNoPoliticalUse.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) militaryRestrictionsList.add("No political use") else militaryRestrictionsList.remove("No political use")
            constitutionData.militaryRestrictions = militaryRestrictionsList.toList()
        }
    }

    private fun setupChapter7() {
        binding.radioGroupEconomicPrinciple.setOnCheckedChangeListener { _, checkedId ->
            constitutionData.economicPrinciple = when (checkedId) {
                R.id.radioFreeMarket -> "Free-market"
                R.id.radioMixedEconomy -> "Mixed economy"
                R.id.radioStateControlled -> "State-controlled"
                R.id.radioCooperative -> "Cooperative-based"
                else -> ""
            }
        }
        val economicPoliciesList = mutableListOf<String>()
        binding.checkboxFreeMovement.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) economicPoliciesList.add("Free movement") else economicPoliciesList.remove("Free movement")
            constitutionData.economicPolicies = economicPoliciesList.toList()
        }
        binding.checkboxStateOwnership.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) economicPoliciesList.add("State ownership") else economicPoliciesList.remove("State ownership")
            constitutionData.economicPolicies = economicPoliciesList.toList()
        }
        binding.checkboxRegulateMonopolies.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) economicPoliciesList.add("Regulate monopolies") else economicPoliciesList.remove("Regulate monopolies")
            constitutionData.economicPolicies = economicPoliciesList.toList()
        }
        binding.checkboxSupportCooperatives.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) economicPoliciesList.add("Support cooperatives") else economicPoliciesList.remove("Support cooperatives")
            constitutionData.economicPolicies = economicPoliciesList.toList()
        }
    }

    private fun setupChapter8() {
        val educationalProvisionsList = mutableListOf<String>()
        binding.checkboxFreePrimaryEducation.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) educationalProvisionsList.add("Free primary education") else educationalProvisionsList.remove("Free primary education")
            constitutionData.educationalProvisions = educationalProvisionsList.toList()
        }
        binding.checkboxScholarships.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) educationalProvisionsList.add("Scholarships") else educationalProvisionsList.remove("Scholarships")
            constitutionData.educationalProvisions = educationalProvisionsList.toList()
        }
        binding.checkboxNoPoliticalInterference.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) educationalProvisionsList.add("No interference") else educationalProvisionsList.remove("No interference")
            constitutionData.educationalProvisions = educationalProvisionsList.toList()
        }
        binding.checkboxPrivateEducation.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) educationalProvisionsList.add("Private education") else educationalProvisionsList.remove("Private education")
            constitutionData.educationalProvisions = educationalProvisionsList.toList()
        }
        binding.radioGroupEducationBudget.setOnCheckedChangeListener { _, checkedId ->
            constitutionData.educationBudget = when (checkedId) {
                R.id.radioBudgetNone -> "None"
                R.id.radioBudget5 -> "5%"
                R.id.radioBudget10 -> "10%"
                R.id.radioBudget15 -> "15%"
                else -> ""
            }
        }
    }

    private fun setupChapter9() {
        val minorityProtectionsList = mutableListOf<String>()
        binding.checkboxMinorityCulture.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) minorityProtectionsList.add("Culture protection") else minorityProtectionsList.remove("Culture protection")
            constitutionData.minorityProtections = minorityProtectionsList.toList()
        }
        binding.checkboxMinorityNonDiscrimination.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) minorityProtectionsList.add("Non-discrimination") else minorityProtectionsList.remove("Non-discrimination")
            constitutionData.minorityProtections = minorityProtectionsList.toList()
        }
        binding.checkboxMinorityRepresentation.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) minorityProtectionsList.add("Representation") else minorityProtectionsList.remove("Representation")
            constitutionData.minorityProtections = minorityProtectionsList.toList()
        }
        binding.checkboxMinorityAutonomy.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) minorityProtectionsList.add("Autonomy") else minorityProtectionsList.remove("Autonomy")
            constitutionData.minorityProtections = minorityProtectionsList.toList()
        }
    }

    private fun setupChapter10() {
        binding.radioGroupAmendmentProcedure.setOnCheckedChangeListener { _, checkedId ->
            constitutionData.amendmentProcedure = when (checkedId) {
                R.id.radioAmendmentSimple -> "Simple majority"
                R.id.radioAmendmentSupermajority -> "Supermajority"
                R.id.radioAmendmentSupermajorityReferendum -> "Referendum"
                R.id.radioAmendmentSupermajorityStates -> "State ratification"
                else -> ""
            }
        }
        binding.radioGroupInterpretationAuthority.setOnCheckedChangeListener { _, checkedId ->
            constitutionData.interpretationAuthority = when (checkedId) {
                R.id.radioInterpretLegislature -> "Legislature"
                R.id.radioInterpretExecutive -> "Executive"
                R.id.radioInterpretCourt -> "Court"
                R.id.radioInterpretCouncil -> "Council"
                else -> ""
            }
        }
    }

    private fun setupSubmitButton() {
        binding.buttonSubmit.setOnClickListener {
            val errors = validateSelections()
            if (errors.isNotEmpty()) {
                AlertDialog.Builder(this)
                    .setTitle("Incomplete")
                    .setMessage(errors.joinToString("\n"))
                    .setPositiveButton("OK", null).show()
                return@setOnClickListener
            }

            val finalDocumentName = if (registerNumber.isNotEmpty()) registerNumber else "Constitution_${System.currentTimeMillis()}"

            lifecycleScope.launch {
                val jsonDataString = gson.toJson(constitutionData)
                val entity = ConstitutionEntity(
                    name = finalDocumentName,
                    jsonData = jsonDataString,
                    dateCreated = System.currentTimeMillis()
                )
                database.constitutionDao().insert(entity)
                Toast.makeText(this@ConstitutionFormActivity, "Saved as $finalDocumentName", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun validateSelections(): List<String> {
        val errors = mutableListOf<String>()
        if (constitutionData.countryName.isBlank()) errors.add("Country Name")
        if (constitutionData.formOfGovernment.isEmpty()) errors.add("Form of Government")
        if (constitutionData.sourceOfAuthority.isEmpty()) errors.add("Source of Authority")
        if (constitutionData.nonNullableRights.size < 2) errors.add("2 Non-Nullable Rights")
        if (constitutionData.suspendableFreedoms.isEmpty()) errors.add("1 Suspendable Freedom")
        if (constitutionData.suspensionAuthority.isEmpty()) errors.add("Suspension Authority")
        if (constitutionData.directivePrinciples.size < 3) errors.add("3 Directive Principles")
        if (constitutionData.legislatureStructure.isEmpty()) errors.add("Legislature Structure")
        if (constitutionData.presidentSelection.isEmpty()) errors.add("President Selection")
        if (constitutionData.judgeAppointment.isEmpty()) errors.add("Judge Appointment")
        if (constitutionData.amendmentProcedure.isEmpty()) errors.add("Amendment Procedure")
        if (constitutionData.interpretationAuthority.isEmpty()) errors.add("Interpretation Authority")
        return errors
    }
}