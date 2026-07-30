# MovieDB

A modern, feature-rich mobile application for browsing movies and TV shows with detailed information, ratings, and search capabilities.

## 📱 Features

### Core Functionality
- **Comprehensive Media Database**: Browse both movies and TV shows in one app
- **Advanced Search**: Search functionality with filtering options
- **Detailed Information**: View comprehensive details including:
  - Movie/show descriptions and summaries
  - Release dates and runtime
  - Star ratings and user vote counts
  - Production information (budget, revenue, companies)
  - Episode listings for TV shows
- **Categories**: Browse popular content with easy navigation
- **Clean UI**: Modern, dark-themed interface optimized for mobile viewing

### Search & Discovery
- **Smart Search**: Real-time search
- **Filter Options**: 
  - Toggle between Movies, TV Shows, or Both
  - Local search capabilities
  - Safe search mode
- **Popular Content**: Curated list of trending movies and shows
- **Detailed Views**: Rich content pages with poster images and comprehensive metadata

### User Experience
- **Intuitive Navigation**: Bottom navigation between Movies and TV Shows
- **High-Quality Images**: Movie posters and backdrop images
- **Episode Management**: Season and episode browsing for TV shows
- **Personal Lists**: "My List" functionality for saving favorites

## 🛠️ Technical Stack

Based on the UI design and functionality, this appears to be built with:
- **Platform**: Android (native mobile application)
- **UI Framework**: Modern Android UI components with dark theme
- **API Integration**: Movie/TV show data integration (TMDB API)
- **Image Loading**: Optimized image loading for posters and backdrops

## 📸 Screenshots

| Home Screen | Move Detail Screen | TV Detail Screen | Search Screen |
|--------------|--------------|----------------|---------------|
| ![Home](assets/ScreenShot1.png) | ![Move_Detail](assets/ScreenShot2.png) | ![TV_Detail](assets/ScreenShot3.png) | ![Search_Screen](assets/ScreenShot4.png) |

---

### Main Browse Screen
- Grid layout displaying popular movies and TV shows
- Star ratings and quick information
- Easy navigation between content types

### Detailed Movie/Show View
- Full-screen poster displays
- Comprehensive metadata including budget, revenue, and production details
- Episode listings for TV series

### Advanced Search
- Real-time search with filtering options
- Search result previews with poster thumbnails
- Media type filtering (Movies/TV Shows/Both)

## 🚀 Getting Started

### Prerequisites
- Android Studio
- Android SDK (API level 26 or higher)
- Internet connection for API data

### Installation

1. Clone the repository:
```bash
git clone https://github.com/nicklauscott/MovieDB.git
```

2. Open the project in Android Studio

3. Add your API key to the project:
   - Create a `local.properties` file in the root directory
   - Add your API key: `API_KEY=your_api_key_here`

4. Build and run the project:
```bash
./gradlew assembleDebug
```

## 🎯 Key Features Demonstrated

- **Modern Android UI**: Clean, intuitive interface following Material Design principles
- **API Integration**: Seamless integration with movie/TV database APIs
- **Search Functionality**: Advanced search with real-time filtering
- **Data Management**: Efficient handling of large datasets with pagination
- **Image Optimization**: Smooth loading and caching of high-quality images
- **Navigation**: Intuitive bottom navigation and detail view transitions

## 📊 Data Sources

The app provides rich metadata including:
- Movie/show titles and descriptions
- Release dates and runtime information
- Production budgets and box office revenue
- Cast and crew information
- User ratings and review counts
- Episode guides for TV series

## 🔧 Configuration

### Search Settings
- **Local Search**: Toggle for offline search capabilities
- **Safe Search**: Content filtering options
- **Media Type Filters**: Customize search results by content type

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the project
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License.

## 👨‍💻 Author

**Nicklauscott** - [nicklauscott](https://github.com/nicklauscott)

## 🙏 Acknowledgments

- Movie and TV show data providers
- Android development community
- Open source libraries and frameworks used in this project

---

*Built with ❤️ for movie and TV show enthusiasts*
