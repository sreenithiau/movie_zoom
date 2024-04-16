package moviezoom;

import java.util.List;

import labexam.ObjectFiles2;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;


public class MovieCatalog extends User {
   
	private static final long serialVersionUID = 1L;
	private List<Movie> movies;
    String username;
    int x;
    public MovieCatalog(String username, String password) {
    	
       super(username, password);
        this.movies = new ArrayList<>();
    this.username=username;
        
    }
    
    public void addMovie(Movie movie) {
        movies.add(movie);
      
    }

    public void deleteMovie(Movie movie) {
        movies.remove(movie);
    }
    
    public List<Movie> searchMovies(String searchTerm) {
        List<Movie> results = new ArrayList<>();

        for (Movie movie : movies) {
            // Customize the search criteria based on your requirements
        	 if (movie.getTitle().toLowerCase().contains(searchTerm.toLowerCase())) 
                 {results.add(movie);
                 continue;
                 }
             
             if (String.valueOf(movie.getYear()).contains(searchTerm)) 
                 {results.add(movie);
                 continue;
                 }
            
             if (movie.getGenre().toLowerCase().contains(searchTerm.toLowerCase())) 
                 {results.add(movie); 
                 continue;
                 }
             
             if (movie.getRating().toLowerCase().contains(searchTerm.toLowerCase())) 
            	 {results.add(movie);
            	 continue;
            	 }
             
             if (movie.getDirector().toLowerCase().contains(searchTerm.toLowerCase())) 
                 {results.add(movie);
                 continue;
                 }
        
             if (movie.getMediaFormat().toLowerCase().contains(searchTerm.toLowerCase())) 
                 {results.add(movie);
                 continue;
                 }
          
             if (movie.getTapeNumber().toLowerCase().contains(searchTerm.toLowerCase())) 
                 {results.add(movie);
                 continue;
                 }           
            }

        return results;
    }


    public List<Movie> getAllMovies() {
        return new ArrayList<>(movies);
    }

    public Movie searchMovie(String title) {
        for (Movie movie : movies) {
            if (movie.getTitle().equalsIgnoreCase(title)) {
                return movie;
            }
            
            
        }
        return null;
    }
    
    
  
        public List<Movie> searchMovies(String searchTerm, List<String> searchByList) {
            List<Movie> results = new ArrayList<>();

            for (Movie movie : movies) {
                // Customize the search criteria based on your requirements
                boolean shouldAdd = true; // Flag to determine if the movie should be added to the results

                for (String searchBy : searchByList) {
                	shouldAdd = true;
                    switch (searchBy.toLowerCase()) {
                        case "title":
                            shouldAdd &= movie.getTitle().toLowerCase().contains(searchTerm.toLowerCase());
                           // System.out.println(movie.getTitle().toLowerCase());
                            break;
                        case "year":
                            shouldAdd &= String.valueOf(movie.getYear()).contains(searchTerm);
                          //  System.out.println(String.valueOf(movie.getYear()).toLowerCase());
                            break;
                        case "genre":
                            shouldAdd &= movie.getGenre().toLowerCase().contains(searchTerm.toLowerCase());
                           // System.out.println(movie.getGenre().toLowerCase());
                            break;
                        case "rating":
                            shouldAdd &= movie.getRating().toLowerCase().contains(searchTerm.toLowerCase());
                            break;
                        case "director":
                            shouldAdd &= movie.getDirector().toLowerCase().contains(searchTerm.toLowerCase());
                            break;
                        case "mediaformat":
                            shouldAdd &= movie.getMediaFormat().toLowerCase().contains(searchTerm.toLowerCase());
                            
                            break;
                        case "tapenumber":
                            shouldAdd &= movie.getTapeNumber().toLowerCase().contains(searchTerm.toLowerCase());
                            break;
                        // Add more cases for additional search criteria

                        default:
                            // Handle unsupported search criteria
                            break;
                    }
                
                    if (shouldAdd) 
                    	 {
                    	results.add(movie);
                    	 break;
                     	 }
                  
                    
                    }   
            }
          
            return results;
        }

    
    public int deleteMovieByTitle(String title) {
        Movie movieToDelete = searchMovie(title);
        x=0;
        if (movieToDelete != null) {
            deleteMovie(movieToDelete);
            x++;
        }
        if(x==1)
        	return 1;
        else return 0;
    }

    public void editMovie( Movie movieToEdit, Movie updatedMovie) {
    	//  Movie movieToEdit = searchMovie(title);
    	    if (movieToEdit != null) {
    	        // Update the existing movie's details
    	        movieToEdit.setTitle(updatedMovie.getTitle());
    	        movieToEdit.setDirector(updatedMovie.getDirector());
    	        movieToEdit.setYear(updatedMovie.getYear());
    	        movieToEdit.setMediaFormat(updatedMovie.getMediaFormat());
    	        movieToEdit.setTapeNumber(updatedMovie.getTapeNumber());
    	        movieToEdit.setGenre(updatedMovie.getGenre());
    	        movieToEdit.setRating(updatedMovie.getRating());
    	        movieToEdit.setPersonalEvaluation(updatedMovie.getPersonalEvaluation());
    	        movieToEdit.setGeneralComment(updatedMovie.getGeneralComment());
    	    }
    	    saveMovieData();
    }

    public List<Movie> getMoviesByGenre(String genre) {
        List<Movie> result = new ArrayList<>();
        for (Movie movie : movies) {
            if (movie.getGenre().equalsIgnoreCase(genre)) {
                result.add(movie);
            }
        }
        return result;
    }

    public List<Movie> getMoviesReleasedAfter(int year) {
        List<Movie> result = new ArrayList<>();
        for (Movie movie : movies) {
            if (movie.getYear() > year) {
                result.add(movie);
            }
        }
        return result;
    }

    public void saveMovieData() {
    	
    	try {
    		String x = username + "movie.txt";
    	//	System.out.println(username);
    		// System.out.println(x);
			FileOutputStream f = new FileOutputStream(x);
			ObjectOutputStream out = new ObjectOutputStream(f);
			out.writeObject(movies);
			out.close();		
		//	System.out.println("hello");
	}
	catch(FileNotFoundException e){}
	catch(IOException e){}
}


 
	public void loadMovieData() {
		try {
		    String x = username + "movie.txt";
		    //System.out.println(username);
		   // System.out.println(x);
		    FileInputStream f = new FileInputStream(x);
		    ObjectInputStream in = new ObjectInputStream(f);
		    movies = (List<Movie>) in.readObject();
		    in.close();
		 //   System.out.println("Movie data loaded successfully.");
		} catch (FileNotFoundException e) {
		  //  System.out.println("File not found. Movie data not loaded.");
		} catch (IOException | ClassNotFoundException e) {
		   // System.out.println("Error loading movie data.");
		    e.printStackTrace();
		}
    }

}
