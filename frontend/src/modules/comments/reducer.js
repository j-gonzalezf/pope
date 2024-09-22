import { combineReducers } from 'redux';

import * as actionTypes from './actionTypes';

const initialState = {
    getComments: [],
}

const getComments = (state = initialState.getComments, action) => {

    switch (action.type) {

        case actionTypes.GET_TEMPLATE_COMMENTS_COMPLETE:
            return action.getComments;

        default:
            return state;
    }

}

const reducer = combineReducers({
    getComments
});

export default reducer;
