package com.kylecorry.trail_sense.navigation.beacons.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.kylecorry.andromeda.fragments.BoundFragment
import com.kylecorry.sol.units.Distance
import com.kylecorry.trail_sense.R
import com.kylecorry.trail_sense.databinding.FragmentBeaconDetailsBinding
import com.kylecorry.trail_sense.navigation.beacons.domain.Beacon
import com.kylecorry.trail_sense.navigation.beacons.infrastructure.persistence.BeaconRepo
import com.kylecorry.trail_sense.navigation.infrastructure.share.LocationGeoSender
import com.kylecorry.trail_sense.shared.CustomUiUtils
import com.kylecorry.trail_sense.shared.FormatService
import com.kylecorry.trail_sense.shared.Units
import com.kylecorry.trail_sense.shared.UserPreferences
import com.kylecorry.trail_sense.tools.qr.infrastructure.BeaconQREncoder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BeaconDetailsFragment : BoundFragment<FragmentBeaconDetailsBinding>() {

    private val beaconRepo by lazy { BeaconRepo.getInstance(requireContext()) }
    private val formatService by lazy { FormatService(requireContext()) }
    private val prefs by lazy { UserPreferences(requireContext()) }

    private var beacon: Beacon? = null
    private var beaconId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        beaconId = requireArguments().getLong("beacon_id")
    }

    private fun loadBeacon(id: Long) {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                beacon = beaconRepo.getBeacon(id)?.toBeacon()
            }

            withContext(Dispatchers.Main) {
                beacon?.apply {
                    binding.beaconName.text = this.name
                    binding.locationText.text = formatService.formatLocation(this.coordinate)

                    if (this.elevation != null) {
                        val d = Distance.meters(this.elevation).convertTo(prefs.baseDistanceUnits)
                        binding.altitudeText.text =
                            formatService.formatDistance(d, Units.getDecimalPlaces(d.units), false)
                    } else {
                        binding.altitudeText.visibility = View.GONE
                        binding.altitudeIcon.visibility = View.GONE
                    }

                    if (!this.comment.isNullOrEmpty()) {
                        binding.commentText.text = this.comment
                    } else {
                        binding.commentText.visibility = View.GONE
                        binding.commentIcon.visibility = View.GONE
                    }

                    binding.navigateBtn.setOnClickListener {
                        val bundle = bundleOf("destination" to (beacon?.id ?: 0L))
                        findNavController().navigate(
                            R.id.action_beaconDetailsFragment_to_action_navigation,
                            bundle
                        )
                    }

                    binding.beaconQrBtn.setOnClickListener {
                        CustomUiUtils.showQR(
                            this@BeaconDetailsFragment,
                            name,
                            BeaconQREncoder().encode(this)
                        )
                    }

                    binding.beaconMapBtn.setOnClickListener {
                        val sender = LocationGeoSender(requireContext())
                        sender.send(this.coordinate)
                    }

                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (beaconId != null) {
            loadBeacon(beaconId!!)
        }
    }

    override fun generateBinding(
        layoutInflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentBeaconDetailsBinding {
        return FragmentBeaconDetailsBinding.inflate(layoutInflater, container, false)
    }
}

