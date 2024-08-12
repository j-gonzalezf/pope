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
