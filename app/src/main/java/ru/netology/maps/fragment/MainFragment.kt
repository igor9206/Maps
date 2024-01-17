package ru.netology.maps.fragment

import android.graphics.PointF
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKit
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.layers.ObjectEvent
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.InputListener
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.mapkit.map.TextStyle
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.search.Response
import com.yandex.mapkit.search.SearchFactory
import com.yandex.mapkit.search.SearchManager
import com.yandex.mapkit.search.SearchManagerType
import com.yandex.mapkit.search.SearchOptions
import com.yandex.mapkit.search.SearchType
import com.yandex.mapkit.search.Session.SearchListener
import com.yandex.mapkit.user_location.UserLocationLayer
import com.yandex.mapkit.user_location.UserLocationObjectListener
import com.yandex.mapkit.user_location.UserLocationView
import com.yandex.runtime.Error
import com.yandex.runtime.image.ImageProvider
import ru.netology.maps.R
import ru.netology.maps.databinding.FragmentMainBinding
import ru.netology.maps.dto.PlaceMark
import ru.netology.maps.viewmodel.PlaceMarkViewModel

class MainFragment : Fragment(), UserLocationObjectListener {
    private lateinit var binding: FragmentMainBinding
    private lateinit var mapView: MapView
    private lateinit var userLocationLayer: UserLocationLayer
    private lateinit var mapKit: MapKit
    private lateinit var map: Map
    private lateinit var placeMarkMapObject: PlacemarkMapObject
    private lateinit var imageProvider: ImageProvider
    private lateinit var searchManager: SearchManager
    private lateinit var searchOptions: SearchOptions

    private val viewModel: PlaceMarkViewModel by activityViewModels()

    private val inputListener = object : InputListener {
        override fun onMapLongTap(map: Map, point: Point) {
            // Create placeMark after long tap
            placeMarkMapObject.geometry = point
            placeMarkMapObject.setIcon(imageProvider)
            placeMarkMapObject.isVisible = true
            searchPoint(point)
            binding.latitude.text = point.latitude.toString()
            binding.longitude.text = point.longitude.toString()
            binding.savePlaceMarkCard.isVisible = true
        }

        override fun onMapTap(map: Map, point: Point) = Unit
    }

    private val searchSessionListener = object : SearchListener {
        override fun onSearchResponse(response: Response) {
            val text = when {
                response.collection.children.firstOrNull()?.obj?.descriptionText != null -> response.collection.children.firstOrNull()?.obj?.descriptionText
                response.metadata.toponym?.name != null -> response.metadata.toponym?.name
                else -> {
                    "Unknown object"
                }
            }

            placeMarkMapObject.setText(
                text.toString(), TextStyle().apply {
                    placement = TextStyle.Placement.BOTTOM
                }
            )
            binding.name.text = text
        }

        override fun onSearchError(error: Error) {
            println(error.toString())
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)

        mapView = binding.mapView
        mapKit = MapKitFactory.getInstance()
        userLocationLayer = mapKit.createUserLocationLayer(mapView.mapWindow)
        map = mapView.map
        imageProvider = ImageProvider.fromResource(requireContext(), R.drawable.ic_add_location_24)
        placeMarkMapObject = mapView.map.mapObjects.addPlacemark()

        mapView.map.move(
            CameraPosition(Point(), 14f, 0f, 0f),
            Animation(Animation.Type.SMOOTH, 2f),
            Map.CameraCallback { }
        )

        map.addInputListener(inputListener)


        searchManager =
            SearchFactory.getInstance().createSearchManager(SearchManagerType.COMBINED)
        searchOptions = SearchOptions().apply {
            searchTypes = SearchType.BIZ.value
            resultPageSize = 1
        }


        mapKit.resetLocationManagerToDefault()
        userLocationLayer.isVisible = true
        userLocationLayer.setObjectListener(this)


        binding.closeButton.setOnClickListener {
            closePlaceMarkCard()
        }

        binding.saveButton.setOnClickListener {
            viewModel.savePoint(
                PlaceMark(
                    0,
                    Point(
                        binding.latitude.text.toString().toDouble(),
                        binding.longitude.text.toString().toDouble()
                    ),
                    binding.name.text.toString()
                )
            )
            closePlaceMarkCard()
        }

        binding.favorite.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_placeMarkFragment)
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        mapView.onStart()
    }

    override fun onStop() {
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    override fun onObjectAdded(userLocationView: UserLocationView) {
        userLocationLayer.setAnchor(
            PointF((mapView.width * 0.5).toFloat(), (mapView.height * 0.5).toFloat()),
            PointF((mapView.width * 0.5).toFloat(), (mapView.height * 0.83).toFloat())
        )
    }

    override fun onObjectRemoved(p0: UserLocationView) {
    }

    override fun onObjectUpdated(p0: UserLocationView, p1: ObjectEvent) {
    }

    private fun searchPoint(point: Point) {
        searchManager.submit(
            point,
            14,
            searchOptions,
            searchSessionListener
        )
    }

    private fun closePlaceMarkCard() {
        binding.savePlaceMarkCard.isVisible = false
        placeMarkMapObject.setText("")
        binding.name.text = ""
        binding.longitude.text = ""
        binding.latitude.text = ""
        placeMarkMapObject.isVisible = false
    }

}