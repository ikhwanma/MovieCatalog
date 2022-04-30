# BinarChallengeEnam
Using TMDB API for list and detail movie, and using mockApi.io for Login and RegisterUser

## WARNING!!
- Don't use your personal data when register an account, because the API is public
- Before running the apps, add your TMDB API Key first

### How to add your TMDB API Key:
- Open project : local.properties
- Add this code : 
```
 TheMovieDBApi = YOUR_KEY
```
For example :  
```
 TheMovieDBApi = 6dshjakdhau8291h
```
### Another way to add your TMDB API Key:
- Open AndroidManifest.xml
- Search this line :
```
<meta-data
            android:name = "apiKey"
            android:value = "${TheMovieDBApi}"/>
```
- Edit android:value to your TMDB API Key
<p>For example</p>

```
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
