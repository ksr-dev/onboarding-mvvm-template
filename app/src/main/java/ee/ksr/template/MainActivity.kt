package ee.ksr.template

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ee.ksr.template.databinding.ActivityMainBinding
import ee.ksr.template.ui.onboarding.OnboardingContainerFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}