package ru.netology.nmedia.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.databinding.FragmentEditBinding
import ru.netology.nmedia.dto.GeoPoint
import ru.netology.nmedia.viewmodel.GeoPointViewModel


class EditFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentEditBinding.inflate(
            inflater,
            container,
            false
        )

        val viewModel: GeoPointViewModel by activityViewModels()

        with(binding) {
            cancelButton.setOnClickListener {
                findNavController().popBackStack()
                viewModel.resetState()
            }

            saveButton.setOnClickListener {
                if (titleEdit.text.isNullOrBlank())
                    Toast.makeText(context, "Заголовок не может быть пустым", Toast.LENGTH_SHORT).show()
                else
                    viewModel.addGeoPoint(
                        GeoPoint(
                            lat = viewModel.editedPost.lat,
                            lng = viewModel.editedPost.lng,
                            title = binding.titleEdit.text.toString(),
                            description = binding.descriptionEdit.text.toString()
                        )
                    )
            }

            coordinatesLat.text = viewModel.editedPost.lat.toString()
            coordinatesLong.text = viewModel.editedPost.lng.toString()
            titleEdit.setText(viewModel.editedPost.title)
            descriptionEdit.setText(viewModel.editedPost.description)
        }

        viewModel.state.observe(viewLifecycleOwner) { state ->
            binding.titleEdit.isEnabled = !state.isProcessing
            binding.descriptionEdit.isEnabled = !state.isProcessing
            binding.cancelButton.isEnabled = !state.isProcessing
            binding.saveButton.isEnabled = !state.isProcessing
            binding.progressbar.isVisible = state.isProcessing
            if (state.isSaveError) {
                Toast.makeText(
                    context,
                    "Не удалось сохранить метку\n${state.errorMessage}",
                    Toast.LENGTH_LONG
                ).show()
            }
            if (state.isSaveOk) {
                findNavController().popBackStack()
                viewModel.resetState()
            }
        }

        return binding.root
    }
}