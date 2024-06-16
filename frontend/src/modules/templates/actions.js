import backend from '../../backend';
import * as actionTypes from './actionTypes';

const createCycleCompleted = () => ({
    type: actionTypes.CREATE_CYCLE_COMPLETED
});

export const createCycle = (cycle, onSuccess, onErrors) => dispatch =>
    backend.templateService.createCycle(cycle,
        () => {
            dispatch(createCycleCompleted());
            onSuccess();
        },
        onErrors
    );

const updateCycleCompleted = getCycle => ({
    type: actionTypes.UPDATE_CYCLE_COMPLETED,
    getCycle
});

export const updateCycle = (cycle, onSuccess, onErrors) => dispatch =>
    backend.templateService.updateCycle(cycle,
        cycle => {
            dispatch(updateCycleCompleted(cycle));
            onSuccess();
        },
        onErrors
    );

const getCyclesCompleted = getCycles => ({
    type: actionTypes.GET_CYCLES_COMPLETED,
    getCycles
});

export const getCycles = (trainerId, clientId, onSuccess, onErrors) => dispatch =>
    backend.templateService.getCycles(trainerId, clientId,
        getCycles => {
            dispatch(getCyclesCompleted(getCycles));
            onSuccess();
        },
        onErrors
    );

const getCycleCompleted = getCycle => ({
    type: actionTypes.GET_CYCLE_COMPLETED,
    getCycle
});

export const getCycle = (id, onSuccess, onErrors) => dispatch =>
    backend.templateService.getCycle(id,
        getCycle => {
            dispatch(getCycleCompleted(getCycle));
            onSuccess();
        },
        onErrors
    );

export const clearCycle = () => ({
    type: actionTypes.CLEAR_CYCLE
});

const addExerciseCompleted = () => ({
    type: actionTypes.ADD_EXERCISE_COMPLETED
});

export const addExercise = (exercise, onSuccess, onErrors) => dispatch =>
    backend.templateService.addExercise(exercise,
        () => {
            dispatch(addExerciseCompleted());
            onSuccess();
        },
        onErrors
    );
