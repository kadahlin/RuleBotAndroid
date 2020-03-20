package com.kyledahlin.reactions

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kyledahlin.myrulebot.rules.RuleActivity

@RuleActivity(name = "reactions")
class ReactionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reaction)
        val activities = packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
        val activityNames = activities.activities[0].name
    }
}
