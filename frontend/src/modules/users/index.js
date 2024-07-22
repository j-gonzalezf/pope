import * as actions from './actions';
import * as actionTypes from './actionTypes';
import reducer from './reducer';
import * as selectors from './selectors';

export { default as Login } from './components/Login';
export { default as SignUp } from './components/SignUp';
export { default as Logout } from './components/Logout';
export { default as ClientsList } from './components/ClientsList';
export { default as AddClient } from './components/AddClient';
export { default as UpdateProfile } from './components/UpdateProfile';
export { default as ChangePassword } from './components/ChangePassword';
export { default as UpdateClient } from './components/UpdateClient';
export { default as ClientDetails } from './components/ClientDetails';

// eslint-disable-next-line
export default { actions, actionTypes, reducer, selectors };
