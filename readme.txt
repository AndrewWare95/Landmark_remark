Landmark Remark

Project Comments.

    •   App Can be run using build variant "Debug"

	•	Login screen is a basic Activity with a text box and button. Logging in passes the username String to the next activity.
	•	The map view screen is an Activity, with a Fragment, that uses a ViewModel to manage logic.

	•	There is a “Note” data class and SQLite Database to store the data.
	•	The Database has 1 table, which store “Username”, “Message”, “Latitude” and “Longitude”

	•	The MainFragment has an observeViewModel function that observes updates for liveData in the viewModel.

	•	Opening the map I use a LocationService to create a task to get the users current location.
	•	When we receive that location, the camera location is updated to centre on the user.

	•	The Search bar filters using both the Username and Message field.
	•	The Username must match the searchQuery, but the Message must only contain the searchQuery.



Using the app.

	•	Launch page is a basic page for the user to enter their username.
	•	Once they have hit “Enter” they will be brought to the Main screen and requested location permissions.
	•	Clicking “Add note to my location” button will take the users current location, and ask the user to create a note for that location.
	•	Long clicking anywhere in the app will take that location and ask the user to create a note for that location.
	•	There is a search bar at the top of the screen that updates the markers on the map, each time the text field is updated.
	•	Clicking an icon on the map will display the User who created it, and the  Note that they entered for that location.



Time Management

Planning  - 30 minutes.
	•	Read project description.
	•	Plan/design basic architecture based on the project requirements.
	•	Plan project UI based on requirements.

Create base project - 1hr
	•	Create basic “Enter username screen”
	•	Add any additional dependencies required for using googleMap api and “com.afollestad:assent” for easier permission request.
	•	Add debug build variant.
	•	Create basic Main page to handle project tasks (display googleMap).

Begin Implementing Tasks

Display users current location on the map - 1.5hr
	•	Create google maps api key.
	•	Implement googleMaps api.
	•	Setup permissions.
	•	Setup location service to get the users current location so that I can move the view camera to that location.

Setup Database Manager. - 2h
	•	Create “Note” data class.
	•	Design and implement database table.
	•	Add functions to add a Note, retrieve all Notes, and get Filtered notes.

Complete task “Save a short note at my current location” - 1h
	•	Use viewModel to handle logic (create note data)
	•	Use MainFragment to handle :
	⁃	Displaying “Add note button”
	⁃	Display AlertDialog to allow user to save a note.
	⁃	Display success/error toast message.

Display all notes as Markers on the screen. - 1h
	•	Get all markers from the database, and display them as markers.
	•	Display current users markers as a different colour.


Add Search Toolbar  - 1.5h
	•	Add Search toolbar with
	•	Use search text to filter by Name or Note message.
