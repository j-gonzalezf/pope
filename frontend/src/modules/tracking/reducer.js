import { combineReducers } from 'redux';

import * as actionTypes from './actionTypes';

const initialState = {
    getWeights: [],
    getSensations: [],
    getSensation: null,
}

const getWeights = (state = initialState.getWeights, action) => {

    switch (action.type) {

        case actionTypes.GET_WEIGHTS_COMPLETED:
            return action.getWeights;
        
        case actionTypes.CLEAR_SENSATIONS:
            return [];

        default:
            return state;

    }

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
    getWeights,
    getSensations,
    getSensation
});

export default reducer;
