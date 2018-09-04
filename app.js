import React from 'react';
import {
  StyleSheet,
  View
} from 'react-native';
import BackgroundColor from 'react-native-background-color';
import {Provider} from 'react-redux';
import {createStore, applyMiddleware} from 'redux';
import ReduxThunk from 'redux-thunk';
import reducers from './store/reducers';
import Router from './router';

class App extends React.Component {
  constructor(props){
    super(props);
  }
  componentDidMount() {
    BackgroundColor.setColor('#005793');
  }
  render() {
    return (
        <Provider store={createStore(reducers,{}, applyMiddleware(ReduxThunk))}>
            <View style={styles.container}>  
                <Router/>
            </View>
         </Provider>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
  },
});

export default App;
