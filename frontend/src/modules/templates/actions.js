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
