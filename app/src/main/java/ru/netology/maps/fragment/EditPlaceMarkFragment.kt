package ru.netology.maps.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.yandex.mapkit.geometry.Point
import ru.netology.maps.databinding.FragmentEditPlaceMarkBinding
import ru.netology.maps.dto.PlaceMark
import ru.netology.maps.viewmodel.PlaceMarkViewModel


class EditPlaceMarkFragment : Fragment() {
    private lateinit var binding: FragmentEditPlaceMarkBinding
    private val viewModel: PlaceMarkViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditPlaceMarkBinding.inflate(inflater, container, false)

        val text = arguments?.getString("name")

        if (text != null) {
            binding.content.setText(text)
        }

        binding.saveButton.setOnClickListener {
            viewModel.savePoint(
                PlaceMark(
                    0,
                    Point(),
                    binding.content.text.toString()
                )
            )
            findNavController().navigateUp()
        }

        return binding.root
    }


}