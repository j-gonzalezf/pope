import { combineReducers } from 'redux';

import * as actionTypes from './actionTypes';

const initialState = {
    getCycles: [{}],
    getCycle: {},
    getExercises: [{}],
    getExercise: {},
    getTemplates: [{}],
    getTemplate: {},
    getTemplateRows: [{}]
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

        case actionTypes.GET_EXERCISE_COMPLETED:
            return action.getExercise;

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

const getTemplate = (state = initialState.getTemplate, action) => {

    switch (action.type) {

        case actionTypes.GET_TEMPLATE_COMPLETED:
            return action.getTemplate;

        case actionTypes.UPDATE_TEMPLATE_COMPLETED:
            return action.getTemplate;

        default:
            return state;

    }

}

const getTemplateRows = (state = initialState.getTemplateRows, action) => {

    switch (action.type) {

        case actionTypes.GET_TEMPLATE_ROWS_COMPLETED:
            return action.getTemplateRows;

        default:
            return state;

    }

}

const reducer = combineReducers({
    getCycles,
    getCycle,
    getExercises,
    getExercise,
    getTemplates,
    getTemplate,
    getTemplateRows
});

export default reducer;
