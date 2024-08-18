import { combineReducers } from 'redux';

import * as actionTypes from './actionTypes';

const initialState = {
    getSensations: [],
    getSensation: null,
}

const getSensations = (state = initialState.getSensations, action) => {

    switch (action.type) {

        case actionTypes.GET_SENSATIONS_COMPLETED:
            return action.getSensations;

        case actionTypes.CLEAR_SENSATIONS:
            return [];

        default:
            return state;

    }

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
    getSensations,
    getSensation
});

export default reducer;
