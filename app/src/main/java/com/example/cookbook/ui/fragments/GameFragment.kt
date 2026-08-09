package com.example.cookbook.ui.fragments

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cookbook.R
import com.example.cookbook.databinding.FragmentGameBinding
import com.vungn.luckywheel.OnLuckyWheelReachTheTarget
import com.vungn.luckywheel.WheelItem
import com.vungn.luckywheel.WheelMode
import com.vungn.luckywheel.WheelUtils

class GameFragment : Fragment() {

    private var _binding: FragmentGameBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentGameBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createWheel()

    }

    private fun createWheel(){
        val lw = binding.lwv
        val wheelItems: List<WheelItem> = listOf(
            WheelItem(0, Color.RED, Color.WHITE, "A", 1),
            WheelItem(1, Color.BLUE, Color.WHITE, "B", 1),
            WheelItem(2, Color.GREEN, Color.WHITE, "C", 1),
            WheelItem(3, Color.YELLOW, Color.WHITE, "D", 1),
            WheelItem(4, Color.MAGENTA, Color.WHITE, "E", 1),
            WheelItem(5, Color.CYAN, Color.WHITE, "F", 1),
            WheelItem(6, Color.DKGRAY, Color.WHITE, "G", 1),
            WheelItem(7, Color.rgb(255, 128, 0), Color.WHITE, "H", 1),
            WheelItem(8, Color.rgb(128, 0, 255), Color.WHITE, "I", 1),
            WheelItem(9, Color.rgb(0, 128, 128), Color.WHITE, "J", 1),
            WheelItem(10, Color.rgb(255, 0, 128), Color.WHITE, "K", 1),
            WheelItem(11, Color.rgb(128, 128, 0), Color.WHITE, "L", 1),
            WheelItem(12, Color.rgb(0, 128, 255), Color.WHITE, "M", 1),
            WheelItem(13, Color.rgb(255, 64, 64), Color.WHITE, "N", 1),
            WheelItem(14, Color.rgb(64, 64, 255), Color.WHITE, "O", 1),
            WheelItem(15, Color.rgb(64, 200, 64), Color.WHITE, "P", 1),
            WheelItem(16, Color.rgb(200, 64, 200), Color.WHITE, "Q", 1),
            WheelItem(17, Color.rgb(200, 128, 64), Color.WHITE, "R", 1),
            WheelItem(18, Color.rgb(64, 160, 160), Color.WHITE, "S", 1),
            WheelItem(19, Color.rgb(160, 64, 160), Color.WHITE, "T", 1),
            WheelItem(20, Color.rgb(64, 100, 200), Color.WHITE, "U", 1),
            WheelItem(21, Color.rgb(200, 64, 100), Color.WHITE, "V", 1),
            WheelItem(22, Color.rgb(100, 200, 64), Color.WHITE, "W", 1),
            WheelItem(23, Color.rgb(100, 64, 200), Color.WHITE, "X", 1),
            WheelItem(24, Color.rgb(200, 100, 64), Color.WHITE, "Y", 1),
            WheelItem(25, Color.rgb(64, 200, 128), Color.WHITE, "Z", 1),
            WheelItem(26, Color.rgb(128, 64, 64), Color.WHITE, "A", 1),
            WheelItem(27, Color.rgb(64, 128, 64), Color.WHITE, "B", 1),
            WheelItem(28, Color.rgb(64, 64, 128), Color.WHITE, "C", 1)
        )

        lw.setWheelMode(WheelMode.NORMAL)

        lw.addWheelItems(wheelItems)

        lw.setLuckyWheelReachTheTarget(object: OnLuckyWheelReachTheTarget{
            override fun onReachFinalTarget(p0: WheelItem?) {
               val letter = p0?.text
            }

            override fun onTargetChanged(p0: WheelItem?) {

            }

        })

        binding.spin.setOnClickListener {
            val randomNum = WheelUtils.getRandomIndex(wheelItems)

            lw.rotateWheelTo(randomNum)
        }


    }

}