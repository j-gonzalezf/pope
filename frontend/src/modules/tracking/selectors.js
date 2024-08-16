const getModuleState = state => state.tracking;

export const getSensation = state =>
    getModuleState(state).getSensation;
