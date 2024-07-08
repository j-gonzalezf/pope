import { combineReducers } from 'redux';

import * as actionTypes from './actionTypes';

const initialState = {
    getCycles: [{}],
    getCycle: {},
    getExercises: [{}],
    getExercise: {},
    getTemplates: [{}]
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

const getExercises = (state = initialState.getExercises, action) => {

    switch (action.type) {

        case actionTypes.GET_EXERCISES_COMPLETED:
            return action.getExercises;

        default:
            return state;

    }

}

const getExercise = (state = initialState.getExercise, action) => {

    switch (action.type) {

        case actionTypes.UPDATE_EXERCISE_COMPLETED:
            return action.getExercise;

        case actionTypes.CLEAR_EXERCISE:
            return {};

        default:
            return state;

    }

}

const getTemplates = (state = initialState.getTemplates, action) => {

    switch (action.type) {

        case actionTypes.GET_TEMPLATES_COMPLETED:
            return action.getTemplates;

        default:
            return state;

    }

}

const reducer = combineReducers({
    getCycles,
    getCycle,
    getExercises,
    getExercise,
    getTemplates
});

export default reducer;
