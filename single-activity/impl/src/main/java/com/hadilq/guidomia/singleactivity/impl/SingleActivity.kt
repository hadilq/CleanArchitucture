package com.hadilq.guidomia.singleactivity.impl

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.hadilq.guidomia.core.api.di.DaggerSingleActivityScope
import com.hadilq.guidomia.core.api.di.SingleActivityScope
import com.hadilq.guidomia.core.api.di.SingleIn
import com.hadilq.guidomia.core.impl.viewBinding
import com.hadilq.guidomia.guidomia.api.GuidomiaNavigatorFactory
import com.hadilq.guidomia.singleactivity.impl.databinding.ActivityMainBinding
import com.hadilq.guidomia.singleactivity.impl.di.SingleActivityComponent
import com.hadilq.guidomia.singleactivity.impl.di.SingleActivityComponentProvider
import javax.inject.Inject

@DaggerSingleActivityScope
@SingleIn(SingleActivityScope::class)
class SingleActivity : AppCompatActivity() {

  private val component: SingleActivityComponent by lazy {
    (application as SingleActivityComponentProvider)
      .singleActivityComponentProvider
      .build()
  }

  @Inject
  internal lateinit var guidomiaNavigatorFactory: GuidomiaNavigatorFactory

  private val binding by viewBinding { ActivityMainBinding.inflate(layoutInflater) }

  override fun onCreate(savedInstanceState: Bundle?) {
    component.inject(this)
    super.onCreate(savedInstanceState)
    setContentView(binding.root)
    setSupportActionBar(binding.toolbar)
    supportActionBar?.setHomeAsUpIndicator(
      ContextCompat.getDrawable(this, R.drawable.ic_baseline_dehaze_24)
    )
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
    supportActionBar?.setDisplayShowTitleEnabled(false)
    binding.toolbar.logo = ContextCompat.getDrawable(this, R.drawable.logo)

    if (savedInstanceState == null) {
      guidomiaNavigatorFactory.create(this).commit()
    }
  }
}


