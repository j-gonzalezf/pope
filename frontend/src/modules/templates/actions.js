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
