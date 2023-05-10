package com.tauheed.githubwavybottomnavigation

import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.tauheed.githubwavybottomnavigation.databinding.ActivityMainBinding
import com.tauheed.wavybottomnavigation.WavyBottomNavigation

class MainActivity : AppCompatActivity() {

    companion object {
        private const val ID_HOME = 1
        private const val ID_EXPLORE = 2
        private const val ID_MESSAGE = 3
        private const val ID_NOTIFICATION = 4
        private const val ID_ACCOUNT = 5
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(
            this,
            R.layout.activity_main
        )

        val tvSelected = binding.tvSelected
        tvSelected.typeface = Typeface.createFromAsset(assets, "fonts/SourceSansPro-Regular.ttf")

        binding.bottomNavigation.apply {

            add(
                WavyBottomNavigation.Model(
                    ID_HOME,
                    R.drawable.ic_home
                )
            )
            add(
                WavyBottomNavigation.Model(
                    ID_EXPLORE,
                    R.drawable.ic_explore
                )
            )
            add(
                WavyBottomNavigation.Model(
                    ID_MESSAGE,
                    R.drawable.ic_message
                )
            )
            add(
                WavyBottomNavigation.Model(
                    ID_NOTIFICATION,
                    R.drawable.ic_notification
                )
            )
            add(
                WavyBottomNavigation.Model(
                    ID_ACCOUNT,
                    R.drawable.ic_account
                )
            )

            setCount(ID_NOTIFICATION, "100")

            setOnShowListener {
                val name = when (it.id) {
                    ID_HOME -> {
                        binding.constraintlayout.setBackgroundColor(Color.parseColor("#D84879"))
                        "HOME"
                    }
                    ID_EXPLORE -> {
                        binding.constraintlayout.setBackgroundColor(Color.parseColor("#4CAF50"))
                        "EXPLORE"
                    }
                    ID_MESSAGE -> {
                        binding.constraintlayout.setBackgroundColor(Color.parseColor("#ffa500"))
                        "MESSAGE"
                    }
                    ID_NOTIFICATION -> {
                        binding.constraintlayout.setBackgroundColor(Color.parseColor("#ff69b4"))
                        "NOTIFICATION"
                    }
                    ID_ACCOUNT -> {
                        binding.constraintlayout.setBackgroundColor(Color.parseColor("#6495ed"))
                        "ACCOUNT"
                    }
                    else -> ""
                }

                tvSelected.text = getString(R.string.main_page_selected, name)
            }

            setOnClickMenuListener {
                val name = when (it.id) {
                    ID_HOME -> "HOME"
                    ID_EXPLORE -> "EXPLORE"
                    ID_MESSAGE -> "MESSAGE"
                    ID_NOTIFICATION -> "NOTIFICATION"
                    ID_ACCOUNT -> "ACCOUNT"
                    else -> ""
                }
            }

            setOnReselectListener {
                Toast.makeText(context, "item ${it.id} is reselected.", Toast.LENGTH_LONG).show()
            }

            show(ID_HOME)

        }

        binding.btShow.setOnClickListener {
            val id = try {
                binding.etPageId.text.toString().toInt()
            } catch (e: Exception) {
                ID_HOME
            }
            if (id in ID_HOME..ID_ACCOUNT)
                binding.bottomNavigation.show(id)
        }

    }
}
