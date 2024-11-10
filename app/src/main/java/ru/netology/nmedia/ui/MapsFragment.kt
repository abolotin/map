package ru.netology.nmedia.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.InputListener
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.nmedia.R
import ru.netology.nmedia.dto.GeoPoint
import ru.netology.nmedia.viewmodel.GeoPointViewModel

@AndroidEntryPoint
class MapsFragment : Fragment() {
    private lateinit var mapView: MapView
    private lateinit var imageProvider: ImageProvider
    private val viewModel: GeoPointViewModel by activityViewModels()

    private val cameraCallback = Map.CameraCallback { Unit }

    private val mapInputListener = object : InputListener {
        override fun onMapTap(m: Map, p: Point) {
            AlertDialog.Builder(requireContext())
                .setTitle("Точка на карте")
                .setMessage("Координаты: ${p.longitude}, ${p.latitude}")
                .setPositiveButton("Добавить метку") { _, _ ->
                    viewModel.editedPost = GeoPoint(
                        lat = p.latitude,
                        lng = p.longitude,
                        title = "Новая метка"
                    )
                    findNavController().navigate(
                        R.id.action_mapsFragment_to_editFragment
                    )
                }
                .setNeutralButton("Закрыть", null)
                .show()
        }

        override fun onMapLongTap(m: Map, p: Point) {
        }
    }

    private val placemarkTapListener = MapObjectTapListener { mapObject, point ->
        val placemark = mapObject as PlacemarkMapObject
        val geoPoint = placemark.userData as GeoPoint
        AlertDialog.Builder(requireContext())
            .setTitle(geoPoint.title)
            .setMessage("${geoPoint.description}\n\nКоординаты: ${geoPoint.lat}, ${geoPoint.lng}")
            .setPositiveButton("Перейти") { _, _ ->
                viewModel.targetGeoPoint.postValue(geoPoint)
            }
            .setNegativeButton("Удалить") { _, _ ->
                val point = (placemark as PlacemarkMapObject).geometry
                viewModel.removeGeoPoint(GeoPoint(lat = point.latitude, lng = point.longitude))
            }
            .setNeutralButton("Изменить") { _, _ ->
                viewModel.editedPost = geoPoint
                findNavController().navigate(
                    R.id.action_mapsFragment_to_editFragment
                )
            }
            .show()
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        imageProvider = ImageProvider.fromBitmap(
            getDrawable(
                requireContext(),
                R.drawable.ic_netology_48dp
            )?.toBitmap()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.yandex_maps, container, false)
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        MapKitFactory.getInstance().onStop()
        mapView.onStop()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView = view.findViewById(R.id.mapview)
        mapView.mapWindow.map.addInputListener(mapInputListener)

        viewModel.data.observe(viewLifecycleOwner) { geoPoints ->
            mapView.mapWindow.map.mapObjects.clear()
            for (geoPoint in geoPoints)
                mapView.mapWindow.map.mapObjects.addPlacemark().apply {
                    geometry = Point(geoPoint.lat, geoPoint.lng)
                    userData = geoPoint
                    setIcon(imageProvider)
                    addTapListener(placemarkTapListener)
                }
        }

        viewModel.state.observe(viewLifecycleOwner) { state ->
            if (state.isSaveError) {
                Toast.makeText(
                    context,
                    "Не удалось удалить метку\n${state.errorMessage}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        viewModel.targetGeoPoint.observe(viewLifecycleOwner) { geoPoint ->
            if (geoPoint != null) {
                mapView.mapWindow.map.move(
                    CameraPosition(
                        /* target = */ Point(geoPoint.lat, geoPoint.lng),
                        /* zoom = */ 15.0f,
                        /* azimuth = */ 0.0f,
                        /* tilt = */ 0.0f
                    ),
                    Animation(Animation.Type.LINEAR, 0.5f),
                    cameraCallback
                )
            }
        }

        requireActivity().addMenuProvider(object: MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menu.clear()
                menuInflater.inflate(R.menu.map_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when(menuItem.itemId) {
                    R.id.menuViewList -> {
                        findNavController().navigate(
                            R.id.action_mapsFragment_to_listFragment
                        )
                        true
                    }
                    else -> return false
                }
            }
        })
    }
}