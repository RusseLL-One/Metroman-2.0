package com.one.russell.metroman_20.presentation.screens.training.training_type_selection

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.one.russell.metroman_20.R
import com.one.russell.metroman_20.databinding.FragmentTrainingTypeSelectionBinding
import com.one.russell.metroman_20.domain.TrainingFinalType
import com.one.russell.metroman_20.domain.TrainingTopLevelType
import com.one.russell.metroman_20.presentation.screens.training.training_type_selection.adapter.TrainingTypesAdapter
import com.one.russell.metroman_20.presentation.views.utils.createPaddingsDecoration
import com.one.russell.metroman_20.presentation.views.utils.disableScrolling
import com.one.russell.metroman_20.toPx
import org.koin.androidx.viewmodel.ext.android.viewModel

class TrainingTypeSelectionFragment : Fragment(R.layout.fragment_training_type_selection) {

    private val binding by viewBinding(FragmentTrainingTypeSelectionBinding::bind)
    private val viewModel: TrainingTypeSelectionViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.root.setBackgroundColor(viewModel.colors.backgroundColor)

        binding.rvList.disableScrolling()
        binding.rvList.addItemDecoration(createPaddingsDecoration(verticalPadding = 16.toPx()))
        binding.rvList.adapter = TrainingTypesAdapter {
            navigateNext(it.type)
        }.apply {
            items = viewModel.getTrainingTypeItems()
        }
    }

    private fun navigateNext(selectedType: TrainingTopLevelType) {
        when (selectedType) {
            TrainingTopLevelType.TEMPO_INCREASING -> TrainingTypeSelectionFragmentDirections.actionTypeSelectionFragmentToSubtypeSelectionFragment(
                TrainingTopLevelType.TEMPO_INCREASING
            )
            TrainingTopLevelType.BAR_DROPPING -> TrainingTypeSelectionFragmentDirections.actionTypeSelectionFragmentToSubtypeSelectionFragment(
                TrainingTopLevelType.BAR_DROPPING
            )
            TrainingTopLevelType.BEAT_DROPPING -> TrainingTypeSelectionFragmentDirections.actionTypeSelectionFragmentToOptionsFragment(
                TrainingFinalType.BEAT_DROPPING
            )
        }.let { findNavController().navigate(it) }
    }
}