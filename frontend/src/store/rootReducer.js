import { combineReducers } from 'redux';

import app from '../modules/app';
import comments from '../modules/comments';
import templates from '../modules/templates';
import tracking from '../modules/tracking';
import users from '../modules/users';

const rootReducer = combineReducers({
    app: app.reducer,
    comments: comments.reducer,
    templates: templates.reducer,
    tracking: tracking.reducer,
    users: users.reducer
});

export default rootReducer;
