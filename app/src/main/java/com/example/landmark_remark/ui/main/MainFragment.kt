package com.example.landmark_remark.ui.main

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.afollestad.assent.Permission
import com.afollestad.assent.runWithPermissions
import com.example.landmark_remark.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.main_fragment.*




class MainFragment(private val username: String) : Fragment() {

    companion object {
        fun newInstance(username: String) = MainFragment(username)
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var mapFragment: SupportMapFragment

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapFragment = childFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync { googleMap ->
            onMapReady(googleMap)
        }

        add_note_button.setOnClickListener {
            // Add a note (at users location) clicked.
            viewModel.addNotePressed()
        }

        toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }

        setupSearchView()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        observeViewModel()
    }


    @SuppressLint("MissingPermission")
    private fun onMapReady(googleMap: GoogleMap) {
        activity?.runWithPermissions(Permission.ACCESS_FINE_LOCATION) {
            // Only runs if we have location permissions (requests permission if we do not already have it)
            googleMap.isMyLocationEnabled = true
            viewModel.mapAndPermissionsReady(username)
        }

        googleMap.setOnMapLongClickListener { clickedLatLng ->
            // User has long clicked a location on the map.
            // Ask the user to enter a note for this location.
            displayEnterNoteDialog(clickedLatLng)
        }
    }

    private fun observeViewModel() {

        viewModel.moveCamera.observe(viewLifecycleOwner, { cameraUpdate ->
            // Move camera update received
            mapFragment.getMapAsync { googleMap ->
                googleMap.moveCamera(cameraUpdate)
            }
        })

        viewModel.displayEnterNoteDialog.observe(viewLifecycleOwner, { latLng ->
            displayEnterNoteDialog(latLng)
        })

        viewModel.displayAddNoteResult.observe(viewLifecycleOwner, { messageResource ->
            Toast.makeText(requireContext(), getString(messageResource), Toast.LENGTH_SHORT).show()
        })

        viewModel.displayNewMarker.observe(viewLifecycleOwner, { newNote ->
            // Add single new note marker to map.
            mapFragment.getMapAsync { googleMap ->
                googleMap.addMarker(MarkerOptions()
                    .position(newNote.coordinates)
                    .title(getString(R.string.username_note_title, newNote.username))
                    .snippet(newNote.message)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)))
            }
        })

        viewModel.displayUpdatedMarkers.observe(viewLifecycleOwner, { noteList ->
            // New note list received, clear current map markers and display new markers.
            mapFragment.getMapAsync { googleMap ->
                googleMap.clear()
                noteList.forEach { note ->
                    googleMap.addMarker(MarkerOptions()
                        .position(note.coordinates)
                        .title(getString(R.string.username_note_title, note.username))
                        .snippet(note.message)
                        .icon(BitmapDescriptorFactory.defaultMarker(getMarkerColour(note.username))))
                }
            }
        })
    }

    private fun setupSearchView() {
        search_view.apply {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    viewModel.updateSearchQuery(query)
                    return false
                }

                override fun onQueryTextChange(query: String): Boolean {
                    // Update the searchQuery value if the text changes.
                    viewModel.updateSearchQuery(query)
                    return false
                }
            })

            setOnCloseListener {
                clearFocus()
                true
            }

            isIconified = false
            queryHint = getString(R.string.search_here)
            clearFocus()
        }
    }

    // Display AlertDialog with edit-text so the user can enter a note message.
    private fun displayEnterNoteDialog(location: LatLng) {
        val noteTextField = EditText(requireContext())
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.add_note))
            .setMessage(getString(R.string.please_enter_a_note))
            .setView(noteTextField)
            .setPositiveButton(R.string.done) { _, _ ->
                // Add new note using entered text and location provided
                viewModel.addNewNote(noteTextField.text.toString(), location)
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    private fun getMarkerColour(noteUser: String): Float {
        // If the note username matches the current username
        // We want to display the Marker a different colour (Cyan).
        return if (username.trim() == noteUser.trim()) { BitmapDescriptorFactory.HUE_CYAN }
        else { BitmapDescriptorFactory.HUE_RED }
    }
}