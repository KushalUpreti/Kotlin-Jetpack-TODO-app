package com.example.myapplication.ui.addedittask

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentAddEditBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddEditFragment : Fragment(R.layout.fragment_add_edit) {
    private val viewModel: AddEditViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentAddEditBinding.bind(view)
        binding.apply{
            editTextPersonName.setText(viewModel.taskName)
            addEditCheckBox.isChecked = viewModel.takImportance
            addEditCheckBox.jumpDrawablesToCurrentState()
            dateCreated.isVisible = viewModel.taskName != null
            dateCreated.text = "Created: ${viewModel.task?.createdDateFormatted}"
        }
    }

}