package com.example.constitutionmaker

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.constitutionmaker.databinding.ActivityPreviewBinding
import com.example.constitutionmaker.models.ConstitutionData
import com.example.constitutionmaker.utils.PDFGenerator
import com.google.gson.Gson
import java.io.File

class PreviewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPreviewBinding
    private lateinit var constitutionData: ConstitutionData
    private var constitutionName: String = ""

    companion object {
        private const val STORAGE_PERMISSION_CODE = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        constitutionName = intent.getStringExtra("constitution_name") ?: "Constitution"
        val jsonData = intent.getStringExtra("constitution_json") ?: ""
        constitutionData = Gson().fromJson(jsonData, ConstitutionData::class.java)

        displayConstitution()
        setupButtons()
    }

    private fun displayConstitution() {
        val htmlContent = generateConstitutionHTML()
        binding.webView.loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null)
    }

    private fun generateConstitutionHTML(): String {
        val countryName = constitutionData.countryName.ifEmpty { "[Country Name]" }
        val username = constitutionData.creatorName.ifEmpty { "[Username]" }
        val regNo = constitutionData.registerNumber.ifEmpty { "[Register No]" }
        
        return """
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>$constitutionName</title>
    <style>
        body {
            font-family: 'Times New Roman', serif;
            margin: 40px;
            line-height: 1.6;
            color: #333;
        }
        .header-info {
            font-weight: bold;
            font-size: 12pt;
            margin-bottom: 20px;
            border-bottom: 1px solid #333;
            padding-bottom: 5px;
        }
        .preamble {
            text-align: center;
            font-style: italic;
            margin: 40px 0;
            padding: 20px;
            border-top: 2px solid #ccc;
            border-bottom: 2px solid #ccc;
        }
        h1 {
            text-align: center;
            font-size: 24pt;
            margin-bottom: 20px;
        }
        h2 {
            font-size: 18pt;
            margin-top: 30px;
            color: #2c3e50;
            border-bottom: 2px solid #2c3e50;
            padding-bottom: 5px;
        }
        h3 {
            font-size: 16pt;
            margin-top: 20px;
            color: #34495e;
        }
        .article {
            margin: 20px 0;
        }
        .article-title {
            font-weight: bold;
            font-size: 14pt;
            margin-top: 15px;
        }
        .clause {
            margin-left: 20px;
            margin-bottom: 10px;
        }
        .footer {
            margin-top: 50px;
            text-align: center;
            font-size: 10pt;
            color: #7f8c8d;
            border-top: 1px solid #ccc;
            padding-top: 20px;
        }
    </style>
</head>
<body>
    <div class="header-info">
        USER: $username | REG NO: $regNo
    </div>

    <h1>THE CONSTITUTION</h1>
    <h1>OF THE FEDERAL REPUBLIC OF ${countryName.uppercase()}</h1>
    
    <div class="preamble">
        <strong>PREAMBLE</strong><br>
        We, the people of $countryName, in order to form a more perfect Union, 
        establish justice, insure domestic tranquility, provide for the common defense, 
        promote the general welfare, and secure the blessings of liberty to ourselves 
        and our posterity, do ordain and establish this Constitution for the 
        Federal Republic of $countryName.
    </div>
    
    ${generateChapter1()}
    ${generateChapter2()}
    ${generateChapter3()}
    ${generateChapter4()}
    ${generateChapter5()}
    ${generateChapter6()}
    ${generateChapter7()}
    ${generateChapter8()}
    ${generateChapter9()}
    ${generateChapter10()}
    
    <div class="footer">
        Adopted on this day, ${java.text.SimpleDateFormat("MMMM dd, yyyy", java.util.Locale.getDefault()).format(java.util.Date())}<br>
        ${constitutionName}
    </div>
</body>
</html>
        """.trimIndent()
    }

    private fun generateChapter1(): String {
        val countryName = constitutionData.countryName.ifEmpty { "[Country Name]" }
        val principles = mutableListOf<String>()
        if (constitutionData.secular) principles.add("secular")
        if (constitutionData.officialReligion) principles.add("with an official religion")
        if (constitutionData.social) principles.add("social")
        if (constitutionData.constitutionSupreme) principles.add("with the Constitution as the supreme law")

        val principlesText = if (principles.isNotEmpty()) principles.joinToString(", ") else ""

        return """
            <h2>CHAPTER ONE: GENERAL PRINCIPLES</h2>
            <div class="article">
                <div class="article-title">Article 1: The Republic</div>
                <div class="clause">
                    The Federal Republic of $countryName is a sovereign, democratic, 
                    ${constitutionData.formOfGovernment.lowercase()}, $principlesText republic.
                </div>
                <div class="clause">
                    ${if (constitutionData.constitutionSupreme) "This Constitution is the supreme law of the land. Any law inconsistent with it shall be null and void." else ""}
                </div>
                <div class="clause">
                    All state authority ${if (constitutionData.sourceOfAuthority.contains("people")) "emanates from the people." else "is derived from ${constitutionData.sourceOfAuthority.lowercase()}."}
                </div>
            </div>
        """.trimIndent()
    }

    private fun generateChapter2(): String {
        val nonNullableText = constitutionData.nonNullableRights.joinToString(";\n") {
            "    $it"
        }

        val freedomsText = constitutionData.suspendableFreedoms.joinToString(";\n") {
            "    $it"
        }

        val suspensionText = when {
            constitutionData.suspensionAuthority.contains("No suspension") ->
                "These freedoms are inviolable and shall not be suspended under any circumstances."
            else -> """
                These freedoms may be restricted only by law, and only to the extent necessary for public order, national security, or public health.
                During a declared national emergency, ${constitutionData.suspensionAuthority}.
            """.trimIndent()
        }

        var dutiesText = ""
        if (constitutionData.includeFundamentalDuties && constitutionData.fundamentalDuties.isNotEmpty()) {
            val duties = constitutionData.fundamentalDuties.joinToString(";\n") { "    $it" }
            dutiesText = """
                <div class="article">
                    <div class="article-title">Article 5: Duties of Citizens</div>
                    <div class="clause">
                        It shall be the duty of every citizen:<br>
                        $duties.
                    </div>
                </div>
            """.trimIndent()
        }

        return """
            <h2>CHAPTER TWO: FUNDAMENTAL RIGHTS AND DUTIES</h2>
            <div class="article">
                <div class="article-title">Part I: Non-Nullable Rights</div>
                <div class="article-title">Article 2: Absolute Protections</div>
                <div class="clause">
                    The following rights are inviolable and shall not be suspended under any circumstances:<br>
                    $nonNullableText.
                </div>
            </div>
            <div class="article">
                <div class="article-title">Part II: Freedoms Subject to Reasonable Restriction</div>
                <div class="article-title">Article 3: Guaranteed Freedoms</div>
                <div class="clause">
                    All citizens shall have the right to:<br>
                    $freedomsText.
                </div>
                <div class="article-title">Article 4: Suspension of Freedoms</div>
                <div class="clause">
                    $suspensionText
                </div>
            </div>
            $dutiesText
        """.trimIndent()
    }

    private fun generateChapter3(): String {
        val flagText = when {
            constitutionData.flagDetermination.contains("Described") ->
                "The National Flag shall be described in detail within this Constitution."
            constitutionData.flagDetermination.contains("law") ->
                "The National Flag shall be as determined by law."
            else -> "The National Flag shall be the traditional flag associated with the nation's founding."
        }

        val capitalText = if (constitutionData.capitalDetermination.contains("Designated")) {
            val name = constitutionData.capitalName
            val displayCity = if (name.isEmpty()) "[Capital City]" else name
            "The capital of the Republic shall be $displayCity."
        } else {
            "The capital of the Republic shall be as designated by law."
        }

        return """
            <h2>CHAPTER THREE: NATIONAL SYMBOLS</h2>
            <div class="article">
                <div class="article-title">Article 6: National Flag and Capital</div>
                <div class="clause">$flagText</div>
                <div class="clause">$capitalText</div>
            </div>
        """.trimIndent()
    }

    private fun generateChapter4(): String {
        val principles = constitutionData.directivePrinciples.joinToString(";\n") { "    $it" }

        return """
            <h2>CHAPTER FOUR: DIRECTIVE PRINCIPLES</h2>
            <div class="article">
                <div class="article-title">Article 7: Principles of State Policy</div>
                <div class="clause">
                    The State shall be guided by the following principles:<br>
                    $principles.
                </div>
            </div>
        """.trimIndent()
    }

    private fun generateChapter5(): String {
        val legislativeStructure = when (constitutionData.legislatureStructure) {
            "Unicameral" -> "a unicameral National Assembly"
            "Bicameral" -> "a bicameral National Assembly consisting of a lower house and an upper house"
            else -> "a National Assembly"
        }

        val electionText = when (constitutionData.electionSystem) {
            "First-past-the-post" -> "elected by first-past-the-post from single-member districts"
            "Proportional representation" -> "elected by proportional representation based on party lists"
            "Mixed-member proportional" -> "elected by a mixed-member proportional system"
            else -> "elected in accordance with law"
        }

        val powers = if (constitutionData.legislativePowers.isNotEmpty()) {
            constitutionData.legislativePowers.joinToString(", ")
        } else "to make laws"

        val presidentText = when (constitutionData.presidentSelection) {
            "Direct popular vote" -> "elected by direct popular vote"
            "Electoral college" -> "elected by an electoral college"
            "Elected by legislature" -> "elected by the National Assembly"
            "Hereditary succession" -> "succeeding by hereditary right"
            else -> "selected in accordance with law"
        }

        val termsText = when (constitutionData.presidentTerms) {
            "One term only" -> "for a single term"
            "Two terms" -> "for a term of five years, renewable once"
            "Unlimited terms" -> "for a term of five years, with no limit on re-election"
            else -> "for a term determined by law"
        }

        val pmText = when (constitutionData.primeMinisterAppointment) {
            "Direct election by people" -> "directly elected by the people"
            "Appointed by President, needs confidence" -> "appointed by the President and must command the confidence of the National Assembly"
            "Appointed by President without confirmation" -> "appointed by the President"
            "Elected by legislature" -> "elected by the National Assembly"
            else -> "appointed in accordance with law"
        }

        val judgeText = when (constitutionData.judgeAppointment) {
            "President alone" -> "appointed by the President"
            "President with legislative consent" -> "appointed by the President with the consent of the National Assembly"
            "Independent Judicial Commission" -> "appointed by an independent Judicial Appointments Commission"
            "Elected by legislature" -> "elected by the National Assembly"
            else -> "appointed in accordance with law"
        }

        val protections = constitutionData.judicialProtections.joinToString(";\n") { "    $it" }

        val reviewText = when (constitutionData.judicialReviewScope) {
            "Full judicial review" -> "The Supreme Court shall have the power of judicial review over all laws, executive actions, and administrative decisions."
            "Limited to fundamental rights" -> "The Supreme Court may review laws only for violations of fundamental rights."
            else -> "Judicial review shall be as determined by ordinary legislation."
        }

        return """
            <h2>CHAPTER FIVE: STRUCTURE OF THE STATE</h2>
            <div class="article">
                <div class="article-title">Section I: The Legislature</div>
                <div class="article-title">Article 8: Composition</div>
                <div class="clause">
                    The legislative power shall be vested in $legislativeStructure.
                    The lower house shall be $electionText.
                    ${if (constitutionData.legislatureStructure == "Bicameral") "The upper house shall represent territorial units." else ""}
                </div>
                <div class="article-title">Article 9: Legislative Powers</div>
                <div class="clause">
                    The National Assembly shall have the exclusive power to $powers.
                </div>
            </div>
            <div class="article">
                <div class="article-title">Section II: The Executive</div>
                <div class="article-title">Article 10: The President</div>
                <div class="clause">
                    The President is the Head of State and shall be $presidentText $termsText.
                    The Prime Minister shall be $pmText.
                </div>
            </div>
            <div class="article">
                <div class="article-title">Section III: The Judiciary</div>
                <div class="article-title">Article 11: Judicial Independence</div>
                <div class="clause">
                    Judges of the Supreme Court shall be $judgeText.<br>
                    The following protections shall be guaranteed:<br>
                    $protections.<br>
                    $reviewText
                </div>
            </div>
        """.trimIndent()
    }

    private fun generateChapter6(): String {
        val restrictions = constitutionData.militaryRestrictions.joinToString(";\n") { "    $it" }

        return """
            <h2>CHAPTER SIX: MILITARY SYSTEM</h2>
            <div class="article">
                <div class="article-title">Article 12: Civilian Control</div>
                <div class="clause">
                    ${constitutionData.supremeCommander} is the supreme commander of the armed forces.<br>
                    $restrictions.
                </div>
            </div>
        """.trimIndent()
    }

    private fun generateChapter7(): String {
        val policies = constitutionData.economicPolicies.joinToString(";\n") { "    $it" }

        return """
            <h2>CHAPTER SEVEN: ECONOMIC POLICY</h2>
            <div class="article">
                <div class="article-title">Article 13: Economic Principles</div>
                <div class="clause">
                    The state shall operate a ${constitutionData.economicPrinciple.lowercase()}.<br>
                    $policies.
                </div>
            </div>
        """.trimIndent()
    }

    private fun generateChapter8(): String {
        val provisions = constitutionData.educationalProvisions.joinToString(";\n") { "    $it" }
        val budgetText = when (constitutionData.educationBudget) {
            "No minimum" -> "No minimum percentage is specified."
            "At least 5%" -> "Not less than 5% of the national budget."
            "At least 10%" -> "Not less than 10% of the national budget."
            "At least 15%" -> "Not less than 15% of the national budget."
            else -> ""
        }

        return """
            <h2>CHAPTER EIGHT: CULTURAL AND EDUCATIONAL POLICY</h2>
            <div class="article">
                <div class="article-title">Article 14: Educational Rights</div>
                <div class="clause">
                    $provisions.<br>
                    Expenditure for education shall be: $budgetText
                </div>
            </div>
        """.trimIndent()
    }

    private fun generateChapter9(): String {
        val protections = constitutionData.minorityProtections.joinToString(";\n") { "    $it" }

        return """
            <h2>CHAPTER NINE: POLICY TOWARDS NATIONALITIES</h2>
            <div class="article">
                <div class="article-title">Article 15: Protection of Minorities</div>
                <div class="clause">
                    $protections.
                </div>
            </div>
        """.trimIndent()
    }

    private fun generateChapter10(): String {
        return """
            <h2>CHAPTER TEN: AMENDMENT AND INTERPRETATION</h2>
            <div class="article">
                <div class="article-title">Article 16: Amendment</div>
                <div class="clause">
                    ${constitutionData.amendmentProcedure}.
                </div>
                <div class="article-title">Article 17: Interpretation</div>
                <div class="clause">
                    ${constitutionData.interpretationAuthority} shall have the final authority to interpret this Constitution and to decide upon the constitutionality of laws.
                </div>
            </div>
        """.trimIndent()
    }

    private fun setupButtons() {
        binding.buttonExportPdf.setOnClickListener {
            if (checkPermission()) {
                generatePDF()
            } else {
                requestPermission()
            }
        }

        binding.buttonShare.setOnClickListener {
            if (checkPermission()) {
                sharePDF()
            } else {
                requestPermission()
            }
        }
    }

    private fun checkPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            true // App-specific Documents dir doesn't need permission on Android 11+
        } else {
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), STORAGE_PERMISSION_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                generatePDF()
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getTargetFile(): File? {
        val username = constitutionData.creatorName.replace(Regex("[^a-zA-Z0-9]"), "_")
        val regNo = constitutionData.registerNumber.replace(Regex("[^a-zA-Z0-9]"), "_")
        val fileName = "$regNo.$username.pdf"
        
        val directory = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        if (directory == null) return null
        if (!directory.exists()) directory.mkdirs()
        return File(directory, fileName)
    }

    private fun generatePDF(onComplete: (File?) -> Unit = {}) {
        val file = getTargetFile()
        if (file == null) {
            Toast.makeText(this, "Storage not available", Toast.LENGTH_SHORT).show()
            onComplete(null)
            return
        }

        PDFGenerator.generateFromWebView(binding.webView, file.absolutePath) { success ->
            if (success) {
                Toast.makeText(this, "PDF saved: ${file.name}", Toast.LENGTH_LONG).show()
                onComplete(file)
            } else {
                Toast.makeText(this, "Failed to generate PDF", Toast.LENGTH_SHORT).show()
                onComplete(null)
            }
        }
    }

    private fun sharePDF() {
        val file = getTargetFile()
        if (file == null) return
        
        if (!file.exists()) {
            generatePDF { generatedFile ->
                if (generatedFile != null) shareFile(generatedFile)
            }
        } else {
            shareFile(file)
        }
    }

    private fun shareFile(file: File) {
        val uri = FileProvider.getUriForFile(this, "com.example.constitutionmaker.fileprovider", file)
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "application/pdf"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        startActivity(Intent.createChooser(intent, "Share PDF"))
    }
}
