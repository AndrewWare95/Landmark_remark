package com.example.landmark_remark.ui.main

import android.annotation.SuppressLint
import android.app.Application
import android.database.Cursor
import androidx.annotation.StringRes
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.landmark_remark.DatabaseManager
import com.example.landmark_remark.R
import com.example.landmark_remark.DatabaseManager.Companion.KEY_LATITUDE
import com.example.landmark_remark.DatabaseManager.Companion.KEY_LONGITUDE
import com.example.landmark_remark.DatabaseManager.Companion.KEY_MESSAGE
import com.example.landmark_remark.DatabaseManager.Companion.KEY_NAME
import com.example.landmark_remark.LocationService
import com.example.landmark_remark.Note
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng

class MainViewModel(application: Application) : AndroidViewModel(application) {

    val moveCamera = MutableLiveData<CameraUpdate>()
    val displayEnterNoteDialog = MutableLiveData<LatLng>()
    val displayAddNoteResult = MutableLiveData<@StringRes Int>()
    val displayUpdatedMarkers = MutableLiveData<List<Note>>()
    val displayNewMarker = MutableLiveData<Note>()

    private lateinit var username: String
    private var searchQuery: String = ""
    private val locationService: LocationService = LocationService(application)
    private val database = DatabaseManager(application)

    fun mapAndPermissionsReady(username: String) {
        this.username = username

        // Use location service to request the users current location.
        locationService.getLastKnownLocation().addOnSuccessListener { location ->
            // Post cameraUpdate value
            moveCamera.postValue(CameraUpdateFactory.newLatLngZoom(LatLng(location.latitude, location.longitude), 14.5f))
        }

        getNotesFromDatabase()
    }

    fun addNotePressed() {
        // Use location service to request the users current location.
        locationService.getLastKnownLocation().addOnSuccessListener { location ->
            displayEnterNoteDialog.postValue(LatLng(location.latitude, location.longitude))
        }
    }

    fun addNewNote(noteMessage: String, location: LatLng) {
        // Create note and add to database.
        val note = Note(username, noteMessage, location)
        val status = database.addNote(note)

        // Post a stringRes to display a toast, informing the use if adding a note was a success or not.
        if (status > -1) {
            displayAddNoteResult.postValue(R.string.note_added_successfully)
            displayNewMarker.postValue(note)
//            getNotesFromDatabase()
        } else {
            displayAddNoteResult.postValue(R.string.error_adding_note)
        }
    }

    fun updateSearchQuery(searchQuery: String) {
        // Update the searchQuery value in the viewModel
        // Get notes from the database. (this will update the results).
        this.searchQuery = searchQuery
        getNotesFromDatabase()
    }

    private fun getNotesFromDatabase() {
        // If we have nothing in searchQuery, get all notes.
        // If we have any text in the search query, get the filtered notes.

        val cursor =
            if (searchQuery.isEmpty()) { database.getAllNotes() }
            else { database.getFilteredNotes(searchQuery) }

        updateListOfMarkers(cursor)
    }

    @SuppressLint("Range")
    private fun updateListOfMarkers(cursor: Cursor) {
        // Get a list of notes from the database result.
        val notesList = mutableListOf<Note>()
        if (cursor.moveToFirst()) {
            do {
                val username = cursor.getString(cursor.getColumnIndex(KEY_NAME))
                val message  = cursor.getString(cursor.getColumnIndex(KEY_MESSAGE))
                val latitude = cursor.getDouble(cursor.getColumnIndex(KEY_LATITUDE))
                val longitude = cursor.getDouble(cursor.getColumnIndex(KEY_LONGITUDE))
                val note = Note(username, message, LatLng(latitude, longitude))
                notesList.add(note)
            } while (cursor.moveToNext())
        }
        // Post new notes list
        displayUpdatedMarkers.postValue(notesList)
    }

}