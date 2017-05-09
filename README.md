## react-native-music-metadata

React Native module for reading the music metadata on iOS and Android.

## Usage (iOS)

First you need to install react-native-music-metadata:

```javascript
npm install react-native-music-metadata --save
```

### Adding automatically with react-native link

At the command line, in your project folder, type:

`react-native link react-native-music-metadata`

Boom! No need to worry about manually adding the library to your project.

### Adding Manually in XCode

In XCode, in the project navigator, right click Libraries ➜ Add Files to [your project's name] Go to node_modules ➜ react-native-music-metadata and add the .xcodeproj file

In XCode, in the project navigator, select your project. Add the `lib*.a` from the RNMusicMetadata project to your project's Build Phases ➜ Link Binary With Libraries. Click the .xcodeproj file you added before in the project navigator and go the Build Settings tab. Make sure 'All' is toggled on (instead of 'Basic'). Look for Header Search Paths and make sure it contains both `$(SRCROOT)/../react-native/React` and `$(SRCROOT)/../../React` - mark both as recursive.

Run your project (Cmd+R)

## Usage (Android)

Make alterations to the following files:

* `android/settings.gradle`

```gradle
...
include ':react-native-music-metadata'
project(':react-native-music-metadata').projectDir = new File(settingsDir, '../node_modules/react-native-music-metadata/android')
```

* `android/app/build.gradle`

```gradle
...
dependencies {
    ...
    compile project(':react-native-music-metadata')
}
```

* register module (in MainApplication.java)

```java
import com.venepe.RNMusicMetadata.RNMusicMetadataPackage; // <------- add package

public class MainApplication extends Application implements ReactApplication {
   // ...
    @Override
    protected List<ReactPackage> getPackages() {
      return Arrays.<ReactPackage>asList(
        new MainReactPackage(), // <---- add comma
        new RNMusicMetadataPackage() // <---------- add package
      );
    }
```

## Example

```javascript
// import the module
import RNMusicMetadata from 'react-native-music-metadata';

// get the metadata for a list of files
RNMusicMetadata.getMetadata(['/path/to/your/track.mp3'])
  .then((tracks) => {
    tracks.forEach((track) => {
      console.log(`${track.title} by ${track.artist}`);
    });
  });
  .catch((err) => {
    console.error(err);
  });
```

## API

### `getMetadata(uris: string[]): Promise<MusicMetadataItem[]>`

Reads the music metadata for each uri.

The returned promise resolves with an array of objects with the following properties:

```
type MusicMetadataItem = {
  albumArtist: string;     // The artist of the album
  albumName: string;     // The name of the album
  artist: string;     // The artist of the track
  title: string;     // The title of the track
  uri: string;     // The path to the track
  duration: number;     // Length of the track in seconds
};
```
