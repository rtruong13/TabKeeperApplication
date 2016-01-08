package com.i.should.what.whatshouldi.MoviesPackage.Models;

import android.widget.ListView;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ryan on 7/18/2015.
 */
public class MovieDBFullMovieModel implements Serializable {
    @SerializedName("belongs_to_collection")
    @Expose
    private MovieDBCollectionModel belongsToCollection;
    @SerializedName("genre_ids")
    @Expose
    private List<Integer> genre_ids;
    @SerializedName("genres")
    @Expose
    private List<MovieDBGenreModel> genres = new ArrayList<MovieDBGenreModel>();
    @SerializedName("homepage")
    @Expose
    private String homepage;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("original_title")
    @Expose
    private String originalTitle;
    @SerializedName("original_name")
    @Expose
    private String originalName;
    @SerializedName("overview")
    @Expose
    private String overview;
    @SerializedName("popularity")
    @Expose
    private Double popularity;
    @SerializedName("poster_path")
    @Expose
    private String posterPath;
    @SerializedName("release_date")
    @Expose
    private String releaseDate;
    @SerializedName("first_air_date")
    @Expose
    private String firstAirDate;
    @SerializedName("runtime")
    @Expose
    private Integer runtime;
    @SerializedName("tagline")
    @Expose
    private String tagline;
    @SerializedName("vote_average")
    @Expose
    private Double voteAverage;
    @SerializedName("vote_count")
    @Expose
    private Integer voteCount;
    @SerializedName("credits")
    @Expose
    private MovieDBCreditsModel credits;
    @SerializedName("created_by")
    @Expose
    private List<TVShowCreator> createdBy = new ArrayList<TVShowCreator>();

    public MoviesState state = MoviesState.ADDED;

    public MovieDBFullMovieModel(MovieDBFullMovieModel movieModel) {
        belongsToCollection = new MovieDBCollectionModel(movieModel.belongsToCollection);
        if (movieModel.genres != null)
            genres = new ArrayList<MovieDBGenreModel>(movieModel.genres);
        if (movieModel.genre_ids != null)
            genre_ids = new ArrayList<Integer>(movieModel.genre_ids);
        homepage = movieModel.homepage;
        id = movieModel.id;
        originalTitle = movieModel.originalTitle;
        originalName = movieModel.originalName;
        overview = movieModel.overview;
        popularity = movieModel.popularity;
        posterPath = movieModel.posterPath;
        releaseDate = movieModel.releaseDate;
        firstAirDate = movieModel.firstAirDate;
        runtime = movieModel.runtime;
        tagline = movieModel.tagline;
        voteAverage = movieModel.voteAverage;
        voteCount = movieModel.voteCount;
        createdBy = movieModel.createdBy;
        state = MoviesState.ADDED;
        if (movieModel.credits != null)
            credits = new MovieDBCreditsModel(movieModel.credits);
    }

    public MovieDBFullMovieModel() {
    }

    /**
     * @return The belongsToCollection
     */
    public MovieDBCollectionModel getBelongsToCollection() {
        return belongsToCollection;
    }

    /**
     * @param belongsToCollection The belongs_to_collection
     */
    public void setBelongsToCollection(MovieDBCollectionModel belongsToCollection) {
        this.belongsToCollection = belongsToCollection;
    }

    /**
     * @return The genres
     */
    public List<Integer> getGenresDiscoveryIds() {
        return genre_ids;
    }

    /**
     * @param genres The genres
     */
    public void setGenresDiscoveryIds(List<Integer> genres) {
        this.genre_ids = new ArrayList<>(genres);
    }


    /**
     * @return The genres
     */
    public List<MovieDBGenreModel> getGenres() {
        return genres;
    }

    /**
     * @param genres The genres
     */
    public void setGenres(List<MovieDBGenreModel> genres) {
        this.genres = genres;
    }

    /**
     * @return The homepage
     */
    public String getHomepage() {
        return homepage;
    }

    /**
     * @param homepage The homepage
     */
    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    /**
     * @return The id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return The originalTitle
     */
    public String getOriginalTitle() {
        if((originalTitle==null || originalTitle.isEmpty()) && originalName!=null && !originalName.isEmpty())
            return originalName;
        return originalTitle;
    }

    /**
     * @param originalTitle The original_title
     */
    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    /**
     * @return The overview
     */
    public String getOverview() {
        return overview;
    }

    /**
     * @param overview The overview
     */
    public void setOverview(String overview) {
        this.overview = overview;
    }

    /**
     * @return The popularity
     */
    public Double getPopularity() {
        return popularity;
    }

