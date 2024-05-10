import { combineReducers } from 'redux';

import * as actionTypes from './actionTypes';

const initialState = {
    user: null,
    getClients: [{}]
}

const user = (state = initialState.user, action) => {

    switch (action.type) {

        case actionTypes.LOGIN_COMPLETED:
            return action.authenticatedUser.user;

        case actionTypes.LOGOUT:
            return initialState.user;

        case actionTypes.SIGN_UP_COMPLETED:
            return action.authenticatedUser.user;    

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

const reducer = combineReducers({
    user,
    getClients
});

export default reducer;
