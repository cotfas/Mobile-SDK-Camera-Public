package io.digitalbinary.camera.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.digitalbinary.camera.R
import io.digitalbinary.camera.adapter.ImageAdapter
import io.digitalbinary.camera.databinding.FragmentSecondBinding
import io.digitalbinary.camera.sdk.PhotoSDK
import io.digitalbinary.camera.sdk.impl.PhotoSDKImpl

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var sdk: PhotoSDK


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)

        // Initialise SDK
        sdk = PhotoSDKImpl(activity)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSecond.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }

        // Initialise Gallery list
        initialiseList(view)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()

        Toast.makeText(activity, "Refreshing UI", Toast.LENGTH_SHORT).show()

        // notifyDataSetChanged
        view?.let { initialiseList(it) }
    }

    private fun initialiseList(rootView: View) {
        val recyclerView: RecyclerView = rootView.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        val adapter = ImageAdapter(sdk.accessPhotos().reversed())
        recyclerView.adapter = adapter
    }
}