const getModuleState = state => state.users;

export const getUser = state =>
    getModuleState(state).user;

export const isLoggedIn = state =>
    !!getUser(state);

export const getEmail = state =>
    getUser(state) ? getUser(state).email : '';

export const getIcon = state =>
    getUser(state) ? getUser(state).icon : '';

export const getClients = state => {
    return getModuleState(state).getClients;
}

export const getClientInfo = state => {
    return getModuleState(state).getClientInfo;
}
