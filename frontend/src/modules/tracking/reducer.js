import { combineReducers } from 'redux';

import * as actionTypes from './actionTypes';

const initialState = {
    getSensation: null,
}

const getSensation = (state = initialState.getSensation, action) => {

    switch (action.type) {

        case actionTypes.GET_SENSATION_COMPLETED:
            return action.getSensation;

        case actionTypes.UPDATE_SENSATION_COMPLETED:
            return action.getSensation;

        case actionTypes.CLEAR_SENSATION:
            return null;

        default:
            return state;

    }

}

const reducer = combineReducers({
    getSensation
});

export default reducer;
