import React from 'react';
import { connect } from 'react-redux';
import { Scene, Router, ActionConst, Tabs } from 'react-native-router-flux';

import LoginForm from './components/Profile/LoginForm';
import ForgotPasswordView from './components/Profile/ForgotPasswordView';
import UpcomingTripView from './components/UpcomingTrip/UpcomingTripView';
import { TabIcon } from './components/common';
import TripInfoView from './components/TripCommon/TripInfoView';
import PaymentView from './components/Payment/PaymentView';
import RunningTrip from './components/RunningTrip/RunningTrip';
import CompletedTripsView from './components/CompletedTrip/CompletedTripsView';
import HelpView from './components/HelpView';
import MyProfileView from './components/Profile/MyProfileView';
import { getCompletedTrips, getRunningTrip, getUpcomingTrips, getUserDetails } from './store/actions';

class RouterComponent extends React.Component{
  constructor(props) {
    super(props);
  }
  render() {
    return (
      <Router>
        <Scene key="root">
          <Scene key="auth" hideNavBar initial type={ActionConst.RESET}>
            <Scene 
              key="login" 
              component={LoginForm}
            />
            <Scene 
              key="forgotPasswordView" 
              component={ForgotPasswordView} 
              title=""
            />
          </Scene>
          <Tabs 
            key="tabs" showLabel={false} hideNavBar
            tabBarPosition="bottom" type={ActionConst.RESET} 
            tabBarStyle={{backgroundColor: '#005793'}} 
          >
            <Scene 
              key="upcoming" 
              icon={({focused})=><TabIcon 
                iconName="upcoming"
                iconSize="hdpi"
                label="UPCOMING"
                opacity={focused? 1 : 0.5}
              />}
              inactiveBackgroundColor="#aaa"
              onEnter={()=>this.props.getUpcomingTrips()}
              component={UpcomingTripView}
              hideNavBar
            />
            <Scene 
              key="completed" 
              icon={TabIcon}
              icon={({focused})=><TabIcon 
                iconName="completed"
                iconSize="hdpi"
                label="COMPLETED"
                opacity={focused? 1 : 0.5}
              />}
              component={CompletedTripsView} 
              onEnter={()=>this.props.getCompletedTrips()}
              hideNavBar
            />
            <Scene 
              key="running" 
              icon={({focused})=><TabIcon 
                iconName="running"
                iconSize="hdpi"
                label="RUNNING"
                opacity={focused? 1 : 0.5}
              />}
              component={RunningTrip}
              onEnter={()=>this.props.getRunningTrip()} 
              hideNavBar
              hideTabBar
            />
            <Scene 
              key="help" 
              icon={({focused})=><TabIcon 
                iconName="help"
                iconSize="hdpi"
                label="HELP"
                opacity={focused? 1 : 0.5}
              />}
              component={HelpView} 
              onEnter={()=>console.log("help")}
              hideNavBar
            />
            <Scene 
              key="profile" 
              icon={({focused})=><TabIcon 
                iconName="myaccount"
                iconSize="hdpi"
                label="PROFILE"
                labelColor={focused? "#f8f4f4" : "#ffffff"}
                opacity={focused? 1 : 0.5}
              />}
              component={MyProfileView} 
              onEnter={()=>this.props.getUserDetails()}
              hideNavBar
            />
          </Tabs>
          <Scene 
            key="tripinfo"
            component={TripInfoView}
            hideNavBar
          />
          <Scene 
            key="paymentview"
            component={PaymentView} 
            hideNavBar
          />
        </Scene>
      </Router>
    );
  }
}

const mapStateToProps = (state) => {
  const {refreshing} = state.commonReducer;
  return {refreshing};
};

export default connect(mapStateToProps, {
  getCompletedTrips, getRunningTrip, getUpcomingTrips, getUserDetails
})(RouterComponent);
