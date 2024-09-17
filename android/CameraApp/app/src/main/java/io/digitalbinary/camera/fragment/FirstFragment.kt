package io.digitalbinary.camera.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import io.digitalbinary.camera.R
import io.digitalbinary.camera.databinding.FragmentFirstBinding
import io.digitalbinary.camera.sdk.PhotoSDK
import io.digitalbinary.camera.sdk.impl.PhotoSDKImpl
import io.digitalbinary.camera.sdk.messages.MessageEvent
import io.digitalbinary.camera.sdk.messages.SuccessEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var sdk: PhotoSDK

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)

        sdk = PhotoSDKImpl(activity)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFirst.setOnClickListener {
            if (sdk.accessPhotos().size > 0) {
                findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
            }
        }

        binding.buttonAuthenticate.setOnClickListener {
            sdk.authenticateUser()
        }
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: SuccessEvent?) {
        Toast.makeText(context, event!!.name, Toast.LENGTH_SHORT).show()
        findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: MessageEvent?) {
        Toast.makeText(context, event!!.name, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}