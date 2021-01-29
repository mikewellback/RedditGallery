# RedditGallery
A gallery of top posts from Reddit

![RedditGallery logo](/app/src/main/res/mipmap-xhdpi/ic_reddit_round.png)

### Features:
- Search while typing
- View photos in grid and gallery modes
- Image zoom feature
- Easily create a favorites list
- Favorites can also be viewed when offline
- 30s to undo the last favorite removal operation
- Support for dark mode

### Technical info:
This project follows Google guidelines for design and architecture, using
[AppCompat](https://developer.android.com/jetpack/androidx/releases/appcompat),
[MaterialDesign](https://material.io/design/introduction),
[ViewBinding](https://developer.android.com/topic/libraries/view-binding),
[ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) and
[LiveData](https://developer.android.com/topic/libraries/architecture/livedata).

The code is written in Kotlin and uses [Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html).

It also features the usage of:
- [Retrofit](https://square.github.io/retrofit/) for API calls
- [Room](https://developer.android.com/jetpack/androidx/releases/room) for favorites database
- [Glide](https://github.com/bumptech/glide) for image loading and caching
- [PhotoView](https://github.com/Baseflow/PhotoView) for photo zooming
