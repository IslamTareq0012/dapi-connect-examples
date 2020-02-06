import React, {Component} from 'react';
import {SafeAreaView, StatusBar, View, Button} from 'react-native';
import {WebView} from 'react-native-webview';
import URL from 'url';

const baseUrl = 'https://connect.dapi.co';
const environment = 'sandbox';
const redirectUri = 'https://google.com';
const appKey =
  '9768810699237332cdd9a4791d26f921790ffa72c44733675644580f556cd345';
const countries = ['AE'];

const dapiUrl = `${baseUrl}?appKey=${appKey}&environment=${environment}&redirectUri=${redirectUri}&isMobile=true&isWebview=true&countries=${JSON.stringify(
  countries,
)}`;

class App extends Component {
  // parseQuery -> parse query parameters into an Object
  parseQuery = queryString => {
    const query = {};
    let pairs = (queryString[0] === '?'
      ? queryString.substr(1)
      : queryString
    ).split('&');
    for (let i = 0; i < pairs.length; i++) {
      let pair = pairs[i].split('=');
      query[decodeURIComponent(pair[0])] = decodeURIComponent(pair[1] || '');
    }
    return query;
  };

  /*
   * This function is called before the webview will load a url.
   * Connect communicates in webview by passing custom uri.
   * We make sure that we dont load the webview if its a custom connect uri,
   * instead we parse the uri and capture the params.
   */
  onShouldStartLoadWithRequest = navigator => {
    const {url} = navigator;
    const uri = URL.parse(url);
    if (uri.protocol === 'dapiconnect:') {
      const query = this.parseQuery(uri.search);
      if (uri.host === 'event') {
        console.log('Event has been fired');
        // The event action is fired as the user moves through connect
        Object.keys(query).map(key => {
          console.log(`${key} : ${query[key]} `);
        });
      } else if (uri.host === 'error') {
        console.log('error_has_happened');
        // The error action is fired whenever an error occurs in connect
        Object.keys(query).map(key => {
          console.log(`${key} : ${query[key]} `);
        });
      } else if (uri.host === 'exit') {
        console.log('exit_has_happened');
        Object.keys(query).map(key => {
          console.log(`${key} : ${query[key]} `);
        });
        // Close the webview
        this.setState({
          isOpen: false,
        });
      } else if (uri.host === 'connected') {
        console.log('connected_has_happened');
        // Take this access_code and exchange it for an access_token
        Object.keys(query).map(key => {
          console.log(`${key} : ${query[key]} `);
        });
        // Close the webview
        this.setState({
          isOpen: false,
        });
      }
      // Prevent loading of custom dapi uri.
      return false;
    } else if (uri.protocol === 'http:' || uri.protocol === 'https:') {
      // Handle http:// and https:// links inside of dapi,
      // This is necessary for redirects
      return true;
    }
  };

  state = {
    isOpen: false,
  };
  render() {
    const styles = !this.state.isOpen
      ? {
          justifyContent: 'center',
          alignItems: 'center',
        }
      : {};
    return (
      <View style={{flex: 1}}>
        <StatusBar barStyle="dark-content" />
        <SafeAreaView
          style={{
            flex: 1,
            ...styles,
          }}>
          {!this.state.isOpen ? (
            <Button
              title="Connect Your Bank"
              onPress={() => {
                this.setState({
                  isOpen: true,
                });
              }}
            />
          ) : (
            <WebView
              ref={ref => (this.webview = ref)}
              source={{
                uri: dapiUrl,
              }}
              onShouldStartLoadWithRequest={this.onShouldStartLoadWithRequest}
            />
          )}
        </SafeAreaView>
      </View>
    );
  }
}

export default App;
