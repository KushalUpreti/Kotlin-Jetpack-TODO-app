package com.example.myapplication.ui.tasks

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.data.SortOrder
import com.example.myapplication.data.TaskViewModel
import com.example.myapplication.databinding.FragmentTaskBinding
import com.example.myapplication.utils.OnQueryTextChanged
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TaskFragment : Fragment(R.layout.fragment_task) {
    private val viewModel: TaskViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentTaskBinding.bind(view)

        val taskAdapter = TaskAdapter()

        binding.apply {
            recyclerView.apply {
                adapter = taskAdapter
                layoutManager= LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }
        }
        viewModel.tasks.observe(viewLifecycleOwner,{
            taskAdapter.submitList(it)
        })
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_fragment_tasks,menu)
        val searchItem = menu.findItem(R.id.menu_search)
        val searchView = searchItem.actionView as SearchView

        searchView.OnQueryTextChanged {
            viewModel.searchQuery.value = it
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.menu_sort_by_name->{
                viewModel.sortOrder.value=SortOrder.BY_NAME
                true
            }
            R.id.menu_sort_by_date->{
                viewModel.sortOrder.value=SortOrder.BY_DATE
                true
            }
            R.id.menu_hide_completed->{
                item.isChecked = !item.isChecked
                viewModel.hideCompleted.value = item.isChecked
                true
            }
            else->super.onOptionsItemSelected(item)
        }
    }
}