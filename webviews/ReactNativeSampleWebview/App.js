import React, {Component} from 'react';
import {SafeAreaView, StatusBar, View, Button} from 'react-native';
import {WebView} from 'react-native-webview';
import URL from 'url';

const BASE_URL = 'https://connect.dapi.co/v2/connection';
const environment = 'production'; // Can be sandbox or production
const showLogo = false; //Hide or show all logos
const isMobile = false; //Hide or show the 'X' button on the list of banks screen
const isExperimental = false; //Hide or show the experimental banks
const countries = ['AE'];

const CONNECT_LINK = {
  ERROR: 'v2/connection/error',
  SUCCESS: 'v2/connection/success',
  EXIT: 'v2/connection/exit',
};

const appKey =
  '0c96cfce3258c7eeb5299d5d6a847cf66c5ea5ac4a263a0cf6a3930b954fff6f';

const connectURL = `${BASE_URL}?appKey=${appKey}&environment=${environment}&isMobile=true&isWebview=true&countries=${JSON.stringify(
  countries,
)}&isExperimental=${isExperimental}&showLogo=${showLogo}&isMobile=${isMobile}`;

class App extends Component {
  // parseQuery -> parse query parameters into an Object
  parseQuery = (queryString) => {
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
  onShouldStartLoadWithRequest = (navigator) => {
    const {url} = navigator;
    const uri = URL.parse(url);

    if (uri.pathname.includes(CONNECT_LINK.ERROR)) {
      const params = this.parseQuery(uri.search);
    } else if (uri.pathname.includes(CONNECT_LINK.SUCCESS)) {
      const params = this.parseQuery(uri.search);
      //Contact your backend for Exchange Token
    } else if (uri.pathname.includes(CONNECT_LINK.EXIT)) {
      const params = this.parseQuery(uri.search);
      //If params exist it came from Find a bank request
    }

    return true;
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
              ref={(ref) => (this.webview = ref)}
              source={{
                uri: connectURL,
              }}
              onNavigationStateChange={this.onShouldStartLoadWithRequest}
            />
          )}
        </SafeAreaView>
      </View>
    );
  }
}

export default App;
