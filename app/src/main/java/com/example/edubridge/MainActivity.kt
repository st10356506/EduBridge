package com.example.edubridge

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.edubridge.data.Lesson
import com.example.edubridge.data.LessonDao
import com.example.edubridge.fragments.StudyFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavView: BottomNavigationView
    private val lessonDao = LessonDao() // Initialize LessonDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // 🔹 Setup bottom navigation
        bottomNavView = findViewById(R.id.bottom_navigation)

        if (savedInstanceState == null) {
            replaceFragment(HomeFragment())
            bottomNavView.selectedItemId = R.id.nav_dashboard
        }

        bottomNavView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_dashboard -> replaceFragment(HomeFragment())
                R.id.nav_study -> replaceFragment(StudyFragment())
                R.id.nav_chat -> replaceFragment(AiFragment())
                R.id.nav_scan -> replaceFragment(ScanFragment())
                R.id.nav_rewards -> replaceFragment(RewardsFragment())
            }
            true
        }

        // 🔹 Populate sample lessons in Firebase (only run once, or remove after testing)
        populateSampleLessons()
    }

    private fun replaceFragment(frag: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, frag)
            .commit()
    }

    private fun populateSampleLessons() {
        val sampleLessons = listOf(
            Lesson(
                id = "lesson_1",
                title = "Introduction to Algebra",
                duration = "15 min",
                difficulty = "Easy",
                content = """
                <h2>Algebra Basics</h2>
                <p>Algebra is the branch of mathematics dealing with symbols and the rules for manipulating them.</p>
                <p><b>Key Concepts:</b></p>
                <ul>
                    <li>Variables: letters representing unknown values, e.g., x, y</li>
                    <li>Constants: fixed values like 2, 10, etc.</li>
                    <li>Equations: statements that two expressions are equal, e.g., 2x + 3 = 7</li>
                </ul>
                <p><b>Example:</b> Solve 2x + 3 = 7 → 2x = 4 → x = 2</p>
            """.trimIndent()
            ),
            Lesson(
                id = "lesson_2",
                title = "Photosynthesis",
                duration = "12 min",
                difficulty = "Medium",
                content = """
                <h2>Photosynthesis Process</h2>
                <p>Photosynthesis is how plants convert sunlight into chemical energy.</p>
                <p><b>Key Steps:</b></p>
                <ol>
                    <li>Chlorophyll captures sunlight.</li>
                    <li>Water and CO₂ are converted into glucose and oxygen.</li>
                    <li>Energy is stored in glucose molecules.</li>
                </ol>
                <p><b>Example:</b> 6CO₂ + 6H₂O → C₆H₁₂O₆ + 6O₂</p>
            """.trimIndent()
            ),
            Lesson(
                id = "lesson_3",
                title = "World War II Overview",
                duration = "20 min",
                difficulty = "Hard",
                content = """
                <h2>World War II Summary</h2>
                <p>World War II was a global conflict from 1939 to 1945, involving most of the world's nations.</p>
                <p><b>Key Events:</b></p>
                <ul>
                    <li>1939: Germany invades Poland</li>
                    <li>1941: Attack on Pearl Harbor</li>
                    <li>1945: Germany and Japan surrender</li>
                </ul>
                <p><b>Example Impact:</b> Millions of lives lost, major geopolitical changes, United Nations formed.</p>
            """.trimIndent()
            ),
            Lesson(
                id = "lesson_4",
                title = "Chemistry: The Atom",
                duration = "25 min",
                difficulty = "Medium",
                content = """
                <h2>Atomic Structure</h2>
                <p>Atoms are the fundamental building blocks of matter. Understanding their structure is essential for chemistry.</p>
                <p><b>Components:</b></p>
                <ul>
                    <li>Protons: positively charged particles</li>
                    <li>Neutrons: neutral particles</li>
                    <li>Electrons: negatively charged particles orbiting the nucleus</li>
                </ul>
                <p><b>Example:</b> Carbon atom → 6 protons, 6 neutrons, 6 electrons</p>
            """.trimIndent()
            )
        )

        sampleLessons.forEach { lessonDao.saveLesson(it) }
    }

}
