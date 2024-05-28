import { combineReducers } from 'redux';

import * as actionTypes from './actionTypes';

const initialState = {
    getCycles: [{}]
}

const getCycles = (state = initialState.getCycles, action) => {

    switch (action.type) {

        case actionTypes.GET_CYCLES_COMPLETED:
            return action.getCycles;

        default:
            return state;

    }

}

const reducer = combineReducers({
    getCycles
});

export default reducer;
