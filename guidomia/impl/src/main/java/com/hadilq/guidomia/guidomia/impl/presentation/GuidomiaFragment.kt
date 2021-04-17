package com.hadilq.guidomia.guidomia.impl.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.hadilq.guidomia.core.api.ViewModelFactory
import com.hadilq.guidomia.core.api.di.FragmentScope
import com.hadilq.guidomia.core.api.di.SingleIn
import com.hadilq.guidomia.core.impl.viewBinding
import com.hadilq.guidomia.guidomia.impl.databinding.FragmentGuidomiaBinding
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@SingleIn(FragmentScope::class)
class GuidomiaFragment @Inject constructor() : Fragment() {

  @Inject
  internal lateinit var viewModelFactory: ViewModelFactory

  @Inject
  internal lateinit var adapter: GuidomiaRecyclerAdapter

  private val binding by viewBinding { FragmentGuidomiaBinding.inflate(layoutInflater) }

  internal val viewModel: GuidomiaViewModel by lazy {
    ViewModelProvider(this, viewModelFactory)
      .get(GuidomiaViewModel::class.java)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    lifecycleScope.launchWhenStarted {
      viewModel.uiState.collect { uiState ->
        when (uiState) {
          is CarListUiState.Success -> adapter.submitList(uiState.list)
          is CarListUiState.Error -> TODO()
        }
      }
    }
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    binding.rvGuidomia.adapter = adapter
    binding.rvGuidomia.layoutManager = LinearLayoutManager(context)
    return binding.root
  }
}
