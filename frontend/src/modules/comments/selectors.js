const getModuleState = state => state.comments;

export const getComments = state =>
    getModuleState(state).getComments;
