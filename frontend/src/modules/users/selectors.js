const getModuleState = state => state.users;

export const getUser = state =>
    getModuleState(state).user;

export const isLoggedIn = state =>
    !!getUser(state);

export const getIcon = state =>
    getUser(state) ? getUser(state).icon : '';

export const getClients = state =>
    getModuleState(state).getClients;


export const getClientInfo = state =>
    getModuleState(state).getClientInfo;
