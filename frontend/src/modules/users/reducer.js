import { combineReducers } from 'redux';

import * as actionTypes from './actionTypes';

const initialState = {
    user: null,
    getClients: [],
    getClientInfo: null,
    getTrainerInfo: null,
    successMessage: null
}

const user = (state = initialState.user, action) => {

    switch (action.type) {

        case actionTypes.LOGIN_COMPLETED:
            return action.authenticatedUser.user;

        case actionTypes.LOGOUT:
            return initialState.user;

        case actionTypes.SIGN_UP_COMPLETED:
            return action.authenticatedUser.user;

        case actionTypes.UPDATE_PROFILE_COMPLETED:
            return action.user;

        default:
            return state;

    }

}

const getClients = (state = initialState.getClients, action) => {

    switch (action.type) {

        case actionTypes.GET_CLIENTS_COMPLETED:
            return action.getClients;

        default:
            return state;

    }

}

const getClientInfo = (state = initialState.getClientInfo, action) => {

    switch (action.type) {

        case actionTypes.GET_CLIENT_INFO_COMPLETED:
            return action.getClientInfo;

        case actionTypes.UPDATE_CLIENT_COMPLETED:
            return action.getClientInfo;

        default:
            return state;

    }

}

const getTrainerInfo = (state = initialState.getTrainerInfo, action) => {

    switch (action.type) {

        case actionTypes.GET_TRAINER_INFO_COMPLETED:
            return action.getTrainerInfo;

        default:
            return state;

    }

}

const successMessage = (state = initialState.successMessage, action) => {
    switch (action.type) {
        case actionTypes.SHOW_SUCCESS_MESSAGE:
            return action.payload;
        case actionTypes.HIDE_SUCCESS_MESSAGE:
            return null;
        default:
            return state;
    }
};

const reducer = combineReducers({
    user,
    getClients,
    getClientInfo,
    getTrainerInfo,
    successMessage
});

export default reducer;
