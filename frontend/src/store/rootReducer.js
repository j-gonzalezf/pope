import { combineReducers } from 'redux';

import app from '../modules/app';
import templates from '../modules/templates';
import users from '../modules/users';

const rootReducer = combineReducers({
    app: app.reducer,
    templates: templates.reducer,
    users: users.reducer
});

export default rootReducer;
