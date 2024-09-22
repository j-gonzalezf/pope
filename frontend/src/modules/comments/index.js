import * as actions from './actions';
import * as actionTypes from './actionTypes';
import reducer from './reducer';
import * as selectors from './selectors';

export { default as AddComment } from './components/AddComment';

// eslint-disable-next-line
export default { actions, actionTypes, reducer, selectors };
