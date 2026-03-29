package com.example.constitutionmaker

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConstitutionFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        constitutionData = ConstitutionData().apply {
            creatorName = intent.getStringExtra("username") ?: ""
            registerNumber = intent.getStringExtra("register_number") ?: ""
        }

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
            override fun afterTextChanged(s: Editable?) { 
                constitutionData.countryName = s.toString()
                binding.tvChapter2Desc.text = "all citizens of the ${if (s.isNullOrBlank()) "[country name]" else s} shall have equal rights, freedoms and duties unless suspended by law."
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun setupChapter1() {
        binding.rgFormOfGov.setOnCheckedChangeListener { _, checkedId ->
            constitutionData.formOfGovernment = when (checkedId) {
                R.id.rbFormA -> "Constitutional Monarchy"
                R.id.rbFormB -> "Parliamentary Republic"
                R.id.rbFormC -> "Presidential Republic"
                R.id.rbFormD -> "Semi-Presidential Republic"
                else -> ""
            }
        }

        binding.rgSourceAuth.setOnCheckedChangeListener { _, checkedId ->
            constitutionData.sourceOfAuthority = when (checkedId) {
                R.id.rbSourceA -> "The Monarch by divine right"
                R.id.rbSourceB -> "The people, through democratic processes"
                R.id.rbSourceC -> "The union of the stats"
                R.id.rbSourceD -> "Rich history and culture"
                R.id.rbSourceE -> "Treaties and Conventions"
                else -> ""
            }
            binding.tilTreatyName.visibility = if (checkedId == R.id.rbSourceE) View.VISIBLE else View.GONE
        }

        binding.editTreatyName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) { constitutionData.treatyName = s.toString() }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.cbSocial.setOnCheckedChangeListener { _, isChecked -> constitutionData.isSocial = isChecked }

        val principlesList = mutableListOf<String>()
        val pWatcher = CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            val p = buttonView.text.toString().substringAfter(". ")
            if (isChecked) principlesList.add(p) else principlesList.remove(p)
            constitutionData.statePrinciples = principlesList.toList()
        }
        binding.cbP1.setOnCheckedChangeListener(pWatcher)
        binding.cbP2.setOnCheckedChangeListener(pWatcher)
        binding.cbP3.setOnCheckedChangeListener(pWatcher)
        binding.cbP4.setOnCheckedChangeListener(pWatcher)
        binding.cbP5.setOnCheckedChangeListener(pWatcher)
    }

    private fun setupChapter2() {
        binding.rgRightsSource.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.rbRightsConstitutional) {
                constitutionData.rightsSource = "Constitutional"
                setViewGroupEnabled(binding.containerB, true)
                setViewGroupEnabled(binding.containerA, false)
                binding.containerB.alpha = 1.0f
                binding.containerA.alpha = 0.5f
            } else if (checkedId == R.id.rbRightsNatural) {
                constitutionData.rightsSource = "Natural"
                setViewGroupEnabled(binding.containerB, false)
                setViewGroupEnabled(binding.containerA, true)
                binding.containerB.alpha = 0.5f
                binding.containerA.alpha = 1.0f
            }
        }

        // CONSTITUTIONAL (B)
        val constNNList = mutableListOf<String>()
        val cNNWatcher = CompoundButton.OnCheckedChangeListener { bv, isChecked ->
            val r = bv.text.toString().substringAfter(". ")
            if (isChecked) constNNList.add(r) else constNNList.remove(r)
            constitutionData.constNonNullableRights = constNNList.toList()
        }
        binding.cbB11A.setOnCheckedChangeListener(cNNWatcher)
        binding.cbB11B.setOnCheckedChangeListener(cNNWatcher)
        binding.cbB11C.setOnCheckedChangeListener(cNNWatcher)
        binding.cbB11D.setOnCheckedChangeListener(cNNWatcher)
        binding.cbB11E.setOnCheckedChangeListener(cNNWatcher)
        binding.cbB11F.setOnCheckedChangeListener(cNNWatcher)
        binding.cbB11G.setOnCheckedChangeListener(cNNWatcher)

        val constSList = mutableListOf<String>()
        binding.cbB12A.setOnCheckedChangeListener { _, isChecked ->
            val r = "Right to elect and to be elected according to law."
            if (isChecked) constSList.add(r) else constSList.remove(r)
            constitutionData.constSuspendableRights = constSList.toList()
            binding.tilVotingAgeB.visibility = if (isChecked) View.VISIBLE else View.GONE
        }
        binding.editVotingAgeB.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) { constitutionData.constVotingAge = s.toString() }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
        binding.cbB12C.setOnCheckedChangeListener { _, isChecked ->
            val r = "Right to work"
            if (isChecked) constSList.add(r) else constSList.remove(r)
            constitutionData.constSuspendableRights = constSList.toList()
            binding.cbB12D.visibility = if (isChecked) View.VISIBLE else View.GONE
        }
        val cSWatcher = CompoundButton.OnCheckedChangeListener { bv, isChecked ->
            val r = bv.text.toString().substringAfter(". ")
            if (isChecked) constSList.add(r) else constSList.remove(r)
            constitutionData.constSuspendableRights = constSList.toList()
        }
        binding.cbB12B.setOnCheckedChangeListener(cSWatcher)
        binding.cbB12D.setOnCheckedChangeListener(cSWatcher)
        binding.cbB12E.setOnCheckedChangeListener(cSWatcher)
        binding.cbB12F.setOnCheckedChangeListener(cSWatcher)
        binding.cbB12G.setOnCheckedChangeListener(cSWatcher)
        binding.cbB12H.setOnCheckedChangeListener(cSWatcher)
        binding.cbB12I.setOnCheckedChangeListener(cSWatcher)
        binding.cbB12J.setOnCheckedChangeListener(cSWatcher)

        val constFList = mutableListOf<String>()
        val cFWatcher = CompoundButton.OnCheckedChangeListener { bv, isChecked ->
            val f = bv.text.toString().substringAfter(". ")
            if (isChecked) constFList.add(f) else constFList.remove(f)
            constitutionData.constSuspendableFreedoms = constFList.toList()
        }
        binding.cbB21A.setOnCheckedChangeListener(cFWatcher)
        binding.cbB21B.setOnCheckedChangeListener(cFWatcher)
        binding.cbB21C.setOnCheckedChangeListener(cFWatcher)
        binding.cbB21D.setOnCheckedChangeListener(cFWatcher)
        binding.cbB21E.setOnCheckedChangeListener(cFWatcher)
        binding.cbB21F.setOnCheckedChangeListener(cFWatcher)
        binding.cbB21G.setOnCheckedChangeListener(cFWatcher)
        binding.cbB21H.setOnCheckedChangeListener(cFWatcher)
        binding.cbB21I.setOnCheckedChangeListener(cFWatcher)
        binding.cbB21J.setOnCheckedChangeListener(cFWatcher)
        binding.cbB21K.setOnCheckedChangeListener(cFWatcher)
        binding.cbB21L.setOnCheckedChangeListener(cFWatcher)
        binding.cbB21M.setOnCheckedChangeListener(cFWatcher)
        binding.cbB21N.setOnCheckedChangeListener(cFWatcher)
        binding.cbB21O.setOnCheckedChangeListener(cFWatcher)

        binding.rgSuspAuth.setOnCheckedChangeListener { _, checkedId ->
            constitutionData.suspensionAuthority = when (checkedId) {
                R.id.rbSuspA -> "The President alone"
                R.id.rbSuspB -> "The Legislature"
                R.id.rbSuspC -> "The Executive"
                R.id.rbSuspD -> "Suspension not permitted"
                else -> ""
            }
        }

        // NATURAL (A)
        val natNNList = mutableListOf<String>()
        val nNNWatcher = CompoundButton.OnCheckedChangeListener { bv, isChecked ->
            val r = bv.text.toString().substringAfter(". ")
            if (isChecked) natNNList.add(r) else natNNList.remove(r)
            constitutionData.naturalRightsNonNullable = natNNList.toList()
        }
        binding.cbA11A.setOnCheckedChangeListener(nNNWatcher)
        binding.cbA11B.setOnCheckedChangeListener(nNNWatcher)
        binding.cbA11C.setOnCheckedChangeListener(nNNWatcher)
        binding.cbA11D.setOnCheckedChangeListener(nNNWatcher)
        binding.cbA11E.setOnCheckedChangeListener(nNNWatcher)
        binding.cbA11F.setOnCheckedChangeListener(nNNWatcher)
        binding.cbA11G.setOnCheckedChangeListener(nNNWatcher)

        val natOList = mutableListOf<String>()
        binding.cbA11H.setOnCheckedChangeListener { _, isChecked ->
            val r = "Right to elect and to be elected"
            if (isChecked) natOList.add(r) else natOList.remove(r)
            constitutionData.naturalRightsOptional = natOList.toList()
            binding.tilVotingAgeA.visibility = if (isChecked) View.VISIBLE else View.GONE
        }
        binding.editVotingAgeA.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) { constitutionData.naturalVotingAge = s.toString() }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
        binding.cbA11J.setOnCheckedChangeListener { _, isChecked ->
            val r = "Right to work"
            if (isChecked) natOList.add(r) else natOList.remove(r)
            constitutionData.naturalRightsOptional = natOList.toList()
            binding.cbA11K.visibility = if (isChecked) View.VISIBLE else View.GONE
        }
        val nOWatcher = CompoundButton.OnCheckedChangeListener { bv, isChecked ->
            val r = bv.text.toString().substringAfter(". ")
            if (isChecked) natOList.add(r) else natOList.remove(r)
            constitutionData.naturalRightsOptional = natOList.toList()
        }
        binding.cbA11I.setOnCheckedChangeListener(nOWatcher)
        binding.cbA11K.setOnCheckedChangeListener(nOWatcher)
        binding.cbA11L.setOnCheckedChangeListener(nOWatcher)
        binding.cbA11M.setOnCheckedChangeListener(nOWatcher)
        binding.cbA11N.setOnCheckedChangeListener(nOWatcher)
        binding.cbA11O.setOnCheckedChangeListener(nOWatcher)
        binding.cbA11P.setOnCheckedChangeListener(nOWatcher)
        binding.cbA11Q.setOnCheckedChangeListener(nOWatcher)

        val natFList = mutableListOf<String>()
        val nFWatcher = CompoundButton.OnCheckedChangeListener { bv, isChecked ->
            val f = bv.text.toString().substringAfter(". ")
            if (isChecked) natFList.add(f) else natFList.remove(f)
            constitutionData.naturalFreedoms = natFList.toList()
        }
        binding.cbA21A.setOnCheckedChangeListener(nFWatcher)
        binding.cbA21B.setOnCheckedChangeListener(nFWatcher)
        binding.cbA21C.setOnCheckedChangeListener(nFWatcher)
        binding.cbA21D.setOnCheckedChangeListener(nFWatcher)
        binding.cbA21E.setOnCheckedChangeListener(nFWatcher)
        binding.cbA21F.setOnCheckedChangeListener(nFWatcher)
        binding.cbA21G.setOnCheckedChangeListener(nFWatcher)
        binding.cbA21H.setOnCheckedChangeListener(nFWatcher)
        binding.cbA21I.setOnCheckedChangeListener(nFWatcher)
        binding.cbA21J.setOnCheckedChangeListener(nFWatcher)
        binding.cbA21K.setOnCheckedChangeListener(nFWatcher)
        binding.cbA21L.setOnCheckedChangeListener(nFWatcher)
        binding.cbA21M.setOnCheckedChangeListener(nFWatcher)
        binding.cbA21N.setOnCheckedChangeListener(nFWatcher)
        binding.cbA21O.setOnCheckedChangeListener(nFWatcher)

        // DUTIES
        binding.rgIncludeDuties.setOnCheckedChangeListener { _, checkedId ->
            constitutionData.includeDuties = (checkedId == R.id.rbDutiesYes)
            binding.containerDuties.visibility = if (constitutionData.includeDuties) View.VISIBLE else View.GONE
            binding.tvChapter2Title.text = if (constitutionData.includeDuties) "CHAPTER TWO: FUNDAMENTAL RIGHTS AND DUTIES" else "CHAPTER TWO: FUNDAMENTAL RIGHTS"
        }
        val dutiesList = mutableListOf<String>()
        val dWatcher = CompoundButton.OnCheckedChangeListener { bv, isChecked ->
            val d = bv.text.toString().substringAfter(". ")
            if (isChecked) dutiesList.add(d) else dutiesList.remove(d)
            constitutionData.fundamentalDuties = dutiesList.toList()
        }
        binding.cbDutyA.setOnCheckedChangeListener(dWatcher)
        binding.cbDutyB.setOnCheckedChangeListener(dWatcher)
        binding.cbDutyC.setOnCheckedChangeListener(dWatcher)
        binding.cbDutyD.setOnCheckedChangeListener(dWatcher)
        binding.cbDutyE.setOnCheckedChangeListener(dWatcher)
        binding.cbDutyG.setOnCheckedChangeListener(dWatcher)
    }

    private fun setupChapter3() {
        binding.rgFlag.setOnCheckedChangeListener { _, checkedId ->
            constitutionData.flagDetermination = if (checkedId == R.id.rbFlagA) "Described in constitution" else "Determined by law"
            binding.tilFlag.visibility = if (checkedId == R.id.rbFlagA) View.VISIBLE else View.GONE
        }
        binding.editFlag.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) { constitutionData.flagDescription = s.toString() }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.rgEmblem.setOnCheckedChangeListener { _, checkedId ->
            constitutionData.emblemDetermination = if (checkedId == R.id.rbEmblemA) "Described in constitution" else "Determined by law"
            binding.tilEmblem.visibility = if (checkedId == R.id.rbEmblemA) View.VISIBLE else View.GONE
        }
        binding.editEmblem.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) { constitutionData.emblemDescription = s.toString() }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.rgCapital.setOnCheckedChangeListener { _, checkedId ->
            constitutionData.capitalDetermination = if (checkedId == R.id.rbCapA) "Designated by name in constitution" else "Determined by law"
            binding.tilCap.visibility = if (checkedId == R.id.rbCapA) View.VISIBLE else View.GONE
        }
        binding.editCap.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) { constitutionData.capitalName = s.toString() }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun setupChapter4() {
        val dpList = mutableListOf<String>()
        val dpWatcher = CompoundButton.OnCheckedChangeListener { bv, isChecked ->
            val p = bv.text.toString().substringAfter(". ")
            if (isChecked) dpList.add(p) else dpList.remove(p)
            constitutionData.directivePrinciples = dpList.toList()
        }
        binding.cbD41A.setOnCheckedChangeListener(dpWatcher)
        binding.cbD41B.setOnCheckedChangeListener(dpWatcher)
        binding.cbD41C.setOnCheckedChangeListener(dpWatcher)
        binding.cbD41D.setOnCheckedChangeListener(dpWatcher)
        binding.cbD41E.setOnCheckedChangeListener(dpWatcher)
        binding.cbD41F.setOnCheckedChangeListener(dpWatcher)
        binding.cbD41G.setOnCheckedChangeListener(dpWatcher)

        val customDList = mutableListOf<String>()
        binding.btnAddD.setOnClickListener {
            val et = EditText(this)
            AlertDialog.Builder(this).setTitle("Add Principle").setView(et).setPositiveButton("Add") { _, _ ->
                val p = et.text.toString()
                if (p.isNotBlank()) {
                    customDList.add(p)
                    constitutionData.customDirectivePrinciples = customDList.toList()
                    val tv = TextView(this)
                    tv.text = "• $p"
                    binding.containerCustomD.addView(tv)
                }
            }.setNegativeButton("Cancel", null).show()
        }
    }

    private fun setupChapter5() {
        binding.rgLegStructure.setOnCheckedChangeListener { _, id ->
            constitutionData.legislatureStructure = when (id) {
                R.id.rbLegUni -> "Unicameral"
                R.id.rbLegBi -> "Bicameral"
                R.id.rbLegTri -> "Tricameral"
                else -> ""
            }
            binding.containerUpper.visibility = if (id != R.id.rbLegUni) View.VISIBLE else View.GONE
            binding.tvTriDesc.visibility = if (id == R.id.rbLegTri) View.VISIBLE else View.GONE
        }
        binding.rgLegElect.setOnCheckedChangeListener { _, id ->
            constitutionData.lowerHouseElection = when (id) {
                R.id.rbElectA -> "First-past-the-post"
                R.id.rbElectB -> "Proportional representation"
                R.id.rbElectC -> "Mixed-member proportional"
                else -> ""
            }
        }
        val lpList = mutableListOf<String>()
        val lpWatcher = CompoundButton.OnCheckedChangeListener { bv, isChecked ->
            val p = bv.text.toString().substringAfter(". ")
            if (isChecked) lpList.add(p) else lpList.remove(p)
            constitutionData.legislaturePowers = lpList.toList()
        }
        binding.cbPowA.setOnCheckedChangeListener(lpWatcher)
        binding.cbPowB.setOnCheckedChangeListener(lpWatcher)
        binding.cbPowC.setOnCheckedChangeListener(lpWatcher)
        binding.cbPowD.setOnCheckedChangeListener(lpWatcher)
        binding.cbPowE.setOnCheckedChangeListener(lpWatcher)

        binding.rgUpperElect.setOnCheckedChangeListener { _, id ->
            constitutionData.upperHouseElection = when (id) {
                R.id.rbUpperA -> "Indirect election by lower house"
                R.id.rbUpperB -> "Appointment by state assemblies"
                R.id.rbUpperC -> "Mixed-member proportional"
                else -> ""
            }
        }
        val upList = mutableListOf<String>()
        val upWatcher = CompoundButton.OnCheckedChangeListener { bv, isChecked ->
            val p = bv.text.toString().substringAfter(". ")
            if (isChecked) upList.add(p) else upList.remove(p)
            constitutionData.upperHousePowers = upList.toList()
        }
        binding.cbUPowA.setOnCheckedChangeListener(upWatcher)
        binding.cbUPowB.setOnCheckedChangeListener(upWatcher)
        binding.cbUPowC.setOnCheckedChangeListener(upWatcher)
        binding.cbUPowD.setOnCheckedChangeListener(upWatcher)

        binding.rgPresSelect.setOnCheckedChangeListener { _, id ->
            constitutionData.presidentSelection = when (id) {
                R.id.rbPresA -> "Direct election by popular vote"
                R.id.rbPresB -> "Election by an electoral college"
                R.id.rbPresC -> "Election by the legislature"
                else -> ""
            }
        }
        binding.rgPresTerms.setOnCheckedChangeListener { _, id ->
            constitutionData.presidentMaxTerms = when (id) {
                R.id.rbTermA -> "One term only"
                R.id.rbTermB -> "Two terms"
                R.id.rbTermC -> "Unlimited terms"
                R.id.rbTermD -> "No term limit specified"
                else -> ""
            }
        }
        binding.rgPmAppt.setOnCheckedChangeListener { _, id ->
            constitutionData.pmAppointment = when (id) {
                R.id.rbPmA -> "Direct election by the people"
                R.id.rbPmB -> "Appointed by the President"
                R.id.rbPmD -> "Elected directly by the legislature"
                else -> ""
            }
        }
        binding.rgDur.setOnCheckedChangeListener { _, id ->
            constitutionData.termDuration = when (id) {
                R.id.rbDurA -> "4 years"
                R.id.rbDurB -> "3 years"
                R.id.rbDurC -> "5 years"
                else -> ""
            }
        }
        binding.rgJudAppt.setOnCheckedChangeListener { _, id ->
            constitutionData.judgeAppointment = when (id) {
                R.id.rbJudA -> "Appointed by President alone"
                R.id.rbJudB -> "Appointed by President with legislature"
                R.id.rbJudC -> "Independent Commission"
                R.id.rbJudD -> "Elected by legislature"
                else -> ""
            }
        }
        val jpList = mutableListOf<String>()
        val jpWatcher = CompoundButton.OnCheckedChangeListener { bv, isChecked ->
            val p = bv.text.toString().substringAfter(". ")
            if (isChecked) jpList.add(p) else jpList.remove(p)
            constitutionData.judicialProtections = jpList.toList()
        }
        binding.cbJProtA.setOnCheckedChangeListener(jpWatcher)
        binding.cbJProtB.setOnCheckedChangeListener(jpWatcher)
        binding.cbJProtC.setOnCheckedChangeListener(jpWatcher)
        binding.cbJProtD.setOnCheckedChangeListener(jpWatcher)
    }

    private fun setupChapter6() {
        binding.rgMilComm.setOnCheckedChangeListener { _, id ->
            constitutionData.supremeCommander = when (id) {
                R.id.rbMCommA -> "The President"
                R.id.rbMCommB -> "The Prime Minister"
                R.id.rbMCommC -> "The Minister of Defense"
                R.id.rbMCommD -> "A senior military officer"
                else -> ""
            }
        }
        val mrList = mutableListOf<String>()
        val mrWatcher = CompoundButton.OnCheckedChangeListener { bv, isChecked ->
            val r = bv.text.toString().substringAfter(". ")
            if (isChecked) mrList.add(r) else mrList.remove(r)
            constitutionData.militaryRestrictions = mrList.toList()
        }
        binding.cbMRestA.setOnCheckedChangeListener(mrWatcher)
        binding.cbMRestB.setOnCheckedChangeListener(mrWatcher)
        binding.cbMRestC.setOnCheckedChangeListener(mrWatcher)
    }

    private fun setupChapter7() {
        binding.rgEco.setOnCheckedChangeListener { _, id ->
            constitutionData.economicPrinciple = when (id) {
                R.id.rbEcoA -> "Free-market capitalism"
                R.id.rbEcoB -> "Mixed economy"
                R.id.rbEcoC -> "State-controlled economy"
                R.id.rbEcoD -> "Cooperative-based economy"
                else -> ""
            }
        }
        val epList = mutableListOf<String>()
        val epWatcher = CompoundButton.OnCheckedChangeListener { bv, isChecked ->
            val p = bv.text.toString().substringAfter(". ")
            if (isChecked) epList.add(p) else epList.remove(p)
            constitutionData.economicPolicies = epList.toList()
        }
        binding.cbEcoPolA.setOnCheckedChangeListener(epWatcher)
        binding.cbEcoPolB.setOnCheckedChangeListener(epWatcher)
        binding.cbEcoPolC.setOnCheckedChangeListener(epWatcher)
        binding.cbEcoPolD.setOnCheckedChangeListener(epWatcher)
    }

    private fun setupChapter8() {
        val epList = mutableListOf<String>()
        val epWatcher = CompoundButton.OnCheckedChangeListener { bv, isChecked ->
            val p = bv.text.toString().substringAfter(". ")
            if (isChecked) epList.add(p) else epList.remove(p)
            constitutionData.educationalProvisions = epList.toList()
        }
        binding.cbEduA.setOnCheckedChangeListener(epWatcher)
        binding.cbEduC.setOnCheckedChangeListener(epWatcher)
        binding.cbEduD.setOnCheckedChangeListener(epWatcher)

        binding.rgEduBud.setOnCheckedChangeListener { _, id ->
            constitutionData.educationBudgetMin = when (id) {
                R.id.rbEBudA -> "No minimum"
                R.id.rbEBudB -> "2%"
                R.id.rbEBudC -> "5%"
                R.id.rbEBudD -> "8%"
                else -> ""
            }
        }
    }

    private fun setupChapter9() {
        val mpList = mutableListOf<String>()
        val mpWatcher = CompoundButton.OnCheckedChangeListener { bv, isChecked ->
            val p = bv.text.toString().substringAfter(". ")
            if (isChecked) mpList.add(p) else mpList.remove(p)
            constitutionData.minorityProtections = mpList.toList()
        }
        binding.cbMinA.setOnCheckedChangeListener(mpWatcher)
        binding.cbMinB.setOnCheckedChangeListener(mpWatcher)
        binding.cbMinC.setOnCheckedChangeListener(mpWatcher)
        binding.cbMinD.setOnCheckedChangeListener(mpWatcher)
    }

    private fun setupChapter10() {
        binding.rgAmend.setOnCheckedChangeListener { _, id ->
            constitutionData.amendmentProcedure = when (id) {
                R.id.rbAmendA -> "To amend the constitution simple mejority in parliment with president approval is needed"
                R.id.rbAmendB -> "To amend the constitution supreme mejority in parliment is needed"
                R.id.rbAmendC -> "To amend the constitution supreme mejority in parliment with a national referandum to be conduct to get the approval of the people"
                R.id.rbAmendD -> "To amend the constitution supreme mejority in parliment with a state simple majority of states accepting the amendment"
                else -> ""
            }
        }
        binding.rgInterp.setOnCheckedChangeListener { _, id ->
            constitutionData.interpretationAuthority = when (id) {
                R.id.rbInterpA -> "Legislature"
                R.id.rbInterpB -> "Executive"
                R.id.rbInterpC -> "Highest Court"
                R.id.rbInterpD -> "Constitutional Council"
                else -> ""
            }
        }
    }

    private fun setViewGroupEnabled(vg: ViewGroup, enabled: Boolean) {
        vg.isEnabled = enabled
        for (i in 0 until vg.childCount) {
            val child = vg.getChildAt(i)
            if (child is ViewGroup) setViewGroupEnabled(child, enabled) else child.isEnabled = enabled
        }
    }

    private fun setupSubmitButton() {
        binding.buttonSubmit.setOnClickListener {
            val errors = validateSelections()
            if (errors.isNotEmpty()) {
                AlertDialog.Builder(this).setTitle("Incomplete").setMessage(errors.joinToString("\n")).setPositiveButton("OK", null).show()
                return@setOnClickListener
            }
            lifecycleScope.launch {
                val entity = ConstitutionEntity(name = constitutionData.countryName, jsonData = gson.toJson(constitutionData), dateCreated = System.currentTimeMillis())
                database.constitutionDao().insert(entity)
                Toast.makeText(this@ConstitutionFormActivity, "Constitution for ${constitutionData.countryName} saved!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun validateSelections(): List<String> {
        val err = mutableListOf<String>()
        if (constitutionData.countryName.isBlank()) err.add("• Country Name")
        if (constitutionData.formOfGovernment.isEmpty()) err.add("• Form of Government")
        if (constitutionData.sourceOfAuthority.isEmpty()) err.add("• Source of Authority")
        if (constitutionData.rightsSource.isEmpty()) err.add("• Rights Source")
        if (constitutionData.rightsSource == "Natural" && constitutionData.naturalRightsNonNullable.size < 6) err.add("• At least 6 natural non-nullable rights")
        if (constitutionData.directivePrinciples.size + constitutionData.customDirectivePrinciples.size < 3) err.add("• At least 3 directive principles")
        if (constitutionData.legislatureStructure.isEmpty()) err.add("• Legislature Structure")
        return err
    }
}
