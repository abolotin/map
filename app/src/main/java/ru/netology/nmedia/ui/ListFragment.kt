package ru.netology.nmedia.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.adapters.GeoPointsAdapter
import ru.netology.nmedia.databinding.FragmentListBinding
import ru.netology.nmedia.viewmodel.GeoPointViewModel

class ListFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentListBinding.inflate(inflater, container, false)
        val viewModel : GeoPointViewModel by activityViewModels()

        val adapter = GeoPointsAdapter(onClickListener = { geoPoint ->
            viewModel.targetGeoPoint.postValue(geoPoint)
            findNavController().popBackStack()
        })
        binding.list.adapter = adapter
        viewModel.data.observe(viewLifecycleOwner) { geoPoints ->
            adapter.list = geoPoints
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().addMenuProvider(object: MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menu.clear()
                menuInflater.inflate(R.menu.list_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when(menuItem.itemId) {
                    R.id.menuViewMap -> {
                        findNavController().popBackStack()
                        true
                    }
                    else -> return false
                }
            }

        })
    }
}