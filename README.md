# Movie Catalog
This project is made to fulfill Binar Academy Bootcamp challenge chapter 7, using TMDB API for list and detail movie, and using mockApi.io for Login and RegisterUser

## WARNING!!
- Don't use your personal data when register an account, because the API is public
- Before running the apps, add your TMDB API Key first

### How to add your TMDB API Key:
- Open project : local.properties
- Add this code : 
```xml
 TheMovieDBApi = YOUR_KEY
```
For example :  
```xml
 TheMovieDBApi = 6dshjakdhau8291h
```
### Another way to add your TMDB API Key:
- Open AndroidManifest.xml
- Search this line :
```xml
<meta-data
            android:name = "apiKey"
            android:value = "${TheMovieDBApi}"/>
```
- Edit android:value to your TMDB API Key
<p>For example :</p>

```xml
<meta-data
            android:name = "apiKey"
            android:value = "6dshjakdhau8291h"/>
```

## Feature
- Register and login for user
- Update data user
- List popular movie
- List now playing movie
- Detail movie(title, release date(only year), poster, average vote, tagline, genre, overview, cast, similiar movie)
- User favorite movie using database room
- Add image profile for user & save image in data store
- Using design pattern MVVM
- Using navigation component single activity
- Using dependency injection
- Unit Test for networking and room database
