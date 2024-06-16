import { combineReducers } from 'redux';

import * as actionTypes from './actionTypes';

const initialState = {
    getCycles: [{}],
    getCycle: {}
}

const getCycles = (state = initialState.getCycles, action) => {

    switch (action.type) {

        case actionTypes.GET_CYCLES_COMPLETED:
            return action.getCycles;

        default:
            return state;

    }

}

const getCycle = (state = initialState.getCycle, action) => {

    switch (action.type) {

        case actionTypes.GET_CYCLE_COMPLETED:
            return action.getCycle;

        case actionTypes.UPDATE_CYCLE_COMPLETED:
            return action.getCycle;
        
        case actionTypes.CLEAR_CYCLE:
            return {};

        default:
            return state;

    }

}

const reducer = combineReducers({
    getCycles,
    getCycle
});

export default reducer;
