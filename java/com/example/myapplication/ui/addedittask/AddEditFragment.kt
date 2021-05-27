package com.example.myapplication.ui.addedittask

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentAddEditBinding
import com.example.myapplication.utils.exhaustive
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_add_edit.*
import kotlinx.android.synthetic.main.fragment_task.*
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class AddEditFragment : Fragment(R.layout.fragment_add_edit) {
    private val viewModel: AddEditViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentAddEditBinding.bind(view)
        binding.apply {
            editTextTaskName.setText(viewModel.taskName)
            addEditCheckBox.isChecked = viewModel.takImportance
            addEditCheckBox.jumpDrawablesToCurrentState()
            dateCreated.isVisible = viewModel.task != null
            dateCreated.text = "Created: ${viewModel.task?.createdDateFormatted}"

            editButton.setOnClickListener {
                viewModel.onSaveClick()
            }
        }

        editTextTaskName.addTextChangedListener {
            viewModel.taskName = it.toString()
        }

        addEditCheckBox.setOnCheckedChangeListener { _, isChecked ->
            viewModel.takImportance = isChecked
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.addEditTaskEvent.collect(){event->
                when(event){
                    is AddEditViewModel.AddEditTaskEvent.ShowInvalidInputMessage->{
                        Snackbar.make(requireView(),event.message,Snackbar.LENGTH_LONG).show()
                    }
                    is AddEditViewModel.AddEditTaskEvent.NavigateBackWithResult->{
                        binding.editTextTaskName.clearFocus()
                        setFragmentResult(
                            "add_edit_request",
                            bundleOf("add_edit_result" to event.result)
                        )
                        findNavController().popBackStack()
                    }
                }
            }.exhaustive
        }
    }
}