package moviezoom;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.geometry.Pos;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MovieZoomApp extends Application {
	
	//UserMovieData user1Data;
	  String username; //= usernameField.getText();
      String password;// = passwordField.getText();
	MovieCatalog movieCatalog;// = new MovieCatalog(username,password);
	User movieuser;
	private TableView<Movie> movieTable = new TableView<>();
	private VBox addMovieLayout;
	private static final String DATABASE_URL = "jdbc:sqlite:moviezoom.db";

	TextField titleField;
	TextField directorField;
	TextField yearField;
	TextField mediaFormatField;
	TextField tapeNumberField;
	TextField genreField;
	ComboBox<String> ratingComboBox;
	TextField personalEvaluationField;
	TextArea generalCommentArea;

	String title;
	String director;

	int i = 0;
	// Create UI components for adding a movie (text fields, buttons, etc.)

	public static void main(String[] args) {
		// Load the SQLite JDBC driver
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		primaryStage.setTitle("Login Page");

		VBox root = new VBox();
		root.setSpacing(10);
		root.setMinSize(800, 600);

		TextField usernameField = new TextField();
		PasswordField passwordField = new PasswordField();
		Button loginButton = new Button("Login");
		Button signUpButton = new Button("Sign Up");

		root.getChildren().addAll(new Label("Username:"), usernameField, new Label("Password:"), passwordField,
				loginButton, signUpButton);  
		loginButton.setOnAction(e -> handleLogin(usernameField.getText(), passwordField.getText()));
		signUpButton.setOnAction(e -> handleSignUp(usernameField.getText(), passwordField.getText()));

		Scene scene = new Scene(root);
		root.getStyleClass().add("add-movie-layout");
		root.setAlignment(Pos.CENTER);
		scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();

		// Create the database and necessary table
		initializeDatabase();
	}

	private void handleLogin(String username, String password) {
		try (Connection connection = DriverManager.getConnection(DATABASE_URL);
				PreparedStatement preparedStatement = connection
						.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?")) {

			preparedStatement.setString(1, username);
			preparedStatement.setString(2, password);

			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				showAlert("Login Successful");
				 
				    movieuser= new User(username,password);
					movieCatalog = new MovieCatalog(username,password);
				// Open the MovieZoomApp window upon successful login
				openMovieZoomApp();
			} else {
				showAlert("Invalid username or password");
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		
	}
    


	private void handleSignUp(String username, String password) {
		try (Connection connection = DriverManager.getConnection(DATABASE_URL);
				PreparedStatement preparedStatement = connection
						.prepareStatement("INSERT INTO users (username, password) VALUES (?, ?)")) {

			preparedStatement.setString(1, username);
			preparedStatement.setString(2, password);

			preparedStatement.executeUpdate();

			showAlert("Sign Up Successful");

		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		
	}

	private void showAlert(String message) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Information");
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

	private void initializeDatabase() {
		try (Connection connection = DriverManager.getConnection(DATABASE_URL);
				PreparedStatement preparedStatement = connection.prepareStatement(
						"CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, password TEXT)")) {
			preparedStatement.executeUpdate();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}


	private void openMovieZoomApp() {
		Stage movieZoomStage = new Stage();
		movieZoomStage.setTitle("MovieZoom - Main Page");
		movieCatalog.loadMovieData();
		// Create UI components for the main page
		Button addMovieButton = new Button("Add Movie");
		Button searchMovieButton = new Button("Search Movie");
		Button viewAllMoviesButton = new Button("View all Movies");
		Button logoutButton = new Button("Logout");

		// Set actions for buttons
		addMovieButton.setOnAction(e -> showAddMovieFrame(movieZoomStage, null));
		searchMovieButton.setOnAction(e -> showSearchMovieFrame(movieZoomStage));
		viewAllMoviesButton.setOnAction(e -> showMovieFrame(movieZoomStage));
		logoutButton.setOnAction(e -> handleLogout(movieZoomStage));

		// Create layout for the main page
		VBox mainPageLayout = new VBox(20);
		mainPageLayout.getChildren().addAll(addMovieButton, searchMovieButton, viewAllMoviesButton, logoutButton);
		mainPageLayout.setAlignment(Pos.CENTER);

		// Set scene for the main page
		Scene mainPageScene = new Scene(mainPageLayout, 800, 600);
		mainPageScene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

		movieZoomStage.setScene(mainPageScene);
		movieZoomStage.show();
	}

	private void handleLogout(Stage stage) {
		// Display a confirmation dialog
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Logout");
		alert.setHeaderText(null);
		alert.setContentText("Are you sure you want to logout?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.isPresent() && result.get() == ButtonType.OK) {
			// Perform logout actions here
			// For example, close the current stage (main page) and show the login page
			movieCatalog.saveMovieData();
			stage.close();
			showLoginPage();
		}
	}

	private void showLoginPage() {
		// Your code to show the login page goes here
		Stage movieStage = new Stage();
		movieStage.setTitle("Login Page");

		VBox root = new VBox();
		root.setSpacing(10);
		root.setMinSize(800, 600);

		TextField usernameField = new TextField();
		PasswordField passwordField = new PasswordField();
		Button loginButton = new Button("Login");
		Button signUpButton = new Button("Sign Up");

		root.getChildren().addAll(new Label("Username:"), usernameField, new Label("Password:"), passwordField,
				loginButton, signUpButton);

		loginButton.setOnAction(e -> handleLogin(usernameField.getText(), passwordField.getText()));
		signUpButton.setOnAction(e -> handleSignUp(usernameField.getText(), passwordField.getText()));

		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
		movieStage.setScene(scene);
		root.getStyleClass().add("add-movie-layout");
		root.setAlignment(Pos.CENTER);
		root.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
		movieStage.show();
		

		// Create the database and necessary table
		initializeDatabase();
			}

	private void showMovieFrame(Stage primaryStage) {

		Stage AllMovieStage = new Stage();
		AllMovieStage.setTitle("Movie List");
		Button backButton = new Button("Back");

		backButton.setOnAction(e -> AllMovieStage.hide());

		while (i < 1) {
			
			// Inside the showSearchMovieFrame method
			TableColumn<Movie, String> titleColumn = new TableColumn<>("Title");
			titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

			TableColumn<Movie, String> directorColumn = new TableColumn<>("Director");
			directorColumn.setCellValueFactory(new PropertyValueFactory<>("director"));

			TableColumn<Movie, Integer> yearColumn = new TableColumn<>("Year");
			yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));

			TableColumn<Movie, String> mediaFormatColumn = new TableColumn<>("Media Format");
			mediaFormatColumn.setCellValueFactory(new PropertyValueFactory<>("mediaFormat"));

			TableColumn<Movie, String> tapeNumberColumn = new TableColumn<>("Tape Number");
			tapeNumberColumn.setCellValueFactory(new PropertyValueFactory<>("tapeNumber"));

			TableColumn<Movie, String> genreColumn = new TableColumn<>("Genre");
			genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));

			TableColumn<Movie, String> ratingColumn = new TableColumn<>("Rating");
			ratingColumn.setCellValueFactory(new PropertyValueFactory<>("rating"));

			TableColumn<Movie, String> personalEvaluationColumn = new TableColumn<>("Personal Evaluation");
			personalEvaluationColumn.setCellValueFactory(new PropertyValueFactory<>("personalEvaluation"));

			TableColumn<Movie, String> generalCommentColumn = new TableColumn<>("General Comment");
			generalCommentColumn.setCellValueFactory(new PropertyValueFactory<>("generalComment"));

			// Add columns to the table
			movieTable.getColumns().addAll(titleColumn, directorColumn, yearColumn, mediaFormatColumn, tapeNumberColumn,
					genreColumn, ratingColumn, personalEvaluationColumn, generalCommentColumn);

			i++;
		}
		VBox movieShow = new VBox(1);
		movieShow.getChildren().addAll(movieTable, backButton);
		movieShow.getStyleClass().add("add-movie-layout");
		movieShow.setAlignment(Pos.CENTER);

		Scene showMovieScene = new Scene(movieShow, 800, 600);
		movieShow.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
		AllMovieStage.setScene(showMovieScene);
		AllMovieStage.show();
		refreshTable();

	}

	private void showAddMovieFrame(Stage primaryStage, Movie existingMovie) {
		Stage addMovieStage = new Stage();

		Button saveButton = new Button("Save");

		addMovieStage.setTitle("Add Movie");

		// Create layout for adding a movie
		addMovieLayout = new VBox(1);
		titleField = new TextField();
		directorField = new TextField();
		yearField = new TextField();
		mediaFormatField = new TextField();
		tapeNumberField = new TextField();
		genreField = new TextField();
		ratingComboBox = new ComboBox<>();
		personalEvaluationField = new TextField();
		generalCommentArea = new TextArea();

		ratingComboBox.getItems().addAll("G", "PG", "PG-13", "R", "NC-17");

		addMovieLayout.getChildren().addAll(new Label("Title:"), titleField, new Label("Director:"), directorField,
				new Label("Year:"), yearField, new Label("mediaFormat:"), mediaFormatField, new Label("tapeNumber:"),
				tapeNumberField, new Label("Genre:"), genreField, new Label("Rating:"), ratingComboBox,
				new Label("PersonalEvaluation:"), personalEvaluationField, new Label("GeneralComment:"),
				generalCommentArea, saveButton);

		addMovieLayout.getStyleClass().add("add-movie-layout");
		saveButton.setAlignment(Pos.CENTER);

		// Set the style class for each control
		titleField.getStyleClass().add("text-field");
		directorField.getStyleClass().add("text-field");
		yearField.getStyleClass().add("text-field");
		mediaFormatField.getStyleClass().add("text-field");
		tapeNumberField.getStyleClass().add("text-field");
		genreField.getStyleClass().add("text-field");
		ratingComboBox.getStyleClass().add("combo-box");
		personalEvaluationField.getStyleClass().add("text-field");
		generalCommentArea.getStyleClass().add("text-area");

		if (existingMovie != null) {
			setMovieDetailsInFields(existingMovie);

			addMovieStage.setTitle("Edit Movie");
		}

		// Set scene for adding a movie
		Scene addMovieScene = new Scene(addMovieLayout, 800, 600);
		addMovieScene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
		addMovieStage.setScene(addMovieScene);
		addMovieStage.show();

		// Set action for save button
		saveButton.setOnAction(e -> {

			if (validateInput()) {
				title = titleField.getText();
				director = directorField.getText();
				int year = 0;

				try {
					year = Integer.parseInt(yearField.getText());

					// Check if the year is a 4-digit number and less than 2024
					if (year < 1000 || year > 2023) {
						showErrorAlert("Please enter a valid year.");
						return;
					}
				} catch (NumberFormatException ex) {
					showErrorAlert("Year must be a number.");
					return;
				}

				// Continue with the rest of your code...
			}

			String title = titleField.getText();
			String director = directorField.getText();
			int year = Integer.parseInt(yearField.getText());
			String mediaFormat = mediaFormatField.getText();
			String tapeNumber = tapeNumberField.getText();
			String genre = genreField.getText();
			String rating = ratingComboBox.getValue();
			String personalEvaluation = personalEvaluationField.getText();
			String generalComment = generalCommentArea.getText();

			Movie newMovie = new Movie(title, director, year, mediaFormat, tapeNumber, genre, rating,
					personalEvaluation, generalComment);
			if (existingMovie == null)
				movieCatalog.addMovie(newMovie);
			else {
				movieCatalog.editMovie(existingMovie, newMovie);
			}

			addMovieStage.close();
		});

		refreshTable();
	}

	// Validation method
	private boolean validateInput() {

		if (titleField.getText().isEmpty() || directorField.getText().isEmpty() || yearField.getText().isEmpty()
				|| mediaFormatField.getText().isEmpty() || tapeNumberField.getText().isEmpty()
				|| genreField.getText().isEmpty() || ratingComboBox.getValue() == null
				|| personalEvaluationField.getText().isEmpty() || generalCommentArea.getText().isEmpty()) {
			showErrorAlert("All fields must be filled.");
			return false;
		}

		return true;
	}

	// Method to show an error alert
	private void showErrorAlert(String message) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

	
	private void showSearchMovieFrame(Stage primaryStage) {
		Stage searchMovieStage = new Stage();
		searchMovieStage.setTitle("Search Movie");

		// Create UI components for searching movies (text fields, buttons, etc.)
		Label searchByLabel = new Label("Search By:");
		CheckBox titleCheckBox = new CheckBox("Title");
		CheckBox yearCheckBox = new CheckBox("Year");
		CheckBox genreCheckBox = new CheckBox("Genre");
		CheckBox ratingCheckBox = new CheckBox("Rating");
		CheckBox directorCheckBox = new CheckBox("Director");
		CheckBox mediaFormatCheckBox = new CheckBox("Media Format");
		CheckBox tapeNumberCheckBox = new CheckBox("Tape Number");
		
		titleCheckBox.getStyleClass().add("check-box");
		yearCheckBox.getStyleClass().add("check-box");
		tapeNumberCheckBox.getStyleClass().add("check-box");
		genreCheckBox.getStyleClass().add("check-box");
		ratingCheckBox.getStyleClass().add("check-box");
		directorCheckBox.getStyleClass().add("check-box");
		mediaFormatCheckBox.getStyleClass().add("check-box");
		
		

		TextField searchField = new TextField();
		Button searchButton = new Button("Search");
		Button deleteButton = new Button("Delete Movie");
		Button editButton = new Button("Edit Movie");

		// Label to display search results
		Label resultLabel = new Label();
		// Create TableView to display search results
		TableView<Movie> searchResultsTable = new TableView<>();

		// Inside the showSearchMovieFrame method
		TableColumn<Movie, String> titleColumn = new TableColumn<>("Title");
		titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

		TableColumn<Movie, String> directorColumn = new TableColumn<>("Director");
		directorColumn.setCellValueFactory(new PropertyValueFactory<>("director"));

		TableColumn<Movie, Integer> yearColumn = new TableColumn<>("Year");
		yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));

		TableColumn<Movie, String> mediaFormatColumn = new TableColumn<>("Media Format");
		mediaFormatColumn.setCellValueFactory(new PropertyValueFactory<>("mediaFormat"));

		TableColumn<Movie, String> tapeNumberColumn = new TableColumn<>("Tape Number");
		tapeNumberColumn.setCellValueFactory(new PropertyValueFactory<>("tapeNumber"));

		TableColumn<Movie, String> genreColumn = new TableColumn<>("Genre");
		genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));

		TableColumn<Movie, String> ratingColumn = new TableColumn<>("Rating");
		ratingColumn.setCellValueFactory(new PropertyValueFactory<>("rating"));

		TableColumn<Movie, String> personalEvaluationColumn = new TableColumn<>("Personal Evaluation");
		personalEvaluationColumn.setCellValueFactory(new PropertyValueFactory<>("personalEvaluation"));

		TableColumn<Movie, String> generalCommentColumn = new TableColumn<>("General Comment");
		generalCommentColumn.setCellValueFactory(new PropertyValueFactory<>("generalComment"));

		// Add columns to the table
		searchResultsTable.getColumns().addAll(titleColumn, directorColumn, yearColumn, mediaFormatColumn,
				tapeNumberColumn, genreColumn, ratingColumn, personalEvaluationColumn, generalCommentColumn);

		// Set action for search button
		searchButton.setOnAction(e -> {
			String searchTerm = searchField.getText();

			List<String> searchByList = new ArrayList<>();
			if (titleCheckBox.isSelected())
				searchByList.add("title");
			if (yearCheckBox.isSelected())
				searchByList.add("year");
			if (genreCheckBox.isSelected())
				searchByList.add("genre");
			if (ratingCheckBox.isSelected())
				searchByList.add("rating");
			if (directorCheckBox.isSelected())
				searchByList.add("director");
			if (mediaFormatCheckBox.isSelected())
				searchByList.add("mediaformat");
			if (tapeNumberCheckBox.isSelected())
				searchByList.add("tapenumber");
			List<Movie> searchResults;
			if (searchByList.isEmpty())
				searchResults = movieCatalog.searchMovies(searchTerm);
			else
				searchResults = movieCatalog.searchMovies(searchTerm, searchByList);
			ObservableList<Movie> searchResultsData = FXCollections.observableArrayList(searchResults);
			searchResultsTable.setItems(searchResultsData);

			if (searchTerm == "")
				resultLabel.setText("Movie not found");
			// Show the TableView
			else
				searchResultsTable.setVisible(true);

		});

		deleteButton.setOnAction(e -> {
			String searchTerm = searchField.getText();
			int x=movieCatalog.deleteMovieByTitle(searchTerm);
			if (x == 0)
				resultLabel.setText("Movie not found");
			else
				resultLabel.setText("Movie deleted");
			
		});

		// Set action for edit button
		editButton.setOnAction(e -> {
			String searchTerm = searchField.getText();
			Movie foundMovie = movieCatalog.searchMovie(searchTerm);
			if (foundMovie != null) {
				setMovieDetailsInFields(foundMovie);
				showAddMovieFrame(primaryStage, foundMovie); // Pass the found movie to the add movie frame
			} else {
				resultLabel.setText("Movie not found");
			}
			
		});

		// Layout
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.add(searchByLabel, 0, 0);
		grid.add(titleCheckBox, 1, 0);
		grid.add(yearCheckBox, 2, 0);
		grid.add(genreCheckBox, 3, 0);
		grid.add(ratingCheckBox, 4,0);
		grid.add(directorCheckBox, 5,0);
		grid.add(mediaFormatCheckBox,6,0);
		grid.add(tapeNumberCheckBox, 7,0);
	
		VBox layout = new VBox(20);
		layout.getChildren().addAll(grid ,searchField,searchButton, deleteButton,editButton,resultLabel,
				searchResultsTable);
		layout.getStyleClass().add("add-movie-layout");
		layout.setAlignment(Pos.CENTER);

		// Hide the TableView initially
		searchResultsTable.setVisible(false);

		// Scene
		Scene scene = new Scene(layout, 800, 600);
		scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
		searchMovieStage.setScene(scene);

		// Show the stage
		searchMovieStage.show();
	}

	private void setMovieDetailsInFields(Movie movie) {
		// Use the existing fields from the addMovieLayout
		TextField titleField = (TextField) addMovieLayout.getChildren().get(1);
		TextField directorField = (TextField) addMovieLayout.getChildren().get(3);
		TextField yearField = (TextField) addMovieLayout.getChildren().get(5);
		TextField mediaFormatField = (TextField) addMovieLayout.getChildren().get(7);
		TextField tapeNumberField = (TextField) addMovieLayout.getChildren().get(9);
		TextField genreField = (TextField) addMovieLayout.getChildren().get(11);
		ComboBox<String> ratingComboBox = (ComboBox<String>) addMovieLayout.getChildren().get(13);
		TextField personalEvaluationField = (TextField) addMovieLayout.getChildren().get(15);
		TextArea generalCommentArea = (TextArea) addMovieLayout.getChildren().get(17);
		// System.out.println("Children of addMovieLayout: " +
		// addMovieLayout.getChildren());
		titleField.setText(movie.getTitle());
		directorField.setText(movie.getDirector());
		yearField.setText(String.valueOf(movie.getYear()));
		mediaFormatField.setText(movie.getMediaFormat());
		tapeNumberField.setText(movie.getTapeNumber());
		genreField.setText(movie.getGenre());
		ratingComboBox.setValue(movie.getRating());
		personalEvaluationField.setText(movie.getPersonalEvaluation());
		generalCommentArea.setText(movie.getGeneralComment());
	}

	/*
	 * private void clearMovieDetailsLabels() { Label titleLabel = new
	 * Label("Title: "); Label directorLabel = new Label("Director: "); Label
	 * yearLabel = new Label("Year: "); Label mediaFormatLabel = new
	 * Label("Media Format: "); Label tapeNumberLabel = new Label("Tape Number: ");
	 * Label genreLabel = new Label("Genre: "); Label ratingLabel = new
	 * Label("Rating: "); Label personalEvaluationLabel = new
	 * Label("Personal Evaluation: "); Label generalCommentLabel = new
	 * Label("General Comment: ");
	 * 
	 * titleLabel.setText("Title: "); directorLabel.setText("Director: ");
	 * yearLabel.setText("Year: "); mediaFormatLabel.setText("Media Format: ");
	 * tapeNumberLabel.setText("Tape Number: "); genreLabel.setText("Genre: ");
	 * ratingLabel.setText("Rating: ");
	 * personalEvaluationLabel.setText("Personal Evaluation: ");
	 * generalCommentLabel.setText("General Comment: "); }
	 */
	// Helper method to set movie details in labels
	/*
	 * private void setMovieDetailsInLabels(Movie movie) { Label titleLabel = new
	 * Label("Title: "); Label directorLabel = new Label("Director: "); Label
	 * yearLabel = new Label("Year: "); Label mediaFormatLabel = new
	 * Label("Media Format: "); Label tapeNumberLabel = new Label("Tape Number: ");
	 * Label genreLabel = new Label("Genre: "); Label ratingLabel = new
	 * Label("Rating: "); Label personalEvaluationLabel = new
	 * Label("Personal Evaluation: "); Label generalCommentLabel = new
	 * Label("General Comment: ");
	 * 
	 * titleLabel.setText("Title: " + movie.getTitle());
	 * directorLabel.setText("Director: " + movie.getDirector());
	 * yearLabel.setText("Year: " + movie.getYear());
	 * mediaFormatLabel.setText("Media Format: " + movie.getMediaFormat());
	 * tapeNumberLabel.setText("Tape Number: " + movie.getTapeNumber());
	 * genreLabel.setText("Genre: " + movie.getGenre());
	 * ratingLabel.setText("Rating: " + movie.getRating());
	 * personalEvaluationLabel.setText("Personal Evaluation: " +
	 * movie.getPersonalEvaluation());
	 * generalCommentLabel.setText("General Comment: " + movie.getGeneralComment());
	 * }
	 */

	private void refreshTable() {
		ObservableList<Movie> movieData = FXCollections.observableArrayList(movieCatalog.getAllMovies());

		// Clear and update the items in the table
		movieTable.getItems().clear();
		movieTable.setItems(movieData);
	}
}
