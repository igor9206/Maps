package ru.netology.maps.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.maps.R
import ru.netology.maps.adapter.OnInteractionListener
import ru.netology.maps.adapter.PlaceMarkAdapter
import ru.netology.maps.databinding.FragmentPlaceMarkBinding
import ru.netology.maps.dto.PlaceMark
import ru.netology.maps.viewmodel.PlaceMarkViewModel

class PlaceMarkFragment : Fragment() {
    private lateinit var binding: FragmentPlaceMarkBinding
    private val viewModel: PlaceMarkViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaceMarkBinding.inflate(inflater, container, false)

        val adapter = PlaceMarkAdapter(object : OnInteractionListener {
            override fun remove(placeMark: PlaceMark) {
                viewModel.delete(placeMark)
            }

            override fun edit(placeMark: PlaceMark) {
                viewModel.edit(placeMark.id)
                findNavController().navigate(
                    R.id.action_placeMarkFragment_to_editPlaceMarkFragment,
                    bundleOf("name" to placeMark.name)
                )
            }

            override fun openCardPost(placeMark: PlaceMark) {
            }
        })

        binding.recyclerView.adapter = adapter

        viewModel.data.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        return binding.root
    }


}