    /**
     * @param popularity The popularity
     */
    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    /**
     * @return The posterPath
     */
    public String getPosterPath() {
        return posterPath;
    }

    /**
     * @param posterPath The poster_path
     */
    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    /**
     * @return The releaseDate
     */
    public String getReleaseDate() {

        if((releaseDate==null || releaseDate.isEmpty()) && firstAirDate!=null && !firstAirDate.isEmpty())
            return firstAirDate;
        return releaseDate;
    }

    /**
     * @param releaseDate The release_date
     */
    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    /**
     * @return The runtime
     */
    public Integer getRuntime() {
        return runtime;
    }

    /**
     * @param runtime The runtime
     */
    public void setRuntime(Integer runtime) {
        this.runtime = runtime;
    }

    /**
     * @return The tagline
     */
    public String getTagline() {
        return tagline;
    }

    /**
     * @param tagline The tagline
     */
    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    /**
     * @return The voteAverage
     */
    public Double getVoteAverage() {
        return voteAverage;
    }

    /**
     * @param voteAverage The vote_average
     */
    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    /**
     * @return The voteCount
     */
    public Integer getVoteCount() {
        return voteCount;
    }

    /**
     * @param voteCount The vote_count
     */
    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    /**
     * @return The credits
     */
    public MovieDBCreditsModel getCredits() {

        if(credits == null) return null;
        if(credits.getCrew() == null || credits.getCrew().size()<=0)
        {
            if(createdBy!=null && createdBy.size()>0)
            {
                List<MovieDBCrewModel> crews = new ArrayList<>();
                for(TVShowCreator creator : createdBy)
                {
                    crews.add(new MovieDBCrewModel(creator));
                }
            }
        }
        return credits;
    }

    /**
     * @param credits The credits
     */
    public void setCredits(MovieDBCreditsModel credits) {
        this.credits = credits;
    }

    private String[] desiredDeps = {"Directing", "Production"};

    private boolean containsInDesired(String dep) {
        for (int i = 0; i < desiredDeps.length; i++) {
            if (dep.equals(desiredDeps[i]))
                return true;
        }

        int a = new Integer(4);
        Integer aa = 5;

        return false;
    }

    public void clean() {
        List<MovieDBCrewModel> newCrewsList = new ArrayList<>();
        if (credits == null) return;

        for (MovieDBCrewModel crew :
                credits.getCrew()) {
            if (containsInDesired(crew.getDepartment()))
                newCrewsList.add(crew);
        }
        getCredits().setCrew(newCrewsList);
    }

    public String getGenresString() {
        StringBuilder genresString = new StringBuilder();

        if (genres == null) return "";
        for (int i = 0; i < Math.min(genres.size(), 3); i++) {
            genresString.append(genres.get(i).getName());
            genresString.append(",\n");
        }
        genresString.deleteCharAt(genresString.length() - 1);
        genresString.deleteCharAt(genresString.length() - 1);

        return genresString.toString();
    }

    public String getPosterImage() {
        StringBuilder builder = new StringBuilder();
        builder.append("http://image.tmdb.org/t/p/w500");
        builder.append(getPosterPath());
        return builder.toString();
    }

    public String getGenresIds() {
        StringBuilder genresString = new StringBuilder();

        if (genres == null || genres.size() == 0) return "";
        for (int i = 0; i < genres.size(); i++) {
            genresString.append(genres.get(i).getId());
            genresString.append("#");
        }
        genresString.deleteCharAt(genresString.length() - 1);

        return genresString.toString();
    }

    public static int getYear(String date) {
        if (date != null && !date.isEmpty())
            return Integer.parseInt(date.substring(0, 4));
        else return 1900;
    }

    public static int getMonth(String date) {

        if (date != null && !date.isEmpty())
            return Integer.parseInt(date.substring(5, 7));
        else return 1;
    }

    public boolean isAnimation()
    {
        if(genres!=null && genres.size()>0)
        {
            for (MovieDBGenreModel genre: genres) {
                if(genre.getId() == 16)
                    return true;
            }
        }else if(genre_ids!=null && genre_ids.size()>0)
        {
            for (Integer genre: genre_ids) {
                if(genre == 16)
                    return true;
            }

        }

        return false;
    }

    public String getDirectoString()
    {
        String directorString = "not loaded";
        if(credits != null)
        {
            directorString = credits.getDirectorString();
        }
        if(directorString.equals("not loaded"))
        {
            if(createdBy != null && createdBy.size()>0)
            {
                directorString = createdBy.get(0).getStringName();
            }
        }
        return directorString;
    }
}