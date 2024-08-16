import * as actions from './actions';
import * as actionTypes from './actionTypes';
import reducer from './reducer';
import * as selectors from './selectors';

export { default as SensationsModal } from './components/SensationsModal';
export { default as SensationUpdateModal } from './components/SensationsUpdateModal';

// eslint-disable-next-line
export default { actions, actionTypes, reducer, selectors };