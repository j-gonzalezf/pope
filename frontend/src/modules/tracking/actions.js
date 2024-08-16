import backend from '../../backend';
import * as actionTypes from './actionTypes';

const sensationsRegisterCompleted = () => ({
    type: actionTypes.SENSATIONS_REGISTER_COMPLETED
});

export const registerSensations = (sensations, onSuccess, onErrors) => dispatch =>
    backend.trackingService.sensationsRegister(sensations,
        () => {
            dispatch(sensationsRegisterCompleted());
            onSuccess();
        },
        onErrors
    );

const getSensationCompleted = getSensation => ({
    type: actionTypes.GET_SENSATION_COMPLETED,
    getSensation
});

export const getSensation = (id, onSuccess, onErrors) => dispatch =>
    backend.trackingService.getSensation(id,
        getSensation => {
            dispatch(getSensationCompleted(getSensation));
            onSuccess();
        },
        onErrors
    );

export const clearSensation = () => ({
    type: actionTypes.CLEAR_SENSATION,
});

const updateSensationCompleted = getSensation => ({
    type: actionTypes.UPDATE_SENSATION_COMPLETED,
    getSensation
});

export const updateSensation = (sensation, onSuccess, onErrors) => dispatch =>
    backend.trackingService.updateSensation(sensation,
        () => {
            dispatch(updateSensationCompleted(sensation));
            onSuccess();
        },
        onErrors
    );
