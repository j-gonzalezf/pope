const getModuleState = state => state.tracking;

export const getSensations = state =>
    getModuleState(state).getSensations;

export const getSensation = state =>
    getModuleState(state).getSensation;
