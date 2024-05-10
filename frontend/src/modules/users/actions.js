import backend from '../../backend';
import * as actionTypes from './actionTypes';

const loginCompleted = authenticatedUser => ({
    type: actionTypes.LOGIN_COMPLETED,
    authenticatedUser
});

export const login = (email, password, onSuccess, onErrors, reauthenticationCallback) => dispatch =>
    backend.userService.login(email, password,
        authenticatedUser => {
            dispatch(loginCompleted(authenticatedUser));
            onSuccess();
        },
        onErrors,
        reauthenticationCallback
    );

export const tryLoginFromServiceToken = reauthenticationCallback => dispatch =>
    backend.userService.tryLoginFromServiceToken(
        authenticatedUser => {
            if (authenticatedUser) {
                dispatch(loginCompleted(authenticatedUser));
            }
        },
        reauthenticationCallback
    );

export const logout = () => {
    backend.userService.logout();
    return { type: actionTypes.LOGOUT };
}

const signUpCompleted = authenticatedUser => ({
    type: actionTypes.SIGN_UP_COMPLETED,
    authenticatedUser
});

export const signUp = (user, onSuccess, onErrors, reauthenticationCallback) => dispatch =>
    backend.userService.signUp(user,
        authenticatedUser => {
            dispatch(signUpCompleted(authenticatedUser));
            onSuccess();
        },
        onErrors,
        reauthenticationCallback);

const getClientsCompleted = getClients => ({
    type: actionTypes.GET_CLIENTS_COMPLETED,
    getClients
});

export const getClients = (id, onSuccess, onErrors) => dispatch =>
    backend.userService.getClients(id,
        getClients => {
            dispatch(getClientsCompleted(getClients));
            onSuccess();
        }, onErrors
    );
