package com.example.habitapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.habitapp.R
import com.example.habitapp.viewmodel.StatsViewModel

class StatsFragment : Fragment() {
    private lateinit var viewModel: StatsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_stats, container, false)

        val header = view.findViewById<View>(R.id.header_stats)
        val headerTitle = header.findViewById<TextView>(R.id.tv_header_title)
        val headerSubtitle = header.findViewById<TextView>(R.id.tv_header_subtitle)

        ViewCompat.setOnApplyWindowInsetsListener(header) { v, insets ->
            val statusBarHeight = insets.getInsets(WindowInsetsCompat.Type.systemBars()).top
            // Convert 24dp to pixels
            val extraTop = (24 * resources.displayMetrics.density).toInt()
            v.setPadding(
                v.paddingLeft,
                statusBarHeight + extraTop,
                v.paddingRight,
                v.paddingBottom
            )
            insets
        }

        headerTitle.text = "Estadísticas"
        headerSubtitle.text = "Tu progreso esta semana"

        viewModel = ViewModelProvider(requireActivity())[StatsViewModel::class.java]

        val box1 = view.findViewById<View>(R.id.stat_box_1)
        val box2 = view.findViewById<View>(R.id.stat_box_2)
        val box3 = view.findViewById<View>(R.id.stat_box_3)
        val box4 = view.findViewById<View>(R.id.stat_box_4)

        val box1Value = box1.findViewById<TextView>(R.id.tv_stat_value)
        val box2Value = box2.findViewById<TextView>(R.id.tv_stat_value)
        val box3Value = box3.findViewById<TextView>(R.id.tv_stat_value)
        val box4Value = box4.findViewById<TextView>(R.id.tv_stat_value)

        viewModel.stats.observe(viewLifecycleOwner) { stats ->
            box1Value.text = stats.completedTasks.toString()
            box2Value.text = stats.habitsDone.toString()
            box3Value.text = "${stats.streak} días"
            box4Value.text = "${stats.totalProgressPercent}%"
        }

        return view
    }
}
