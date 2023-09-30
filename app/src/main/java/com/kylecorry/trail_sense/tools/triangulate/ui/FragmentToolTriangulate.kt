package com.kylecorry.trail_sense.tools.triangulate.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.kylecorry.andromeda.alerts.Alerts
import com.kylecorry.andromeda.core.system.GeoUri
import com.kylecorry.andromeda.fragments.BoundFragment
import com.kylecorry.sol.science.geology.Geology
import com.kylecorry.sol.units.Bearing
import com.kylecorry.sol.units.Coordinate
import com.kylecorry.trail_sense.R
import com.kylecorry.trail_sense.databinding.FragmentToolTriangulateBinding
import com.kylecorry.trail_sense.navigation.infrastructure.share.LocationCopy
import com.kylecorry.trail_sense.shared.AppUtils
import com.kylecorry.trail_sense.shared.FormatService
import com.kylecorry.trail_sense.shared.UserPreferences

class FragmentToolTriangulate : BoundFragment<FragmentToolTriangulateBinding>() {

    private val formatService by lazy { FormatService.getInstance(requireContext()) }
    private val prefs by lazy { UserPreferences(requireContext()) }

    private var direction1: Bearing? = null
    private var trueNorth1: Boolean = false
    private var trueNorth2: Boolean = false
    private var direction2: Bearing? = null
    private var location: Coordinate? = null

    private var shouldCalculateMyLocation = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.bearing1.setOnBearingChangeListener { bearing, isTrueNorth ->
            direction1 = bearing
            trueNorth1 = isTrueNorth
            update()
        }

        binding.bearing2.setOnBearingChangeListener { bearing, isTrueNorth ->
            direction2 = bearing
            trueNorth2 = isTrueNorth
            update()
        }


        binding.triangulateTitle.rightButton.setOnClickListener {
            location?.let {
                val share = LocationCopy(requireContext())
                share.send(it)
            }
        }

        binding.createBeacon.setOnClickListener {
            location?.let {
                AppUtils.placeBeacon(requireContext(), GeoUri(it))
            }
        }

        binding.updateGpsOverride.setOnClickListener {
            location?.let { coord ->
                prefs.locationOverride = coord
                Alerts.toast(requireContext(), getString(R.string.location_override_updated))
            }
        }

        if (prefs.useAutoLocation) {
            binding.updateGpsOverride.isVisible = false
        }

        binding.locationButtonGroup.check(if (shouldCalculateMyLocation) binding.locationButtonSelf.id else binding.locationButtonOther.id)
        binding.locationButtonGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (!isChecked) {
                return@addOnButtonCheckedListener
            }
            shouldCalculateMyLocation = checkedId == binding.locationButtonSelf.id
            update()
        }

        // Handle expansion
        binding.location1Expansion.setOnExpandStateChangedListener {
            // TODO: Set the dropdown icon
        }

        binding.location2Expansion.setOnExpandStateChangedListener {
            // TODO: Set the dropdown icon
        }

        // Expand the first location by default (this will change once it loads the last recorded values)
        binding.location1Expansion.expand()

        // TODO: Add an status indicator for the location
        // TODO: Display the distance to the location in the title
    }

    override fun onResume() {
        super.onResume()
        binding.bearing1.start()
        binding.bearing2.start()
    }

    override fun onPause() {
        super.onPause()
        binding.bearing1.stop()
        binding.bearing2.stop()
    }

    private fun update() {
        if (!isBound) {
            return
        }

        val location1 = binding.location1.coordinate
        val location2 = binding.location2.coordinate
        val direction1 = direction1
        val direction2 = direction2

        if (location1 == null || location2 == null || direction1 == null || direction2 == null) {
            setLocation(null)
            return
        }

        // All information is available to triangulate
        val declination1 = if (trueNorth1) 0f else Geology.getGeomagneticDeclination(location1)
        val declination2 = if (trueNorth2) 0f else Geology.getGeomagneticDeclination(location2)
        val bearing1 = direction1.withDeclination(declination1)
        val bearing2 = direction2.withDeclination(declination2)

        val location = if (shouldCalculateMyLocation) {
            Geology.triangulateSelf(location1, bearing1, location2, bearing2)
        } else {
            Geology.triangulateDestination(location1, bearing1, location2, bearing2)
        }
        setLocation(location)
    }

    private fun setLocation(location: Coordinate?) {
        this.location = location
        if (location == null || location.latitude.isNaN() || location.longitude.isNaN()) {
            binding.triangulateTitle.title.text = getString(R.string.could_not_triangulate)
            binding.triangulateTitle.rightButton.isInvisible = true
            binding.actions.isVisible = false
        } else {
            binding.triangulateTitle.title.text = formatService.formatLocation(location)
            binding.triangulateTitle.rightButton.isInvisible = false
            binding.actions.isVisible = true
        }
    }

    override fun generateBinding(
        layoutInflater: LayoutInflater, container: ViewGroup?
    ): FragmentToolTriangulateBinding {
        return FragmentToolTriangulateBinding.inflate(layoutInflater, container, false)
    }
}