import {
  NativeModules,
} from 'react-native';

const { RNMusicMetadata } = NativeModules;

const MusicMetadataWrapper = {

  getMetadata: uris => RNMusicMetadata.getMetadata(uris),

};

export default MusicMetadataWrapper;
