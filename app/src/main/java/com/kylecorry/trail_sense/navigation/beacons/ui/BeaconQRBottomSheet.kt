package com.kylecorry.trail_sense.navigation.beacons.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kylecorry.andromeda.core.system.Resources
import com.kylecorry.andromeda.fragments.BoundBottomSheetDialogFragment
import com.kylecorry.andromeda.qr.QR
import com.kylecorry.trail_sense.databinding.FragmentBeaconQrShareBinding
import com.kylecorry.trail_sense.navigation.beacons.domain.Beacon
import com.kylecorry.trail_sense.navigation.beacons.infrastructure.share.BeaconGeoUri

class BeaconQRBottomSheet : BoundBottomSheetDialogFragment<FragmentBeaconQrShareBinding>() {

    var beacon: Beacon? = null
        set(value) {
            field = value
            updateUI()
        }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateUI()
    }

    private fun updateUI(){
        val beacon = this.beacon ?: return
        if (!isBound){
            return
        }
        binding.beaconName.text = beacon.name
        val encoder = BeaconGeoUri()
        val encoded = encoder.encode(beacon).toString()
        val size = Resources.dp(requireContext(), 250f).toInt()
        val bitmap = QR.encode(encoded, size, size)
        binding.beaconQr.setImageBitmap(bitmap)
    }


    override fun generateBinding(
        layoutInflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentBeaconQrShareBinding {
        return FragmentBeaconQrShareBinding.inflate(layoutInflater, container, false)
    }

}